package com.five.Maeum_Eum.entity.center;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCenter is a Querydsl query type for Center
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCenter extends EntityPathBase<Center> {

    private static final long serialVersionUID = -605139495L;

    public static final QCenter center = new QCenter("center");

    public final StringPath address = createString("address");

    public final StringPath centerCode = createString("centerCode");

    public final NumberPath<Long> centerId = createNumber("centerId", Long.class);

    public final StringPath centerName = createString("centerName");

    public final DatePath<java.time.LocalDate> designatedTime = createDate("designatedTime", java.time.LocalDate.class);

    public final StringPath detailAddress = createString("detailAddress");

    public final StringPath finalGrade = createString("finalGrade");

    public final BooleanPath hasCar = createBoolean("hasCar");

    public final DatePath<java.time.LocalDate> installationTime = createDate("installationTime", java.time.LocalDate.class);

    public final ComparablePath<org.locationtech.jts.geom.Point> location = createComparable("location", org.locationtech.jts.geom.Point.class);

    public final ListPath<com.five.Maeum_Eum.entity.user.manager.Manager, com.five.Maeum_Eum.entity.user.manager.QManager> managers = this.<com.five.Maeum_Eum.entity.user.manager.Manager, com.five.Maeum_Eum.entity.user.manager.QManager>createList("managers", com.five.Maeum_Eum.entity.user.manager.Manager.class, com.five.Maeum_Eum.entity.user.manager.QManager.class, PathInits.DIRECT2);

    public final StringPath oneLineIntro = createString("oneLineIntro");

    public final StringPath zipCode = createString("zipCode");

    public QCenter(String variable) {
        super(Center.class, forVariable(variable));
    }

    public QCenter(Path<? extends Center> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCenter(PathMetadata metadata) {
        super(Center.class, metadata);
    }

}

