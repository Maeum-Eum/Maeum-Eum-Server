package com.five.Maeum_Eum.repository.manager;
import com.five.Maeum_Eum.dto.user.caregiver.main.response.QSimpleContactDTO;
import com.five.Maeum_Eum.dto.user.caregiver.main.response.SimpleContactDTO;
import com.five.Maeum_Eum.entity.center.QCenter;
import com.five.Maeum_Eum.entity.user.caregiver.*;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.five.Maeum_Eum.entity.user.elder.QElder;
import com.five.Maeum_Eum.entity.user.elder.QSavedElders;
import com.five.Maeum_Eum.entity.user.elder.ServiceSlot;
import com.five.Maeum_Eum.entity.user.manager.ApprovalStatus;
import com.five.Maeum_Eum.entity.user.manager.QManager;
import com.five.Maeum_Eum.entity.user.manager.QManagerContact;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Repository;
import java.time.LocalTime;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ManagerContactQueryDsl {

    private final JPAQueryFactory jpaQueryFactory;

    // 최종
    public List<Caregiver> findCaregiverByLowMatchingSystem(Elder elder, int count, double distance){

        QCaregiver caregiver = QCaregiver.caregiver;
        QResume resume = QResume.resume;

        // 1. [필] 이력서 등록 여부
        BooleanExpression isResumeRegistred = caregiver.isResumeRegistered.eq(true);

        // 2. [필] 요양보호사 자격증 번호 등록 여부
        BooleanExpression isCertificateRegistred = caregiver.isResumeRegistered.eq(true);

        return jpaQueryFactory
                .select(caregiver)
                .from(resume)
                .join(resume.caregiver, caregiver)
                .where(
                        // 1. [필] 이력서 등록 여부
                        isResumeRegistred,
                        // 2. [필] 요양보호사 자격증 번호 등록 여부
                        isCertificateRegistred
                )
                .limit(count)
                .fetch();
    }

    // 최종
    public List<Caregiver> findCaregiverByFullMatchingSystem(Elder elder, int count, double distance){

        QCaregiver caregiver = QCaregiver.caregiver;
        QResume resume = QResume.resume;

        // 1. [필] 이력서 등록 여부
        BooleanExpression isResumeRegistred = caregiver.isResumeRegistered.eq(true);

        // 2. [필] 요양보호사 자격증 번호 등록 여부
        BooleanExpression isCertificateRegistred = caregiver.isResumeRegistered.eq(true);

        // 3. [필] 장기 요양 등급별 필터링 : 어르신의 등급보다 요양사의 서비스 가능 등급이 이상이어야 함.
        BooleanExpression rankFilter = resume.elderRankLevel.goe(elder.getElderRank());

        // 4. [필] 성별 조건 필터링 : 선호 성별이 모두 가능[EVERY]인 경우에는 노출
        BooleanExpression genderFilter = resume.preferredGender.stringValue().eq(elder.getGender())
                .or(resume.preferredGender.eq(Resume.PreferredGender.EVERY));

        // 5-1 [필] 어르신 필요 서비스 필터링 (일상 제외)
        BooleanExpression serviceFilter = resume.mealLevel.goe(elder.getMealLevel())
                .or(resume.toiletingLevel.goe(elder.getToiletingLevel()))
                .or(resume.mobilityLevel.goe(elder.getMobilityLevel()));

        // 5-2 [필] 일상 6요소에 대한 쿼리 - 단 elder의 필드가 1일때만 1이면 괜찮다.
        BooleanExpression dailyFilter = resume.dailyFilter1.goe(elder.isDailyFilter1())
                .and(resume.dailyFilter2.goe(elder.isDailyFilter2()))
                .and(resume.dailyFilter3.goe(elder.isDailyFilter3()))
                .and(resume.dailyFilter4.goe(elder.isDailyFilter4()))
                .and(resume.dailyFilter5.goe(elder.isDailyFilter5()))
                .and(resume.dailyFilter6.goe(elder.isDailyFilter6()));

        // 6. 거리 조건 - 어르신의 주소지 위치와 요양사의 주소지 위치를 계산하고 이력서상 요양사의 가능 근무범위 (예시 : 5 Km 이내) 에 해당되는지 체크 후 필터링
        NumberExpression<Double> distanceFilter = Expressions.numberTemplate(
                        Double.class,
                "ST_Distance_Sphere({0}, {1})",
                        elder.getLocation(),
                        caregiver.location
                );

        // 7. [필] 시간 조건 : 근로자의 근무 가능 요일자가 어르신 요구 요일자 및 시간대를 전부 가지고 있어야 할 것
        Set<Integer> requiredWorkTimeSlot = new HashSet<>();

        // 각 시간대의 기준 시간 정의
        LocalTime morningStart = LocalTime.of(9, 0);
        LocalTime morningEnd   = LocalTime.of(12, 0);
        LocalTime afternoonStart = LocalTime.of(12, 0);
        LocalTime afternoonEnd   = LocalTime.of(18, 0);
        LocalTime eveningStart = LocalTime.of(18, 0);
        LocalTime eveningEnd   = LocalTime.of(21, 0);

        for (ServiceSlot slot : elder.getServiceSlots()) {

            LocalTime slotStart = slot.getServiceSlotStart();
            LocalTime slotEnd = slot.getServiceSlotEnd();

            // MORNING: 슬롯이 오전 시간대와 일부라도 겹치는 경우
            if (slotEnd.isAfter(morningStart) && slotStart.isBefore(morningEnd)) {
                requiredWorkTimeSlot.add(TimeSlot.MORNING.ordinal());
            }
            // AFTERNOON: 슬롯이 점심(오후) 시간대와 일부라도 겹치는 경우
            if (slotEnd.isAfter(afternoonStart) && slotStart.isBefore(afternoonEnd)) {
                requiredWorkTimeSlot.add(TimeSlot.AFTERNOON.ordinal());
            }
            // EVENING: 슬롯이 저녁 시간대와 일부라도 겹치는 경우
            if (slotEnd.isAfter(eveningStart) && slotStart.isBefore(eveningEnd)) {
                requiredWorkTimeSlot.add(TimeSlot.EVENING.ordinal());
            }
        }

        // 어르신이 원하는 시간대를 모두 추출하여 저장
        BooleanExpression timeSlotFilter = Expressions.TRUE;
        for (int requiredWorkTime : requiredWorkTimeSlot) {

            log.info("요구 시간대" + requiredWorkTime);

            // 시간대를 필터에 추가 한다.
            BooleanExpression slotExpr = Expressions.numberTemplate(
                    Integer.class,
                    "FIND_IN_SET({0}, {1})",
                    requiredWorkTime + "",
                    resume.workTimeSlot

            ).gt(0);
            timeSlotFilter = timeSlotFilter.and(slotExpr);
        }

        BooleanBuilder workDayFilter = new BooleanBuilder();
        for (ServiceSlot slot : elder.getServiceSlots()) {

            // 요일을 필터에 추가 한다.
            BooleanExpression dayExpr = Expressions.numberTemplate(
                    Integer.class,
                    "FIND_IN_SET({0}, {1})",
                    (slot.getServiceSlotDay().ordinal()+1) + "",
                    resume.workDay

            ).gt(0);
            workDayFilter.and(dayExpr);
        }

        return jpaQueryFactory
                .select(caregiver)
                .from(resume)
                .join(resume.caregiver, caregiver)
                .where(

                        // 1. [필] 이력서 등록 여부
                        isResumeRegistred,

                        // 2. [필] 요양보호사 자격증 번호 등록 여부
                        isCertificateRegistred,

                        // 3. [필] 장기 요양 등급별 필터링
                        rankFilter,

                        // 4. [필] 성별 조건 필터링 : 모두 가능인 경우에는 무조건 노출 시키기
                        genderFilter,

                        // 5. [필] 어르신 필요 서비스 필터링
                        serviceFilter,
                        dailyFilter,

                        // 6. [중] 거리 조건 필터링
                        distanceFilter.loe(distance * 1000.0),

                        // 7. [중] 시간 조건
                        timeSlotFilter,
                        workDayFilter
                )
                .limit(count)
                .orderBy(distanceFilter.desc())
                .fetch();
    }

    // 거리기반 매칭
    public Page<SimpleContactDTO> findContacts(String pointWKT,
                                               double distanceValue,
                                               Pageable pageable,
                                               Caregiver caregiver,
                                               int order) {

        QManagerContact contact = QManagerContact.managerContact;
        QManager manager = QManager.manager;
        QCenter center = QCenter.center;
        QCaregiver qCaregiver = QCaregiver.caregiver;
        QResume resume = QResume.resume;

        // 거리 계산 (미터 단위)
        NumberExpression<Double> distanceExpr = Expressions.numberTemplate(
                Double.class,
                "ST_Distance_Sphere({0}, {1})",
                contact.elder.location,
                caregiver.getLocation()
        );

        // n km 이내 조건
        BooleanExpression withinDistance = distanceExpr.loe(distanceValue * 1000.0);

        // 업무 가능 여부 확인
        BooleanExpression meal = resume.caregiver.eq(caregiver)
                        .and(resume.mealLevel.goe(QElder.elder.mealLevel));
        BooleanExpression toileting = resume.caregiver.eq(caregiver)
                        .and(resume.toiletingLevel.goe(QElder.elder.toiletingLevel));
        BooleanExpression mobility = resume.caregiver.eq(caregiver)
                .and(resume.mobilityLevel.goe(QElder.elder.mobilityLevel));
        BooleanExpression daily = resume.dailyFilter1.goe(QElder.elder.dailyFilter1)
                .and(resume.dailyFilter2.goe(QElder.elder.dailyFilter2))
                .and(resume.dailyFilter3.goe(QElder.elder.dailyFilter3))
                .and(resume.dailyFilter4.goe(QElder.elder.dailyFilter4))
                .and(resume.dailyFilter5.goe(QElder.elder.dailyFilter5))
                .and(resume.dailyFilter6.goe(QElder.elder.dailyFilter6));

        // 가능한 업무 개수
        NumberExpression<Integer> mealCount = Expressions.numberTemplate(
                Integer.class, "CASE WHEN {0} THEN 1 ELSE 0 END", meal);
        NumberExpression<Integer> toiletingCount = Expressions.numberTemplate(
                Integer.class, "CASE WHEN {0} THEN 1 ELSE 0 END", toileting);
        NumberExpression<Integer> mobilityCount = Expressions.numberTemplate(
                Integer.class, "CASE WHEN {0} THEN 1 ELSE 0 END", mobility);
        NumberExpression<Integer> dailyCount = Expressions.numberTemplate(
                Integer.class, "CASE WHEN {0} THEN 1 ELSE 0 END", daily);

        // 개수 총합
        NumberExpression<Integer> workExpr = mealCount
                .add(toiletingCount)
                .add(mobilityCount)
                .add(dailyCount);



        // 일치도 총합
        NumberExpression<Integer> resumeTrueCount =
                Expressions.numberTemplate(Integer.class, "CASE WHEN {0} THEN 1 ELSE 0 END", resume.dailyFilter1)
                        .add(Expressions.numberTemplate(Integer.class, "CASE WHEN {0} THEN 1 ELSE 0 END", resume.dailyFilter2))
                        .add(Expressions.numberTemplate(Integer.class, "CASE WHEN {0} THEN 1 ELSE 0 END", resume.dailyFilter3))
                        .add(Expressions.numberTemplate(Integer.class, "CASE WHEN {0} THEN 1 ELSE 0 END", resume.dailyFilter4))
                        .add(Expressions.numberTemplate(Integer.class, "CASE WHEN {0} THEN 1 ELSE 0 END", resume.dailyFilter5))
                        .add(Expressions.numberTemplate(Integer.class, "CASE WHEN {0} THEN 1 ELSE 0 END", resume.dailyFilter6));

        NumberExpression<Integer> elderTrueCount =
                Expressions.numberTemplate(Integer.class, "CASE WHEN {0} THEN 1 ELSE 0 END", QElder.elder.dailyFilter1)
                        .add(Expressions.numberTemplate(Integer.class, "CASE WHEN {0} THEN 1 ELSE 0 END", QElder.elder.dailyFilter2))
                        .add(Expressions.numberTemplate(Integer.class, "CASE WHEN {0} THEN 1 ELSE 0 END", QElder.elder.dailyFilter3))
                        .add(Expressions.numberTemplate(Integer.class, "CASE WHEN {0} THEN 1 ELSE 0 END", QElder.elder.dailyFilter4))
                        .add(Expressions.numberTemplate(Integer.class, "CASE WHEN {0} THEN 1 ELSE 0 END", QElder.elder.dailyFilter5))
                        .add(Expressions.numberTemplate(Integer.class, "CASE WHEN {0} THEN 1 ELSE 0 END", QElder.elder.dailyFilter6));

        NumberExpression<Integer> trueCountDifference = resumeTrueCount.subtract(elderTrueCount);
        NumberExpression<Integer> totalDifference = resume.mealLevel.subtract(QElder.elder.mealLevel)
                .add(resume.toiletingLevel.subtract(QElder.elder.toiletingLevel))
                .add(resume.mobilityLevel.subtract(QElder.elder.mobilityLevel))
                .add(trueCountDifference);

        // 북마크 여부
        BooleanExpression bookmarked = caregiver != null ? JPAExpressions
                .selectOne()
                .from(QSavedElders.savedElders)
                .join(QSavedElders.savedElders.caregiver, qCaregiver)
                .join(QSavedElders.savedElders.elder, QElder.elder)
                .where(QSavedElders.savedElders.elder.eq(contact.elder), QSavedElders.savedElders.caregiver.eq(caregiver))
                .exists() : Expressions.FALSE;


        OrderSpecifier orderSpecifier = new OrderSpecifier<>(Order.DESC, contact.contactId);
        if (order == 2) orderSpecifier = new OrderSpecifier<>(
                Order.DESC,
                ExpressionUtils.template(Integer.class, "{0}, {1}", workExpr, totalDifference)
        );
        else if (order == 3) orderSpecifier = new OrderSpecifier<>(Order.DESC, contact.wage);

        JPAQuery<SimpleContactDTO> query = jpaQueryFactory
                .select(new QSimpleContactDTO(
                        contact.contactId,
                        center.centerName,
                        contact.elder,
                        contact.createdAt,
                        contact.wage,
                        contact.negotiable,
                        bookmarked,
                        meal,
                        toileting,
                        mobility,
                        daily,
                        contact.workRequirement
                ))
                .from(contact)
                .join(contact.manager, manager)
                .join(manager.center, center)
                .join(contact.caregiver, qCaregiver)
                .join(qCaregiver.resume, resume)
                .where(contact.caregiver.eq(caregiver), withinDistance, contact.approvalStatus.eq(ApprovalStatus.PENDING))
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());



        // 결과 리스트 조회
        List<SimpleContactDTO> contacts = query.fetch();
        // 전체 건수 조회 (페이징 처리를 위한 count)
        long total = query.fetchCount();



        return new PageImpl<>(contacts, pageable, total);
    }

    public Page<SimpleContactDTO> findMyContacts(Caregiver caregiver, Pageable pageable, ApprovalStatus approvalStatus) {
        QManagerContact contact = QManagerContact.managerContact;
        QManager manager = QManager.manager;
        QCenter center = QCenter.center;
        QCaregiver qCaregiver = QCaregiver.caregiver;
        QResume resume = QResume.resume;

        BooleanExpression meal = resume.caregiver.eq(caregiver)
                .and(resume.mealLevel.goe(QElder.elder.mealLevel));
        BooleanExpression toileting = resume.caregiver.eq(caregiver)
                .and(resume.toiletingLevel.goe(QElder.elder.toiletingLevel));
        BooleanExpression mobility = resume.caregiver.eq(caregiver)
                .and(resume.mobilityLevel.goe(QElder.elder.mobilityLevel));
        BooleanExpression daily = resume.dailyFilter1.goe(QElder.elder.dailyFilter1)
                .and(resume.dailyFilter2.goe(QElder.elder.dailyFilter2))
                .and(resume.dailyFilter3.goe(QElder.elder.dailyFilter3))
                .and(resume.dailyFilter4.goe(QElder.elder.dailyFilter4))
                .and(resume.dailyFilter5.goe(QElder.elder.dailyFilter5))
                .and(resume.dailyFilter6.goe(QElder.elder.dailyFilter6));

        BooleanExpression bookmarked = caregiver != null ? JPAExpressions
                .selectOne()
                .from(QSavedElders.savedElders)
                .join(QSavedElders.savedElders.caregiver, qCaregiver)
                .join(QSavedElders.savedElders.elder, QElder.elder)
                .where(QSavedElders.savedElders.elder.eq(contact.elder), QSavedElders.savedElders.caregiver.eq(caregiver))
                .exists() : Expressions.FALSE;

        JPAQuery<SimpleContactDTO> query = jpaQueryFactory
                .select(new QSimpleContactDTO(
                        contact.contactId,
                        center.centerName,
                        contact.elder,
                        contact.createdAt,
                        contact.wage,
                        contact.negotiable,
                        bookmarked,
                        meal.coalesce(false).as("meal"),
                        toileting.coalesce(false).as("toileting"),
                        mobility.coalesce(false).as("mobility"),
                        daily.coalesce(false).as("daily"),
                        contact.workRequirement
                ))
                .from(contact)
                .join(contact.manager, manager)
                .join(manager.center, center)
                .join(contact.caregiver, qCaregiver)
                .where(contact.caregiver.eq(caregiver), contact.approvalStatus.eq(approvalStatus))
                .orderBy(contact.contactId.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<SimpleContactDTO> contacts = query.fetch();
        System.out.println(contacts);
        // 전체 건수 조회 (페이징 처리를 위한 count)
        long total = query.fetchCount();

        return new PageImpl<>(contacts, pageable, total);
    }
}
