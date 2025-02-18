package com.five.Maeum_Eum.entity.user.caregiver;

import com.five.Maeum_Eum.common.BaseTimeEntity;
import com.five.Maeum_Eum.converter.GenericListConverter;
import com.five.Maeum_Eum.converter.WorkPlaceListConverter;
import com.five.Maeum_Eum.dto.user.caregiver.resume.request.ResumeSaveDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "resume")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resume extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resumeId;

    @OneToOne
    @JoinColumn(nullable = false, name = "caregiver_id")
    private Caregiver caregiver;

    private Boolean hasVehicle;

    @Enumerated(EnumType.STRING)
    private DemantiaTraining hasDementiaTraining;

    private int wage;

    @Convert(converter = GenericListConverter.class)
    private List<String> workTimeSlot;

    private Boolean negotiableTime;

    private Boolean petPreferred;

    private Boolean familyPreferred;

    private String introduction;

    @Setter
    private String profileImage = "/images/logo.png";

    @Embedded
    private Certificate certificate;

    @Convert(converter = GenericListConverter.class)
    private List<String> workDay;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = WorkPlaceListConverter.class)
    private List<WorkPlace> workPlace;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = GenericListConverter.class)
    private List<String> jobPosition;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = GenericListConverter.class)
    private List<String> meal;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = GenericListConverter.class)
    private List<String> toileting;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = GenericListConverter.class)
    private List<String> mobility;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = GenericListConverter.class)
    private List<String> daily;

    // 서비스 가능 수준
    private int mealLevel;
    private int toiletingLevel;
    private int mobilityLevel;
    private int dailyLevel;

    @Convert(converter = GenericListConverter.class)
    private List<Integer> elderRank;

    private int elderRankLevel;

    @Enumerated(EnumType.STRING)
    private PreferredGender preferredGender;

    public enum PreferredGender {
        EVERY, MALE, FEMALE
    }

    public enum DemantiaTraining {
        UNKNOWN, COMPLETE, NOT_COMPLETE
    }

    public void updateResume(ResumeSaveDTO resumeSaveDTO) {
        this.jobPosition = resumeSaveDTO.getJobPosition();

        // 자격증
        Certificate dto = Certificate.builder()
                .certificateType(Certificate.CertificateType.CARE_GIVER)
                .certificateRank(1)
                .certificateCode(resumeSaveDTO.getCertificateCode())
                .build();

        this.certificate = dto;

        this.hasDementiaTraining = resumeSaveDTO.getHasDementiaTraining();
        this.hasVehicle = resumeSaveDTO.getHasVehicle();
        this.workPlace = resumeSaveDTO.getWorkPlace();
        this.workDay = resumeSaveDTO.getWorkDay();
        this.workTimeSlot = resumeSaveDTO.getWorkTimeSlot();
        this.negotiableTime = resumeSaveDTO.getIsNegotiableTime();
        this.wage = resumeSaveDTO.getWage();
        this.elderRank = resumeSaveDTO.getElderRank();
        this.meal = resumeSaveDTO.getMeal();
        this.toileting = resumeSaveDTO.getToileting();
        this.mobility = resumeSaveDTO.getMobility();
        this.daily = resumeSaveDTO.getDaily();
        this.preferredGender = resumeSaveDTO.getPreferredGender();
        this.familyPreferred = resumeSaveDTO.getIsFamilyPreferred();
        this.introduction = resumeSaveDTO.getIntroduction();
    }
}