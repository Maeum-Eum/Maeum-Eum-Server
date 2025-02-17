package com.five.Maeum_Eum.entity.user.elder;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QElder is a Querydsl query type for Elder
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QElder extends EntityPathBase<Elder> {

    private static final long serialVersionUID = -77979176L;

    public static final QElder elder = new QElder("elder");

    public final ListPath<String, StringPath> daily = this.<String, StringPath>createList("daily", String.class, StringPath.class, PathInits.DIRECT2);

    public final EnumPath<ElderFamily> elder_family = createEnum("elder_family", ElderFamily.class);

    public final BooleanPath elder_pet = createBoolean("elder_pet");

    public final StringPath elderAddress = createString("elderAddress");

    public final DatePath<java.time.LocalDate> elderBirth = createDate("elderBirth", java.time.LocalDate.class);

    public final NumberPath<Long> elderId = createNumber("elderId", Long.class);

    public final StringPath elderName = createString("elderName");

    public final NumberPath<Integer> elderRank = createNumber("elderRank", Integer.class);

    public final StringPath gender = createString("gender");

    public final ComparablePath<org.locationtech.jts.geom.Point> location = createComparable("location", org.locationtech.jts.geom.Point.class);

    public final ListPath<com.five.Maeum_Eum.entity.user.manager.ManagerBookmark, com.five.Maeum_Eum.entity.user.manager.QManagerBookmark> managerBookmarks = this.<com.five.Maeum_Eum.entity.user.manager.ManagerBookmark, com.five.Maeum_Eum.entity.user.manager.QManagerBookmark>createList("managerBookmarks", com.five.Maeum_Eum.entity.user.manager.ManagerBookmark.class, com.five.Maeum_Eum.entity.user.manager.QManagerBookmark.class, PathInits.DIRECT2);

    public final ListPath<com.five.Maeum_Eum.entity.user.manager.ManagerContact, com.five.Maeum_Eum.entity.user.manager.QManagerContact> managerContacts = this.<com.five.Maeum_Eum.entity.user.manager.ManagerContact, com.five.Maeum_Eum.entity.user.manager.QManagerContact>createList("managerContacts", com.five.Maeum_Eum.entity.user.manager.ManagerContact.class, com.five.Maeum_Eum.entity.user.manager.QManagerContact.class, PathInits.DIRECT2);

    public final ListPath<String, StringPath> meal = this.<String, StringPath>createList("meal", String.class, StringPath.class, PathInits.DIRECT2);

    public final ListPath<String, StringPath> mobility = this.<String, StringPath>createList("mobility", String.class, StringPath.class, PathInits.DIRECT2);

    public final BooleanPath negotiable = createBoolean("negotiable");

    public final ListPath<String, StringPath> toileting = this.<String, StringPath>createList("toileting", String.class, StringPath.class, PathInits.DIRECT2);

    public QElder(String variable) {
        super(Elder.class, forVariable(variable));
    }

    public QElder(Path<? extends Elder> path) {
        super(path.getType(), path.getMetadata());
    }

    public QElder(PathMetadata metadata) {
        super(Elder.class, metadata);
    }

}

