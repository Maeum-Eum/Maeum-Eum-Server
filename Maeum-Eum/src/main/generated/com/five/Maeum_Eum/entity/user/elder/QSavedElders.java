package com.five.Maeum_Eum.entity.user.elder;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSavedElders is a Querydsl query type for SavedElders
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSavedElders extends EntityPathBase<SavedElders> {

    private static final long serialVersionUID = -1993267618L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSavedElders savedElders = new QSavedElders("savedElders");

    public final com.five.Maeum_Eum.entity.user.caregiver.QCaregiver caregiver;

    public final QElder elder;

    public final NumberPath<Long> savedEldersId = createNumber("savedEldersId", Long.class);

    public QSavedElders(String variable) {
        this(SavedElders.class, forVariable(variable), INITS);
    }

    public QSavedElders(Path<? extends SavedElders> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSavedElders(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSavedElders(PathMetadata metadata, PathInits inits) {
        this(SavedElders.class, metadata, inits);
    }

    public QSavedElders(Class<? extends SavedElders> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.caregiver = inits.isInitialized("caregiver") ? new com.five.Maeum_Eum.entity.user.caregiver.QCaregiver(forProperty("caregiver"), inits.get("caregiver")) : null;
        this.elder = inits.isInitialized("elder") ? new QElder(forProperty("elder")) : null;
    }

}

