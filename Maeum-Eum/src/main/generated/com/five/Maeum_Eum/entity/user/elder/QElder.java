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

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QElder elder = new QElder("elder");

    public final ListPath<String, StringPath> daily = this.<String, StringPath>createList("daily", String.class, StringPath.class, PathInits.DIRECT2);

    public final BooleanPath dailyFilter1 = createBoolean("dailyFilter1");

    public final BooleanPath dailyFilter2 = createBoolean("dailyFilter2");

    public final BooleanPath dailyFilter3 = createBoolean("dailyFilter3");

    public final BooleanPath dailyFilter4 = createBoolean("dailyFilter4");

    public final BooleanPath dailyFilter5 = createBoolean("dailyFilter5");

    public final BooleanPath dailyFilter6 = createBoolean("dailyFilter6");

    public final NumberPath<Integer> dailyLevel = createNumber("dailyLevel", Integer.class);

    public final EnumPath<ElderFamily> elder_family = createEnum("elder_family", ElderFamily.class);

    public final BooleanPath elder_pet = createBoolean("elder_pet");

    public final StringPath elderAddress = createString("elderAddress");

    public final DatePath<java.time.LocalDate> elderBirth = createDate("elderBirth", java.time.LocalDate.class);

    public final NumberPath<Long> elderId = createNumber("elderId", Long.class);

    public final StringPath elderName = createString("elderName");

    public final NumberPath<Integer> elderRank = createNumber("elderRank", Integer.class);

    public final StringPath gender = createString("gender");

    public final ComparablePath<org.locationtech.jts.geom.Point> location = createComparable("location", org.locationtech.jts.geom.Point.class);

    public final com.five.Maeum_Eum.entity.user.manager.QManager manager;

    public final ListPath<com.five.Maeum_Eum.entity.user.manager.ManagerBookmark, com.five.Maeum_Eum.entity.user.manager.QManagerBookmark> managerBookmarks = this.<com.five.Maeum_Eum.entity.user.manager.ManagerBookmark, com.five.Maeum_Eum.entity.user.manager.QManagerBookmark>createList("managerBookmarks", com.five.Maeum_Eum.entity.user.manager.ManagerBookmark.class, com.five.Maeum_Eum.entity.user.manager.QManagerBookmark.class, PathInits.DIRECT2);

    public final ListPath<com.five.Maeum_Eum.entity.user.manager.ManagerContact, com.five.Maeum_Eum.entity.user.manager.QManagerContact> managerContacts = this.<com.five.Maeum_Eum.entity.user.manager.ManagerContact, com.five.Maeum_Eum.entity.user.manager.QManagerContact>createList("managerContacts", com.five.Maeum_Eum.entity.user.manager.ManagerContact.class, com.five.Maeum_Eum.entity.user.manager.QManagerContact.class, PathInits.DIRECT2);

    public final ListPath<String, StringPath> meal = this.<String, StringPath>createList("meal", String.class, StringPath.class, PathInits.DIRECT2);

    public final NumberPath<Integer> mealLevel = createNumber("mealLevel", Integer.class);

    public final ListPath<String, StringPath> mobility = this.<String, StringPath>createList("mobility", String.class, StringPath.class, PathInits.DIRECT2);

    public final NumberPath<Integer> mobilityLevel = createNumber("mobilityLevel", Integer.class);

    public final BooleanPath negotiable = createBoolean("negotiable");

    public final ListPath<ServiceSlot, QServiceSlot> serviceSlots = this.<ServiceSlot, QServiceSlot>createList("serviceSlots", ServiceSlot.class, QServiceSlot.class, PathInits.DIRECT2);

    public final ListPath<String, StringPath> toileting = this.<String, StringPath>createList("toileting", String.class, StringPath.class, PathInits.DIRECT2);

    public final NumberPath<Integer> toiletingLevel = createNumber("toiletingLevel", Integer.class);

    public final NumberPath<Integer> wage = createNumber("wage", Integer.class);

    public QElder(String variable) {
        this(Elder.class, forVariable(variable), INITS);
    }

    public QElder(Path<? extends Elder> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QElder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QElder(PathMetadata metadata, PathInits inits) {
        this(Elder.class, metadata, inits);
    }

    public QElder(Class<? extends Elder> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.manager = inits.isInitialized("manager") ? new com.five.Maeum_Eum.entity.user.manager.QManager(forProperty("manager"), inits.get("manager")) : null;
    }

}

