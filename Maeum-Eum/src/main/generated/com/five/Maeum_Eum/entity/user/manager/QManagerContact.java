package com.five.Maeum_Eum.entity.user.manager;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QManagerContact is a Querydsl query type for ManagerContact
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QManagerContact extends EntityPathBase<ManagerContact> {

    private static final long serialVersionUID = -549250014L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QManagerContact managerContact = new QManagerContact("managerContact");

    public final com.five.Maeum_Eum.common.QBaseTimeEntity _super = new com.five.Maeum_Eum.common.QBaseTimeEntity(this);

    public final EnumPath<ApprovalStatus> approvalStatus = createEnum("approvalStatus", ApprovalStatus.class);

    public final com.five.Maeum_Eum.entity.user.caregiver.QCaregiver caregiver;

    public final StringPath caregiverPhoneNumber = createString("caregiverPhoneNumber");

    public final NumberPath<Long> contactId = createNumber("contactId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.five.Maeum_Eum.entity.user.elder.QElder elder;

    public final QManager manager;

    public final StringPath managerPhoneNumber = createString("managerPhoneNumber");

    public final StringPath messageFromCaregiver = createString("messageFromCaregiver");

    public final StringPath messageFromManager = createString("messageFromManager");

    public final BooleanPath negotiable = createBoolean("negotiable");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Integer> wage = createNumber("wage", Integer.class);

    public final StringPath workRequirement = createString("workRequirement");

    public QManagerContact(String variable) {
        this(ManagerContact.class, forVariable(variable), INITS);
    }

    public QManagerContact(Path<? extends ManagerContact> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QManagerContact(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QManagerContact(PathMetadata metadata, PathInits inits) {
        this(ManagerContact.class, metadata, inits);
    }

    public QManagerContact(Class<? extends ManagerContact> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.caregiver = inits.isInitialized("caregiver") ? new com.five.Maeum_Eum.entity.user.caregiver.QCaregiver(forProperty("caregiver"), inits.get("caregiver")) : null;
        this.elder = inits.isInitialized("elder") ? new com.five.Maeum_Eum.entity.user.elder.QElder(forProperty("elder"), inits.get("elder")) : null;
        this.manager = inits.isInitialized("manager") ? new QManager(forProperty("manager"), inits.get("manager")) : null;
    }

}

