package com.five.Maeum_Eum.dto.user.register.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ManagerRegiDTO {
    @NotBlank
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String id;

    @NotBlank
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^(?=(.*[A-Za-z].*[0-9])|(?=.*[A-Za-z].*[@$!%*?&])|(?=.*[0-9].*[@$!%*?&])).{10,}$")
    private String password;

    @NotBlank
    @Size(min = 1, max = 32)
    @Pattern(regexp = "^[a-zA-Z0-9가-힣@$!%*?&]{1,32}$")
    private String name;

    @NotBlank
    @Pattern(regexp = "^(010-?\\d{4}-?\\d{4})$")
    private String phone;

    @NotBlank
    private String address;

    @NotNull
    private Boolean hasCar;

    @NotNull
    private Long centerId;
}
