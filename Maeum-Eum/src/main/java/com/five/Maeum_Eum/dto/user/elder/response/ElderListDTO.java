package com.five.Maeum_Eum.dto.user.elder.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ElderListDTO {
    private NearElderDTO meal;
    private NearElderDTO toileting;
    private NearElderDTO mobility;
    private NearElderDTO daily;
    private NearElderDTO wage;
}
