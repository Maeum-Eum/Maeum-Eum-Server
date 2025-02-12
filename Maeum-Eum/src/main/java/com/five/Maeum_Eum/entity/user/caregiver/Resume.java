package com.five.Maeum_Eum.entity.user.caregiver;

import com.five.Maeum_Eum.converter.StringListConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "resume")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resumeId;

    @OneToOne
    @JoinColumn(nullable = false , name = "caregiver_id")
    private Caregiver caregiver;

    @Column(nullable = false)
    private boolean hasVehicle;

    @Enumerated(EnumType.STRING)
    private DemantiaTraining hasDementiaTraining;

    @Column(nullable = false)
    @Min(value = 13000, message = "최소 시급은 13000원 입니다.")
    private int wage;

    @Column(nullable = false)
    private int workDay;

    @Enumerated(EnumType.STRING)
    private WorkTimeSlot workTimeSlot;

    @Column(nullable = false)
    private boolean isNegotiableTime;

    @Column(nullable = false)
    private boolean isPetPreferred;

    @Column(nullable = false)
    private boolean isFamilyPreferred;

    private String introduction;

    private String profileImage;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = StringListConverter.class)
    private List<String> workPlace;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = StringListConverter.class)
    private List<String> jobPosition;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = StringListConverter.class)
    private List<String> meal;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = StringListConverter.class)
    private List<String> toileting;
    
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = StringListConverter.class)
    private List<String> mobility;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @Convert(converter = StringListConverter.class)
    private List<String> daily;

    @Column(nullable = false)
    private int elderRank;

    @Enumerated(EnumType.STRING)
    private PreferredGender preferredGender;

    public enum WorkTimeSlot {
        MORNING, AFTERNOON, EVENING
    }

    public enum PreferredGender {
        EVERY, MALE, FEMALE
    }

    public enum DemantiaTraining {
        UNKNOWN, COMPLETE, NOT_COMPLETE
    }

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}