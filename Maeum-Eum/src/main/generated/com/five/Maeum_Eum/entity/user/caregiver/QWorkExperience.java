package com.five.Maeum_Eum.entity.user.caregiver;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWorkExperience is a Querydsl query type for WorkExperience
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWorkExperience extends EntityPathBase<WorkExperience> {

    private static final long serialVersionUID = -1133178233L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWorkExperience workExperience = new QWorkExperience("workExperience");

    public final QCaregiver caregiver;

    public final com.five.Maeum_Eum.entity.center.QCenter center;

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Long> experienceId = createNumber("experienceId", Long.class);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final StringPath work = createString("work");

    public QWorkExperience(String variable) {
        this(WorkExperience.class, forVariable(variable), INITS);
    }

    public QWorkExperience(Path<? extends WorkExperience> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWorkExperience(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWorkExperience(PathMetadata metadata, PathInits inits) {
        this(WorkExperience.class, metadata, inits);
    }

    public QWorkExperience(Class<? extends WorkExperience> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.caregiver = inits.isInitialized("caregiver") ? new QCaregiver(forProperty("caregiver"), inits.get("caregiver")) : null;
        this.center = inits.isInitialized("center") ? new com.five.Maeum_Eum.entity.center.QCenter(forProperty("center")) : null;
    }

}

