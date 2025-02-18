package com.five.Maeum_Eum.dto.user.elder.response;

import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class QueryDslElderDTO {
    private Elder elder;
    private Boolean tmp;
    private Boolean bookmarked;

    @QueryProjection
    public QueryDslElderDTO(Elder elder, Boolean tmp, Boolean bookmarked) {
        this.elder = elder;
        this.tmp = tmp;
        this.bookmarked = bookmarked;
    }
}
