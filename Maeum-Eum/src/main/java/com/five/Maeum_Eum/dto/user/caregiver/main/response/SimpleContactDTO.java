package com.five.Maeum_Eum.dto.user.caregiver.main.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleContactDTO {
    private Long contactId;
    private Long applyId;
    private String center;
    private String title;
    private LocalDateTime createdAt;
    private Integer wage;
    private Boolean negotiable;
    private Boolean bookmarked;
    private Boolean meal;
    private Boolean toileting;
    private Boolean mobility;
    private Boolean daily;
    private Elder elder;
    private String workRequirement;

    @QueryProjection
    public SimpleContactDTO(Long contactId, String center, Elder elder, LocalDateTime createdAt, Integer wage, Boolean negotiable, Boolean bookmarked, Boolean meal, Boolean toileting, Boolean mobility, Boolean daily, String workRequirement) {
        this.contactId = contactId;
        this.center = center;
        this.elder = elder;
        this.createdAt = createdAt;
        this.wage = wage;
        this.negotiable = negotiable;
        this.bookmarked = bookmarked;
        this.meal = meal;
        this.toileting = toileting;
        this.mobility = mobility;
        this.daily = daily;
        this.workRequirement = workRequirement;
    }
}
