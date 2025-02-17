package com.five.Maeum_Eum.entity.user.caregiver;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCaregiver is a Querydsl query type for Caregiver
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCaregiver extends EntityPathBase<Caregiver> {

    private static final long serialVersionUID = -113705884L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCaregiver caregiver = new QCaregiver("caregiver");

    public final StringPath address = createString("address");

    public final NumberPath<Long> caregiverId = createNumber("caregiverId", Long.class);

    public final ListPath<WorkExperience, QWorkExperience> experience = this.<WorkExperience, QWorkExperience>createList("experience", WorkExperience.class, QWorkExperience.class, PathInits.DIRECT2);

    public final BooleanPath hasCaregiverCertificate = createBoolean("hasCaregiverCertificate");

    public final StringPath introduction = createString("introduction");

    public final BooleanPath isJobOpen = createBoolean("isJobOpen");

    public final BooleanPath isResumeRegistered = createBoolean("isResumeRegistered");

    public final EnumPath<Caregiver.JobState> jobState = createEnum("jobState", Caregiver.JobState.class);

    public final ComparablePath<org.locationtech.jts.geom.Point> location = createComparable("location", org.locationtech.jts.geom.Point.class);

    public final StringPath loginId = createString("loginId");

    public final ListPath<com.five.Maeum_Eum.entity.user.manager.ManagerBookmark, com.five.Maeum_Eum.entity.user.manager.QManagerBookmark> managerBookmarks = this.<com.five.Maeum_Eum.entity.user.manager.ManagerBookmark, com.five.Maeum_Eum.entity.user.manager.QManagerBookmark>createList("managerBookmarks", com.five.Maeum_Eum.entity.user.manager.ManagerBookmark.class, com.five.Maeum_Eum.entity.user.manager.QManagerBookmark.class, PathInits.DIRECT2);

    public final ListPath<com.five.Maeum_Eum.entity.user.manager.ManagerContact, com.five.Maeum_Eum.entity.user.manager.QManagerContact> managerContact = this.<com.five.Maeum_Eum.entity.user.manager.ManagerContact, com.five.Maeum_Eum.entity.user.manager.QManagerContact>createList("managerContact", com.five.Maeum_Eum.entity.user.manager.ManagerContact.class, com.five.Maeum_Eum.entity.user.manager.QManagerContact.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final QResume resume;

    public final ListPath<com.five.Maeum_Eum.entity.user.elder.SavedElders, com.five.Maeum_Eum.entity.user.elder.QSavedElders> savedElders = this.<com.five.Maeum_Eum.entity.user.elder.SavedElders, com.five.Maeum_Eum.entity.user.elder.QSavedElders>createList("savedElders", com.five.Maeum_Eum.entity.user.elder.SavedElders.class, com.five.Maeum_Eum.entity.user.elder.QSavedElders.class, PathInits.DIRECT2);

    public final ListPath<com.five.Maeum_Eum.entity.CaregiverTime, com.five.Maeum_Eum.entity.QCaregiverTime> workTimeSlot = this.<com.five.Maeum_Eum.entity.CaregiverTime, com.five.Maeum_Eum.entity.QCaregiverTime>createList("workTimeSlot", com.five.Maeum_Eum.entity.CaregiverTime.class, com.five.Maeum_Eum.entity.QCaregiverTime.class, PathInits.DIRECT2);

    public QCaregiver(String variable) {
        this(Caregiver.class, forVariable(variable), INITS);
    }

    public QCaregiver(Path<? extends Caregiver> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCaregiver(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCaregiver(PathMetadata metadata, PathInits inits) {
        this(Caregiver.class, metadata, inits);
    }

    public QCaregiver(Class<? extends Caregiver> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.resume = inits.isInitialized("resume") ? new QResume(forProperty("resume"), inits.get("resume")) : null;
    }

}

