package com.five.Maeum_Eum.dto.user.elder.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElderInfoDTO {

    private Integer rank;
    private String gender;
    private String address;
    private List<String> meal;
    private List<String> toileting;
    private List<String> mobility;
    private List<String> daily;
    private String family;
    private String pet;

}
