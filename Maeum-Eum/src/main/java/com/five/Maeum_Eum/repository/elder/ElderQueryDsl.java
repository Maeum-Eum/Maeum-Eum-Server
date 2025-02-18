package com.five.Maeum_Eum.repository.elder;

import com.five.Maeum_Eum.dto.user.caregiver.main.response.QSimpleContactDTO;
import com.five.Maeum_Eum.dto.user.caregiver.main.response.SimpleContactDTO;
import com.five.Maeum_Eum.dto.user.elder.response.NearElderDTO;
import com.five.Maeum_Eum.dto.user.elder.response.QNearElderDTO;
import com.five.Maeum_Eum.dto.user.elder.response.QQueryDslElderDTO;
import com.five.Maeum_Eum.dto.user.elder.response.QueryDslElderDTO;
import com.five.Maeum_Eum.entity.center.QCenter;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.caregiver.QApply;
import com.five.Maeum_Eum.entity.user.caregiver.QCaregiver;
import com.five.Maeum_Eum.entity.user.caregiver.QResume;
import com.five.Maeum_Eum.entity.user.elder.QElder;
import com.five.Maeum_Eum.entity.user.elder.QSavedElders;
import com.five.Maeum_Eum.entity.user.manager.ApprovalStatus;
import com.five.Maeum_Eum.entity.user.manager.QManager;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ElderQueryDsl {

    private final JPAQueryFactory jpaQueryFactory;

    public NearElderDTO findElder(String pointWKT,
                                     double distanceValue,
                                     Caregiver caregiver,
                                      int num) {
        QApply apply = QApply.apply;
        QManager manager = QManager.manager;
        QCenter center = QCenter.center;
        QCaregiver qCaregiver = QCaregiver.caregiver;
        QResume resume = QResume.resume;
        QElder elder = QElder.elder;

        BooleanExpression meal = resume.caregiver.eq(caregiver)
                .and(resume.mealLevel.goe(QElder.elder.mealLevel));
        BooleanExpression toileting = resume.caregiver.eq(caregiver)
                .and(resume.mealLevel.goe(QElder.elder.toiletingLevel));
        BooleanExpression mobility = resume.caregiver.eq(caregiver)
                .and(resume.mealLevel.goe(QElder.elder.mobilityLevel));
        BooleanExpression daily = resume.caregiver.eq(caregiver)
                .and(resume.mealLevel.goe(QElder.elder.dailyLevel));

        BooleanExpression tmp = meal;
        if (num == 1) {
            tmp = toileting;
        }
        else if (num == 2) {
            tmp = mobility;
        }

        else if (num == 3) {
            tmp = daily;
        }


        BooleanExpression bookmarked = caregiver != null ? JPAExpressions
                .selectOne()
                .from(QSavedElders.savedElders)
                .join(QSavedElders.savedElders.caregiver, qCaregiver)
                .join(QSavedElders.savedElders.elder, QElder.elder)
                .where(QSavedElders.savedElders.elder.eq(apply.elder), QSavedElders.savedElders.caregiver.eq(caregiver))
                .exists() : Expressions.FALSE;

        NumberExpression<Double> distanceExpr = Expressions.numberTemplate(
                Double.class,
                "ST_Distance_Sphere({0}, ST_GeomFromText({1}, 4326))",
                center.location,
                pointWKT
        );

        // n km 이내 조건
        BooleanExpression withinDistance = distanceExpr.loe(distanceValue);

        return jpaQueryFactory
                .select(new QNearElderDTO(
                        elder.manager.center.centerName,
                        elder.wage,
                        elder.negotiable,
                        bookmarked,
                        meal,
                        toileting,
                        mobility,
                        daily,
                        elder
                ))
                .from(elder)
                .join(elder.manager, manager)
                .join(elder.manager.center, center)
                .where(tmp.isTrue(), withinDistance)
                .orderBy(elder.elderId.desc())
                .fetchFirst();
    }


    public NearElderDTO findWageElder(String pointWKT,
                                      double distanceValue,
                                      Caregiver caregiver) {
        QApply apply = QApply.apply;
        QManager manager = QManager.manager;
        QCenter center = QCenter.center;
        QCaregiver qCaregiver = QCaregiver.caregiver;
        QResume resume = QResume.resume;
        QElder elder = QElder.elder;

        BooleanExpression meal = resume.caregiver.eq(caregiver)
                .and(resume.mealLevel.goe(QElder.elder.mealLevel));
        BooleanExpression toileting = resume.caregiver.eq(caregiver)
                .and(resume.mealLevel.goe(QElder.elder.toiletingLevel));
        BooleanExpression mobility = resume.caregiver.eq(caregiver)
                .and(resume.mealLevel.goe(QElder.elder.mobilityLevel));
        BooleanExpression daily = resume.caregiver.eq(caregiver)
                .and(resume.mealLevel.goe(QElder.elder.dailyLevel));


        BooleanExpression bookmarked = caregiver != null ? JPAExpressions
                .selectOne()
                .from(QSavedElders.savedElders)
                .join(QSavedElders.savedElders.caregiver, qCaregiver)
                .join(QSavedElders.savedElders.elder, QElder.elder)
                .where(QSavedElders.savedElders.elder.eq(apply.elder), QSavedElders.savedElders.caregiver.eq(caregiver))
                .exists() : Expressions.FALSE;

        NumberExpression<Double> distanceExpr = Expressions.numberTemplate(
                Double.class,
                "ST_Distance_Sphere({0}, ST_GeomFromText({1}, 4326))",
                center.location,
                pointWKT
        );

        // n km 이내 조건
        BooleanExpression withinDistance = distanceExpr.loe(distanceValue);

        return jpaQueryFactory
                .select(new QNearElderDTO(
                        elder.manager.center.centerName,
                        elder.wage,
                        elder.negotiable,
                        bookmarked,
                        meal,
                        toileting,
                        mobility,
                        daily,
                        elder
                ))
                .from(elder)
                .join(elder.manager, manager)
                .join(elder.manager.center, center)
                .where(withinDistance)
                .orderBy(elder.wage.desc(), elder.elderId.desc())
                .fetchFirst();
    }
}
