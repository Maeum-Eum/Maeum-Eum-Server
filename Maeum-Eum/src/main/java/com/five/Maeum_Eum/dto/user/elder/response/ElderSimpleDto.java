package com.five.Maeum_Eum.dto.user.elder.response;

import com.five.Maeum_Eum.entity.user.elder.Elder;

public record ElderSimpleDto(
        Long elderId,
        String elderName
) {

    public static ElderSimpleDto from(Elder elder){
        return new ElderSimpleDto(
                elder.getElderId(),
                elder.getElderName()
        );
    }
}
