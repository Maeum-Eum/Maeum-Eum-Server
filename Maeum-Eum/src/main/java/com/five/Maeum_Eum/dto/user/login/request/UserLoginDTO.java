package com.five.Maeum_Eum.dto.user.login.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLoginDTO {
    private String id;
    private String password;
}
