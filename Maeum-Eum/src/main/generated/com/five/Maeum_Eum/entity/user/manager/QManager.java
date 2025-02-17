package com.five.Maeum_Eum.entity.user.manager;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QManager is a Querydsl query type for Manager
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QManager extends EntityPathBase<Manager> {

    private static final long serialVersionUID = 500539934L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QManager manager = new QManager("manager");

    public final com.five.Maeum_Eum.entity.center.QCenter center;

    public final StringPath loginId = createString("loginId");

    public final ListPath<ManagerContact, QManagerContact> managerContacts = this.<ManagerContact, QManagerContact>createList("managerContacts", ManagerContact.class, QManagerContact.class, PathInits.DIRECT2);

    public final NumberPath<Long> managerId = createNumber("managerId", Long.class);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public QManager(String variable) {
        this(Manager.class, forVariable(variable), INITS);
    }

    public QManager(Path<? extends Manager> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QManager(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QManager(PathMetadata metadata, PathInits inits) {
        this(Manager.class, metadata, inits);
    }

    public QManager(Class<? extends Manager> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.center = inits.isInitialized("center") ? new com.five.Maeum_Eum.entity.center.QCenter(forProperty("center")) : null;
    }

}

