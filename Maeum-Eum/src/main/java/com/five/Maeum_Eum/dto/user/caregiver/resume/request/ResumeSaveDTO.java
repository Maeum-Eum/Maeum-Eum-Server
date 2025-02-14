package com.five.Maeum_Eum.dto.user.caregiver.resume.request;


import com.five.Maeum_Eum.entity.user.caregiver.Resume;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter

public class ResumeSaveDTO {

    // 자격증 코드
    @Pattern(regexp = "^[0-9]{4}-[0-9]{7}$", message = "자격증 번호는 2025-1234567 형식이어야 합니다.")
    private String certificateCode;

    // 치매교육 이수 상태
    @NotNull
    private Resume.DemantiaTraining hasDementiaTraining; // "COMPLETE", "NOT_COMPLETE", "UNKNOWN"

    @NotNull
    private Boolean hasVehicle;
    private List<String> workPlace;
    private List<String> workDay; // ["월", "화", "수", "목"]
    private List<String> workTimeSlot; // ["MORNING", "AFTERNOON"]
    private Boolean isNegotiableTime;

    @Min(value = 13000)
    @Max(value = 100000)
    private Integer wage;

    @NotNull
    private List<Integer> elderRank;

    @NotNull
    private List<String> meal;

    @NotNull
    private List<String> toileting;

    @NotNull
    private List<String> mobility;

    @NotNull
    private List<String> daily;

    @NotNull
    private Resume.PreferredGender preferredGender;

    @NotNull
    private Boolean isFamilyPreferred;

    @NotNull
    private List<ExperienceDTO> experience;

    private String introduction;

    private String profileImage;
}
