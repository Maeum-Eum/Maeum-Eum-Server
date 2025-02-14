package com.five.Maeum_Eum.entity.user.caregiver;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Certificate {

    private CertificateType certificateType;

    private int certificateRank;

    private String certificateCode;

    public enum CertificateType {
        CARE_GIVER, SOCIAL_WORKER, NURSING_ASSISTANT
    }
}