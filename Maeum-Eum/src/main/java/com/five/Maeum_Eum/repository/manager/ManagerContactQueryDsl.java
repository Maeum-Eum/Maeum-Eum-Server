package com.five.Maeum_Eum.repository.manager;

import com.five.Maeum_Eum.dto.manager.CaregiverWithOverlapDto;
import com.five.Maeum_Eum.entity.QCaregiverTime;
import com.five.Maeum_Eum.entity.center.QCenter;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.caregiver.QCaregiver;
import com.five.Maeum_Eum.entity.user.elder.DayOfWeek;
import com.five.Maeum_Eum.entity.user.elder.ServiceSlot;
import com.five.Maeum_Eum.entity.user.manager.ManagerContact;
import com.five.Maeum_Eum.entity.user.manager.QManager;
import com.five.Maeum_Eum.entity.user.manager.QManagerContact;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.StringTemplate;
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

import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ManagerContactQueryDsl {
    private final JPAQueryFactory jpaQueryFactory;

    // ServiceSlot : 어떤 어르신의 [월~일]까지 요구 서비스 시간대에 적합한 요양사를 추출한다.
    public List<CaregiverWithOverlapDto> findCaregiverByMatchingTimeSlot(List<ServiceSlot> serviceSlotList) {

        QCaregiverTime caregiverTime = QCaregiverTime.caregiverTime;
        QCaregiver caregiver = QCaregiver.caregiver;
        NumberExpression<Integer> totalOverlapExpr = Expressions.numberTemplate(Integer.class, "0");

        // 모든 요일에 대해서 합산해 더하기
        for(ServiceSlot slot : serviceSlotList) {

            DayOfWeek reqDay = slot.getServiceSlotDay();
            LocalTime reqStartTime = slot.getServiceSlotStart();
            LocalTime reqEndTime = slot.getServiceSlotEnd();

            StringTemplate greatestExpr = Expressions.stringTemplate(
                    "GREATEST({0}, {1})", caregiverTime.startTime, reqStartTime
            );

            // 요청 시간대와 요양사 근무시간 간의 겹치는 종료시간
            StringTemplate leastExpr = Expressions.stringTemplate(
                    "LEAST({0}, {1})", caregiverTime.endTime, reqEndTime
            );

            // TIMESTAMPDIFF(MINUTE, 겹치는 시작시간, 겹치는 종료시간)
            NumberExpression<Integer> timeDiff = Expressions.numberTemplate(
                    Integer.class,
                    "TIMESTAMPDIFF(MINUTE, {0}, {1})",
                    greatestExpr, leastExpr
            );

            NumberExpression<Integer> overlapMinutesExpr = Expressions.numberTemplate(Integer.class,
                    "CASE WHEN {0} = {1} AND {2} > {3} THEN {4} ELSE 0 END",
                    caregiverTime.workDay,
                    slot.getServiceSlotDay(),
                    leastExpr,
                    greatestExpr,
                    timeDiff
            );

            totalOverlapExpr.add(overlapMinutesExpr);
        }

        // 요일 목록
        List<CaregiverWithOverlapDto> dto = jpaQueryFactory
                .select(Projections.constructor(CaregiverWithOverlapDto.class, caregiver, totalOverlapExpr.sum().as("totalOverlap")))
                .from(caregiverTime)
                .join(caregiver).on(caregiverTime.caregiver.eq(caregiver.caregiver))
                .groupBy(caregiver.caregiver)
                .orderBy(totalOverlapExpr.sum().desc())
                .limit(10)
                .fetch();

        return dto;
    }

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
