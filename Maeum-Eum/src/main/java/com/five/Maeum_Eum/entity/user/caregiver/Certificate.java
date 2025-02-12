package com.five.Maeum_Eum.entity.user.caregiver;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "certificate")
@Getter
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long certificateId;

    @ManyToOne
    @JoinColumn(name = "caregiver_id", nullable = false)
    private Caregiver caregiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CertificateType certificateType;

    @Column(nullable = false)
    private int certificateRank;

    @Column(nullable = false)
    private String certificateCode;

    public enum CertificateType {
        CARE_GIVER, SOCIAL_WORKER, NURSING_ASSISTANT
    }
}