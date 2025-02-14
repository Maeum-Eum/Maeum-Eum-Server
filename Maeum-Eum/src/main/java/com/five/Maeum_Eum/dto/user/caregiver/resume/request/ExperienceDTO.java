package com.five.Maeum_Eum.dto.user.caregiver.resume.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter
public class ExperienceDTO {

    // 시작일
    private LocalDate startDate;
    // 종료일
    private LocalDate endDate;
    // 근무지 센터 ID
    private Integer centerId;
    // 직무
    @NotBlank
    private String work;
}