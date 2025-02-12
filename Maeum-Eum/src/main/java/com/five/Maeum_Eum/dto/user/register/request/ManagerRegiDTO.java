package com.five.Maeum_Eum.dto.user.register.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerRegiDTO {
    @NotBlank(message = "ID is blank")
    @Size(min = 1, max = 32, message = "아이디는 최대 32자까지 가능합니다")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "invalid ID")
    private String id;

    @NotBlank(message = "PW is blank")
    @Size(min = 10, max = 100, message = "PW must be at least 10")
    @Pattern(regexp = "^(?=(.*[A-Za-z].*[0-9])|(?=.*[A-Za-z].*[@$!%*?&])|(?=.*[0-9].*[@$!%*?&])).{10,}$", message = "invalid PW")
    private String password;

    @NotBlank(message = "name is blank")
    @Size(min = 1, max = 32, message = "name can not be more than 32")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣@$!%*?&]{1,32}$", message = "Number, Korean, Alphabet are only available")
    private String name;

    @NotBlank(message = "email is blank")
    @Email(message = "invalid email")
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

    private Boolean hasCar;
}
