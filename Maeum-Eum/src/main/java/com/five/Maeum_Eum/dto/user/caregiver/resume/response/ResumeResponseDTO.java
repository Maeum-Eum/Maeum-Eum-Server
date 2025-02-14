package com.five.Maeum_Eum.dto.user.caregiver.resume.response;

import com.five.Maeum_Eum.dto.user.caregiver.resume.request.ExperienceDTO;
import com.five.Maeum_Eum.entity.user.caregiver.Resume;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class ResumeResponseDTO {
    private List<String> jobPosition;
    private String certificateCode;
    private Resume.DemantiaTraining hasDementiaTraining;
    private Boolean hasVehicle;
    private List<String> workPlace;
    private List<String> workDay;
    private List<String> workTimeSlot;
    private Boolean isNegotiableTime;
    private Integer wage;
    private Integer expectedSalary;
    private List<Long> elderRank;
    private List<String> meal;
    private List<String> toileting;
    private List<String> mobility;
    private List<String> daily;
    private Resume.PreferredGender preferredGender;
    private Boolean isFamilyPreferred;
    private List<ExperienceDTO> experience;
    private String introduction;
    private String profileImage;
}