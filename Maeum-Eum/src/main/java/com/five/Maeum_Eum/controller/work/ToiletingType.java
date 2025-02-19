package com.five.Maeum_Eum.controller.work;

import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum ToiletingType {
    ONE("null",1),
    TWO("화장실 이용하기",2),
    THREE("기저귀 교환",3),
    FOUR("배뇨배변보조",4);

    private final String label;
    private final int level;

    ToiletingType(String label, int level) {
        this.label = label;
        this.level = level;
    }

    public static ToiletingType fromLabel(String label) {
        for (ToiletingType place : values()) {
            if (place.label.equals(label)) {
                return place;
            }
        }
        throw new CustomException(ErrorCode.INVALID_INPUT, "잘못된 Toilet 입력입니다 : " + label
                + "맞는 입력 : null / 화장실 이용하기 / 기저귀 교환 / 배뇨배변보조"
        );
    }
}


