package com.five.Maeum_Eum.dto.center.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CenterDTO {
    private Long centerId;
    private String centerName;
    private String address;
    private LocalDate designatedTime;
    private LocalDate installationTime;
    private String detailAddress;
    private String zipCode;
    private String centerCode;
    private String finalGrade;
    private String oneLineIntro;
    private Boolean hasCar;
}
