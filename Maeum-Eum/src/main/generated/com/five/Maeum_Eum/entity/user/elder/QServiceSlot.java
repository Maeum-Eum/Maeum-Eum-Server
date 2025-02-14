package com.five.Maeum_Eum.entity.user.elder;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QServiceSlot is a Querydsl query type for ServiceSlot
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QServiceSlot extends EntityPathBase<ServiceSlot> {

    private static final long serialVersionUID = 712994721L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QServiceSlot serviceSlot = new QServiceSlot("serviceSlot");

    public final QElder elder;

    public final NumberPath<Integer> serviceSlotDay = createNumber("serviceSlotDay", Integer.class);

    public final TimePath<java.time.LocalTime> serviceSlotEnd = createTime("serviceSlotEnd", java.time.LocalTime.class);

    public final NumberPath<Long> serviceSlotId = createNumber("serviceSlotId", Long.class);

    public final TimePath<java.time.LocalTime> serviceSlotStart = createTime("serviceSlotStart", java.time.LocalTime.class);

    public QServiceSlot(String variable) {
        this(ServiceSlot.class, forVariable(variable), INITS);
    }

    public QServiceSlot(Path<? extends ServiceSlot> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QServiceSlot(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QServiceSlot(PathMetadata metadata, PathInits inits) {
        this(ServiceSlot.class, metadata, inits);
    }

    public QServiceSlot(Class<? extends ServiceSlot> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.elder = inits.isInitialized("elder") ? new QElder(forProperty("elder")) : null;
    }

}

