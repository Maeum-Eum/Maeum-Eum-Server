package com.five.Maeum_Eum.entity.user.caregiver;

import com.five.Maeum_Eum.common.BaseTimeEntity;
import com.five.Maeum_Eum.controller.work.DailyType;
import com.five.Maeum_Eum.controller.work.MealType;
import com.five.Maeum_Eum.controller.work.MobilityType;
import com.five.Maeum_Eum.controller.work.ToiletingType;
import com.five.Maeum_Eum.converter.GenericListConverter;
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
    private List<Integer> workTimeSlot;

    private Boolean negotiableTime;

    private Boolean petPreferred;

    private Boolean familyPreferred;

    private String introduction;

    @Setter
    private String profileImage = "/images/logo.png";

    @Embedded
    private Certificate certificate;

    @Convert(converter = GenericListConverter.class)
    private List<Integer> workDay;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = GenericListConverter.class)
    private List<String> workPlace;

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
    private int mealLevel = 0;
    private int toiletingLevel = 0;
    private int mobilityLevel = 0;

    private boolean dailyFilter1;
    private boolean dailyFilter2;
    private boolean dailyFilter3;
    private boolean dailyFilter4;
    private boolean dailyFilter5;
    private boolean dailyFilter6;

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

        // 4개의 등급 수준 결정
        this.mealLevel =
                resumeSaveDTO.getMeal().stream()
                        .map(MealType::fromLabel)
                        .mapToInt(MealType::getLevel)
                        .max()
                        .orElse(0);
        this.dailyFilter1 = resumeSaveDTO.getDaily().contains(DailyType.ONE.getLabel());
        this.dailyFilter2 = resumeSaveDTO.getDaily().contains(DailyType.TWO.getLabel());
        this.dailyFilter3 = resumeSaveDTO.getDaily().contains(DailyType.THREE.getLabel());
        this.dailyFilter4 = resumeSaveDTO.getDaily().contains(DailyType.FOUR.getLabel());
        this.dailyFilter5 = resumeSaveDTO.getDaily().contains(DailyType.FIVE.getLabel());
        this.dailyFilter6 = resumeSaveDTO.getDaily().contains(DailyType.SIX.getLabel());
        this.mobilityLevel = resumeSaveDTO.getMobility().stream()
                                .map(MobilityType::fromLabel)
                                .mapToInt(MobilityType::getLevel)
                                .max()
                                .orElse(0);
        this.toiletingLevel = resumeSaveDTO.getToileting().stream()
                                .map(ToiletingType::fromLabel)
                                .mapToInt(ToiletingType::getLevel)
                                .max()
                                .orElse(0);
                // 인지 지원 가능 등급 결정
        this.elderRankLevel = resumeSaveDTO.getElderRank().stream()
                                .mapToInt(Integer::intValue)
                                .max()
                                .orElse(0);
    }
}