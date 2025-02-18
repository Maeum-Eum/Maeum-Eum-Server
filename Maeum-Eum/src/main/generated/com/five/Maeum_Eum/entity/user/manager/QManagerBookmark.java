package com.five.Maeum_Eum.entity.user.manager;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QManagerBookmark is a Querydsl query type for ManagerBookmark
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QManagerBookmark extends EntityPathBase<ManagerBookmark> {

    private static final long serialVersionUID = -1569018508L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QManagerBookmark managerBookmark = new QManagerBookmark("managerBookmark");

    public final com.five.Maeum_Eum.common.QBaseTimeEntity _super = new com.five.Maeum_Eum.common.QBaseTimeEntity(this);

    public final NumberPath<Long> bookmarkId = createNumber("bookmarkId", Long.class);

    public final com.five.Maeum_Eum.entity.user.caregiver.QCaregiver caregiver;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.five.Maeum_Eum.entity.user.elder.QElder elder;

    public final QManager manager;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QManagerBookmark(String variable) {
        this(ManagerBookmark.class, forVariable(variable), INITS);
    }

    public QManagerBookmark(Path<? extends ManagerBookmark> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QManagerBookmark(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QManagerBookmark(PathMetadata metadata, PathInits inits) {
        this(ManagerBookmark.class, metadata, inits);
    }

    public QManagerBookmark(Class<? extends ManagerBookmark> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.caregiver = inits.isInitialized("caregiver") ? new com.five.Maeum_Eum.entity.user.caregiver.QCaregiver(forProperty("caregiver"), inits.get("caregiver")) : null;
        this.elder = inits.isInitialized("elder") ? new com.five.Maeum_Eum.entity.user.elder.QElder(forProperty("elder"), inits.get("elder")) : null;
        this.manager = inits.isInitialized("manager") ? new QManager(forProperty("manager"), inits.get("manager")) : null;
    }

}

