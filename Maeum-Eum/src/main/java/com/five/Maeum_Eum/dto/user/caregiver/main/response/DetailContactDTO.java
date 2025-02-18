package com.five.Maeum_Eum.dto.user.caregiver.main.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.five.Maeum_Eum.dto.center.response.CenterDTO;
import com.five.Maeum_Eum.dto.user.elder.response.ElderInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailContactDTO {
    private Long contactId;
    private Long elderId;
    private CenterDTO center;
    private String message;
    private String title;
    private LocalDateTime createdAt;
    private Integer wage;
    private Boolean negotiable;
    private Boolean bookmarked;
    private ElderInfoDTO elder;
    private Boolean meal;
    private Boolean toileting;
    private Boolean mobility;
    private Boolean daily;
}


