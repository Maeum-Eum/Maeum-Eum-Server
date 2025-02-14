package com.five.Maeum_Eum.entity.user.caregiver;

import com.five.Maeum_Eum.common.BaseTimeEntity;
import com.five.Maeum_Eum.converter.GenericListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false)
    private boolean hasVehicle;

    @Enumerated(EnumType.STRING)
    private DemantiaTraining hasDementiaTraining;

    @Column(nullable = false)
    private int wage;

    @Column(nullable = false)
    @Convert(converter = GenericListConverter.class)
    private List<String> workTimeSlot;

    @Column(nullable = false)
    private boolean negotiableTime;

    @Column(nullable = false)
    private boolean petPreferred;

    @Column(nullable = false)
    private boolean familyPreferred;

    private String introduction;

    private String profileImage;

    @Embedded
    private Certificate certificate;

    @Column(nullable = false)
    @Convert(converter = GenericListConverter.class)
    private List<String> workDay;

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

    @Column(nullable = false)
    @Convert(converter = GenericListConverter.class)
    private List<Long> elderRank;

    @Enumerated(EnumType.STRING)
    private PreferredGender preferredGender;

    public enum PreferredGender {
        EVERY, MALE, FEMALE
    }

    public enum DemantiaTraining {
        UNKNOWN, COMPLETE, NOT_COMPLETE
    }
}