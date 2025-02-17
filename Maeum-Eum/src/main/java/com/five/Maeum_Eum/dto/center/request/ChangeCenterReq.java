package com.five.Maeum_Eum.dto.center.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangeCenterReq {
    Long centerId;
    JsonNullable<Boolean> hasCar;
    JsonNullable<String> oneLineIntro;

    public ChangeCenterReq(Long centerId , JsonNullable<Boolean> hasCar , JsonNullable<String> oneLineIntro){
        this.centerId = centerId;
        this.hasCar = hasCar;
        this.oneLineIntro = oneLineIntro;
    }
}
