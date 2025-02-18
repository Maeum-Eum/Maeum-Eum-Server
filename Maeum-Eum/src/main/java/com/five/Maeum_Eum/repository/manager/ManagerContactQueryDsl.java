package com.five.Maeum_Eum.repository.manager;

import com.five.Maeum_Eum.entity.center.QCenter;
import com.five.Maeum_Eum.entity.user.caregiver.*;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.five.Maeum_Eum.entity.user.elder.ServiceSlot;
import com.five.Maeum_Eum.entity.user.manager.ManagerContact;
import com.five.Maeum_Eum.entity.user.manager.QManager;
import com.five.Maeum_Eum.entity.user.manager.QManagerContact;
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

        // 5. [필] 어르신 필요 서비스 필터링
        BooleanExpression serviceFilter = resume.mealLevel.goe(elder.getMealLevel())
                .and(resume.toiletingLevel.goe(elder.getToiletingLevel()))
                .and(resume.dailyLevel.goe(elder.getDailyLevel()))
                .and(resume.mobilityLevel.goe(elder.getMobilityLevel()));

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
    public Page<ManagerContact> findContactsByFieldAndCenterWithinDistance(String pointWKT,
                                                                   double distanceValue,
                                                                   Pageable pageable,
                                                                   Caregiver caregiver) {

        QManagerContact contact = QManagerContact.managerContact;
        QManager manager = QManager.manager;
        QCenter center = QCenter.center;
        QCaregiver qCaregiver = QCaregiver.caregiver;

        // 거리 계산 (미터 단위)
        NumberExpression<Double> distanceExpr = Expressions.numberTemplate(
                Double.class,
                "ST_Distance_Sphere({0}, ST_GeomFromText({1}, 4326))",
                center.location,
                pointWKT
        );

        // n km 이내 조건
        BooleanExpression withinDistance = distanceExpr.loe(distanceValue);

        JPAQuery<ManagerContact> query = jpaQueryFactory
                .selectFrom(contact)
                .join(contact.manager, manager)
                .join(manager.center, center)
                .join(contact.caregiver, qCaregiver)
                .where(contact.caregiver.eq(caregiver))
                // 정렬: 거리에 따라 오름차순 정렬 (가까운 순)
                .orderBy(distanceExpr.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // 결과 리스트 조회
        List<ManagerContact> contacts = query.fetch();
        // 전체 건수 조회 (페이징 처리를 위한 count)
        long total = query.fetchCount();

        return new PageImpl<>(contacts, pageable, total);
    }
}
