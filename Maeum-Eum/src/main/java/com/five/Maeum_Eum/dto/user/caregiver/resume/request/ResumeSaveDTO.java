package com.five.Maeum_Eum.dto.user.caregiver.resume.request;


import com.five.Maeum_Eum.dto.user.caregiver.resume.response.ExperienceDTO;
import com.five.Maeum_Eum.entity.user.caregiver.Resume;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ResumeSaveDTO {

    @NotNull
    private List<String> jobPosition;
    @Pattern(regexp = "^[0-9]{4}-[0-9]{7}$", message = "자격증 번호는 2025-1234567 형식이어야 합니다.")
    @NotBlank
    private String certificateCode;
    @NotNull
    private Resume.DemantiaTraining hasDementiaTraining;
    @NotNull
    private Boolean hasVehicle;
    @NotNull
    private List<String> workPlace;
    @NotNull
    private List<String> workDay;
    @NotNull
    private List<String> workTimeSlot;
    @NotNull
    private Boolean isNegotiableTime;
    @Min(value = 13000)
    @Max(value = 30000)
    @NotNull
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
    private Boolean isPetPreferred;
    @NotNull
    private List<ExperienceDTO> experience;
    @NotBlank
    private String introduction;
}
