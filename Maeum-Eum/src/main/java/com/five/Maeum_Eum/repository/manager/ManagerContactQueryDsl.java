package com.five.Maeum_Eum.repository.manager;

import com.five.Maeum_Eum.dto.manager.CaregiverWithOverlapDto;
import com.five.Maeum_Eum.entity.QCaregiverTime;
import com.five.Maeum_Eum.entity.center.QCenter;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.caregiver.QCaregiver;
import com.five.Maeum_Eum.entity.user.caregiver.QResume;
import com.five.Maeum_Eum.entity.user.caregiver.Resume;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.five.Maeum_Eum.entity.user.elder.ServiceSlot;
import com.five.Maeum_Eum.entity.user.manager.ManagerContact;
import com.five.Maeum_Eum.entity.user.manager.QManager;
import com.five.Maeum_Eum.entity.user.manager.QManagerContact;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ManagerContactQueryDsl {

    private final JPAQueryFactory jpaQueryFactory;

    // 이 주변에서 시급이 가장 높은 어르신 조회

    // 최종
    public List<Caregiver> findCaregiverByFullMatchingSystem(ManagerContact mc, Elder elder, int count){

        QCaregiver caregiver = QCaregiver.caregiver;
        QCaregiverTime caregiverTime = QCaregiverTime.caregiverTime;
        QResume resume = QResume.resume;

        // 1. [필] 이력서 등록 여부
        BooleanExpression isResumeRegistred = caregiver.isResumeRegistered.eq(true);

        // 2. [필] 요양보호사 자격증 번호 등록 여부
        BooleanExpression isCertificateRegistred = caregiver.isResumeRegistered.eq(true);

        // 3. [필] 장기 요양 등급별 필터링
        BooleanExpression rankFilter = Expressions.numberTemplate(Integer.class,
                "FIND_IN_SET({0}, {1})", elder.getElderRank() + "", resume.elderRank).gt(0);

        // 4. [필] 성별 조건 필터링 : 선호 성별이 모두 가능[EVERY]인 경우에는 노출
        BooleanExpression genderFilter = resume.preferredGender.eq(Resume.PreferredGender.valueOf(elder.getGender()))
                .or(resume.preferredGender.eq(Resume.PreferredGender.EVERY));

        // 5. [필] 어르신 필요 서비스 필터링
        BooleanExpression serviceFilter = resume.mealLevel.goe(elder.getMealLevel())
                .and(resume.toiletingLevel.goe(elder.getToiletingLevel()))
                .and(resume.dailyLevel.goe(elder.getDailyLevel()))
                .and(resume.mobilityLevel.goe(elder.getMobilityLevel()));

        // 6. 거리 조건 - 어르신의 주소지 위치와 요양사의 주소지 위치를 계산하고 이력서상 요양사의 가능 근무범위 (N Km 이내) 에 해당되는지 체크 후 필터링
        BooleanExpression distanceFilter = null;

        // 7. 시간 조건 : 근로자의 근무 가능 요일자가 어르신 요구 요일자 중 최소 하루는 포함하고 있어야 필터링.
        // 이후 어르신 요구 요일 Day에 대해서, 어르신의 요구 시간대와 겹치는 시간을 minute 값으로 환산 후 모든 정렬

        NumberExpression<Integer> timeFilter = Expressions.numberTemplate(
                Integer.class,
                "CASE " +
                        "WHEN {4} != {5} THEN 0 " +

                        "WHEN GREATEST({0},{2}) <= LEAST({1}, {3}) " +
                        "THEN 0 " +

                        "WHEN GREATEST({1}, {3}) <= LEAST({0},{2}) " +
                        "THEN 0 " +

                        "WHEN {0} <= {1} AND {2} <= {3} "+
                        "THEN TIMESTAMPDIFF(MINUTE, {1}, {2}) "+

                        "WHEN {0} <= {1} AND {2} >= {3} "+
                        "THEN TIMESTAMPDIFF(MINUTE, {1}, {3}) "+

                        "WHEN {1} <= {0} AND {2} >= {3} "+
                        "THEN TIMESTAMPDIFF(MINUTE, {0}, {3}) "+

                        "WHEN {1} <= {0} AND {2} <= {3} "+
                        "THEN TIMESTAMPDIFF(MINUTE, {0}, {2}) "+
                        "END ",

                caregiverTime.startTime, // 0
                elder.getServiceSlots().get(0).getServiceSlotStart(), //1
                caregiverTime.endTime, //2
                elder.getServiceSlots().get(0).getServiceSlotEnd(), //3
                caregiverTime.workDay, // 4
                elder.getServiceSlots().get(0).getServiceSlotDay() // 5
        );

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

                        // 5. 어르신 필요 서비스 필터링
                        serviceFilter,

                        // 6. [중] 거리 조건 필터링
                        distanceFilter,

                        // 7. [중] 시간 조건 점수
                        timeFilter.gt(0)
                )
                .limit(count)
                .fetch();
    }

    // 도보 15분 이내는 약 1.25km, 도보 20분 이내는 약 1.65 km 로 환산 가능하다.
    // 구현사항 : 1.25, 1.65, 3, 5 반경을 검색하자.
    public List<Caregiver> findCaregiverWithinNKm(String requiredElderRank){
        return null;
    }

    public List<Caregiver> findQualifiedCaregivers(int requiredElderRank){

        QCaregiver caregiver = QCaregiver.caregiver;
        QResume resume = QResume.resume;

        // FIND_IN_SET : (요양보호사의 등급 목록에 elderRank가 포함되는지 확인하기)
        BooleanExpression rankCondition = Expressions.numberTemplate(Integer.class,
                "FIND_IN_SET({0}, {1})", requiredElderRank + "", resume.elderRank).gt(0);

        return jpaQueryFactory
                .select(caregiver)
                .from(resume)
                .join(resume.caregiver, caregiver)
                .where(
                        rankCondition,
                        caregiver.isResumeRegistered.eq(true)
                )
                .limit(20)
                .fetch();
    }

    // ServiceSlot : 어떤 요일에 대한 요구 서비스 시간대에 적합한 요양사를 추출 (테스트 완료)
    public List<CaregiverWithOverlapDto> findCaregiverByMatchingTimeOne(ServiceSlot serviceSlot) {

        QCaregiverTime caregiverTime = QCaregiverTime.caregiverTime;
        QCaregiver caregiver = QCaregiver.caregiver;

        System.out.println("[LOG]"+ serviceSlot.getServiceSlotStart() + serviceSlot.getServiceSlotEnd());

        NumberExpression<Integer> timeDiff = Expressions.numberTemplate(
                Integer.class,
                         "CASE " +

                                 "WHEN {4} != {5} THEN 0 " +

                                 "WHEN GREATEST({0},{2}) <= LEAST({1}, {3}) " +
                                 "THEN 0 " +

                                 "WHEN GREATEST({1}, {3}) <= LEAST({0},{2}) " +
                                 "THEN 0 " +

                                 "WHEN {0} <= {1} AND {2} <= {3} "+
                                 "THEN TIMESTAMPDIFF(MINUTE, {1}, {2}) "+

                                 "WHEN {0} <= {1} AND {2} >= {3} "+
                                 "THEN TIMESTAMPDIFF(MINUTE, {1}, {3}) "+

                                 "WHEN {1} <= {0} AND {2} >= {3} "+
                                 "THEN TIMESTAMPDIFF(MINUTE, {0}, {3}) "+

                                 "WHEN {1} <= {0} AND {2} <= {3} "+
                                 "THEN TIMESTAMPDIFF(MINUTE, {0}, {2}) "+
                                 "END ",

                caregiverTime.startTime, // 0
                serviceSlot.getServiceSlotStart(), //1
                caregiverTime.endTime, //2
                serviceSlot.getServiceSlotEnd(), //3
                caregiverTime.workDay, // 4
                serviceSlot.getServiceSlotDay() // 5
        );

        List<CaregiverWithOverlapDto> caregivers = jpaQueryFactory
                .select(Projections.constructor(CaregiverWithOverlapDto.class, caregiver, timeDiff.sum()))
                .from(caregiverTime)
                .join(caregiver).on(caregiverTime.caregiver.eq(caregiver))
                .groupBy(caregiver.caregiverId)
                .orderBy(timeDiff.sum().desc())
                .limit(10)
                .fetch();

        return caregivers;
    }

    // ServiceSlot : 어떤 어르신의 [월~일]까지 요구 서비스 시간대에 적합한 요양사를 추출한다. (안됨)
    public List<CaregiverWithOverlapDto> findCaregiverByMatchingTimeSlot(List<ServiceSlot> serviceSlotList) {

        QCaregiverTime caregiverTime = QCaregiverTime.caregiverTime;
        QCaregiver caregiver = QCaregiver.caregiver;

        NumberExpression<Integer> totalOverlapExpr = Expressions.numberTemplate(Integer.class, "0");

        for (ServiceSlot slot : serviceSlotList) {
            NumberExpression<Integer> dailyOverlap = Expressions.numberTemplate(
                    Integer.class,
                    "CASE " +
                            "WHEN GREATEST({0}, {2}) >= LEAST({1}, {3}) THEN 0 " + // 시간대 겹치지 않음
                            "WHEN {0} <= {1} AND {2} <= {3} THEN TIMESTAMPDIFF(MINUTE, {1}, {2}) " + // 완전 포함
                            "WHEN {0} <= {1} AND {2} >= {3} THEN TIMESTAMPDIFF(MINUTE, {1}, {3}) " + // 시작점 포함
                            "WHEN {1} <= {0} AND {2} >= {3} THEN TIMESTAMPDIFF(MINUTE, {0}, {3}) " + // 끝점 포함
                            "WHEN {1} <= {0} AND {2} <= {3} THEN TIMESTAMPDIFF(MINUTE, {0}, {2}) " + // 완전히 내부
                            "ELSE 0 END",
                    caregiverTime.startTime,          // {0}
                    slot.getServiceSlotStart(),       // {1}
                    caregiverTime.endTime,            // {2}
                    slot.getServiceSlotEnd(),         // {3}
                    caregiverTime.workDay,           // {4}
                    slot.getServiceSlotDay()          // {5}
            );
            totalOverlapExpr.add(dailyOverlap);
        }

        return jpaQueryFactory
                .select(Projections.constructor(
                        CaregiverWithOverlapDto.class,
                        caregiver,
                        totalOverlapExpr.sum()
                ))
                .from(caregiverTime)
                .join(caregiver).on(caregiverTime.caregiver.eq(caregiver))
                .groupBy(caregiver.caregiverId)
                .orderBy(totalOverlapExpr.sum().desc())
                .limit(10)
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
