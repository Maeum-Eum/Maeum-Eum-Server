package com.five.Maeum_Eum.repository.caregiver;

import com.five.Maeum_Eum.dto.user.caregiver.main.response.QSimpleContactDTO;
import com.five.Maeum_Eum.dto.user.caregiver.main.response.SimpleContactDTO;
import com.five.Maeum_Eum.entity.center.QCenter;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.caregiver.QApply;
import com.five.Maeum_Eum.entity.user.caregiver.QCaregiver;
import com.five.Maeum_Eum.entity.user.caregiver.QResume;
import com.five.Maeum_Eum.entity.user.elder.QElder;
import com.five.Maeum_Eum.entity.user.elder.QSavedElders;
import com.five.Maeum_Eum.entity.user.manager.ApprovalStatus;
import com.five.Maeum_Eum.entity.user.manager.QManager;
import com.five.Maeum_Eum.entity.user.manager.QManagerContact;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
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
public class ApplyQueryDsl {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<SimpleContactDTO> findMyApplies(Caregiver caregiver, Pageable pageable, ApprovalStatus approvalStatus) {
        QApply apply = QApply.apply;
        QManager manager = QManager.manager;
        QCenter center = QCenter.center;
        QCaregiver qCaregiver = QCaregiver.caregiver;
        QResume resume = QResume.resume;

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

        JPAQuery<SimpleContactDTO> query = jpaQueryFactory
                .select(new QSimpleContactDTO(
                        apply.applyId,
                        center.centerName,
                        apply.elder,
                        apply.createdAt,
                        apply.elder.wage,
                        apply.elder.negotiable,
                        bookmarked,
                        meal,
                        toileting,
                        mobility,
                        daily
                ))
                .from(apply)
                .join(apply.elder.manager, manager)
                .join(manager.center, center)
                .join(apply.caregiver, qCaregiver)
                .where(apply.caregiver.eq(caregiver), apply.approvalStatus.eq(approvalStatus))
                .orderBy(apply.applyId.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<SimpleContactDTO> contacts = query.fetch();
        // 전체 건수 조회 (페이징 처리를 위한 count)
        long total = query.fetchCount();

        return new PageImpl<>(contacts, pageable, total);
    }
}
