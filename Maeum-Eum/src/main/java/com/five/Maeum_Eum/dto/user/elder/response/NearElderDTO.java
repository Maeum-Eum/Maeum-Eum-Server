package com.five.Maeum_Eum.dto.user.elder.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NearElderDTO {
    @Setter
    private Long elderId;
    private String center;
    @Setter
    private String title;
    private Integer wage;
    private Boolean negotiable;
    private Boolean bookmarked;
    private Boolean meal;
    private Boolean toileting;
    private Boolean mobility;
    private Boolean daily;
    @Setter
    private Elder elder;

    @QueryProjection
    public NearElderDTO(String center, Integer wage, Boolean negotiable, Boolean bookmarked, Boolean meal, Boolean toileting, Boolean mobility, Boolean daily, Elder elder) {
        this.center = center;
        this.wage = wage;
        this.negotiable = negotiable;
        this.bookmarked = bookmarked;
        this.meal = meal;
        this.toileting = toileting;
        this.mobility = mobility;
        this.daily = daily;
        this.elder = elder;
    }
}
