package com.five.Maeum_Eum.entity.user.caregiver;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCertificate is a Querydsl query type for Certificate
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QCertificate extends BeanPath<Certificate> {

    private static final long serialVersionUID = -633832981L;

    public static final QCertificate certificate = new QCertificate("certificate");

    public final StringPath certificateCode = createString("certificateCode");

    public final NumberPath<Integer> certificateRank = createNumber("certificateRank", Integer.class);

    public final EnumPath<Certificate.CertificateType> certificateType = createEnum("certificateType", Certificate.CertificateType.class);

    public QCertificate(String variable) {
        super(Certificate.class, forVariable(variable));
    }

    public QCertificate(Path<? extends Certificate> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCertificate(PathMetadata metadata) {
        super(Certificate.class, metadata);
    }

}

