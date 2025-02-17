package com.five.Maeum_Eum.dto.user.register.request;

import com.five.Maeum_Eum.dto.user.caregiver.resume.response.ExperienceDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaregiverRegiDTO {
    @NotBlank
    @Size(min = 1, max = 32)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String id;

    @NotBlank
    @Size(min = 10, max = 100)
    @Pattern(regexp = "^(?=(.*[A-Za-z].*[0-9])|(?=.*[A-Za-z].*[@$!%*?&])|(?=.*[0-9].*[@$!%*?&])).{10,}$")
    private String password;

    @NotBlank
    @Size(min = 1, max = 32)
    @Pattern(regexp = "^[a-zA-Z0-9가-힣@$!%*?&]{1,32}$")
    private String name;

    @NotBlank
    private String phone;

    private String address;

    private String introduction;

    private List<ExperienceDTO> experience;

    // 자신 있는 사항 추가 필요
}
