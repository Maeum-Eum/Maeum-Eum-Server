package com.five.Maeum_Eum.entity.user.caregiver;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QResume is a Querydsl query type for Resume
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QResume extends EntityPathBase<Resume> {

    private static final long serialVersionUID = 1480567641L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QResume resume = new QResume("resume");

    public final com.five.Maeum_Eum.common.QBaseTimeEntity _super = new com.five.Maeum_Eum.common.QBaseTimeEntity(this);

    public final QCaregiver caregiver;

    public final QCertificate certificate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<String, StringPath> daily = this.<String, StringPath>createList("daily", String.class, StringPath.class, PathInits.DIRECT2);

    public final ListPath<Long, NumberPath<Long>> elderRank = this.<Long, NumberPath<Long>>createList("elderRank", Long.class, NumberPath.class, PathInits.DIRECT2);

    public final BooleanPath familyPreferred = createBoolean("familyPreferred");

    public final EnumPath<Resume.DemantiaTraining> hasDementiaTraining = createEnum("hasDementiaTraining", Resume.DemantiaTraining.class);

    public final BooleanPath hasVehicle = createBoolean("hasVehicle");

    public final StringPath introduction = createString("introduction");

    public final ListPath<String, StringPath> jobPosition = this.<String, StringPath>createList("jobPosition", String.class, StringPath.class, PathInits.DIRECT2);

    public final ListPath<String, StringPath> meal = this.<String, StringPath>createList("meal", String.class, StringPath.class, PathInits.DIRECT2);

    public final ListPath<String, StringPath> mobility = this.<String, StringPath>createList("mobility", String.class, StringPath.class, PathInits.DIRECT2);

    public final BooleanPath negotiableTime = createBoolean("negotiableTime");

    public final BooleanPath petPreferred = createBoolean("petPreferred");

    public final EnumPath<Resume.PreferredGender> preferredGender = createEnum("preferredGender", Resume.PreferredGender.class);

    public final StringPath profileImage = createString("profileImage");

    public final NumberPath<Long> resumeId = createNumber("resumeId", Long.class);

    public final ListPath<String, StringPath> toileting = this.<String, StringPath>createList("toileting", String.class, StringPath.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Integer> wage = createNumber("wage", Integer.class);

    public final ListPath<String, StringPath> workDay = this.<String, StringPath>createList("workDay", String.class, StringPath.class, PathInits.DIRECT2);

    public final ListPath<String, StringPath> workPlace = this.<String, StringPath>createList("workPlace", String.class, StringPath.class, PathInits.DIRECT2);

    public final ListPath<String, StringPath> workTimeSlot = this.<String, StringPath>createList("workTimeSlot", String.class, StringPath.class, PathInits.DIRECT2);

    public QResume(String variable) {
        this(Resume.class, forVariable(variable), INITS);
    }

    public QResume(Path<? extends Resume> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QResume(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QResume(PathMetadata metadata, PathInits inits) {
        this(Resume.class, metadata, inits);
    }

    public QResume(Class<? extends Resume> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.caregiver = inits.isInitialized("caregiver") ? new QCaregiver(forProperty("caregiver"), inits.get("caregiver")) : null;
        this.certificate = inits.isInitialized("certificate") ? new QCertificate(forProperty("certificate")) : null;
    }

}

