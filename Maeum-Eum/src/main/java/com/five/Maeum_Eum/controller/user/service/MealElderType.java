package com.five.Maeum_Eum.controller.user.service;

import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum MealElderType {

    ONE("스스로 식사 가능",1),
    TWO("식사 차림 필요",2),
    THREE("죽, 반찬 등 요리 필요",3),
    FOUR("콧줄 식사",4);

    private final String label;
    private final int level;

    MealElderType(String label, int level) {
        this.label = label;
        this.level = level;
    }

    public static MealElderType fromLabel(String label) {
        for (MealElderType place : values()) {
            if (place.label.equals(label)) {
                return place;
            }
        }
        throw new CustomException(ErrorCode.INVALID_INPUT, "잘못된 Meal 입력입니다 : " + label
                + " 필요한 입력 : / 스스로 식사 가능 / 식사 차림 필요 / 죽, 반찬 등 요리 필요 / 콧줄 식사");
    }
}
