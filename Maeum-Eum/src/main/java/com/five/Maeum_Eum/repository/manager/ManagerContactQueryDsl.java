package com.five.Maeum_Eum.repository.manager;

import com.five.Maeum_Eum.entity.center.QCenter;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.caregiver.QCaregiver;
import com.five.Maeum_Eum.entity.user.manager.ManagerContact;
import com.five.Maeum_Eum.entity.user.manager.QManager;
import com.five.Maeum_Eum.entity.user.manager.QManagerContact;
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
