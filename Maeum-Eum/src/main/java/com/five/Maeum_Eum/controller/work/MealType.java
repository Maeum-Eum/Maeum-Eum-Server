package com.five.Maeum_Eum.controller.work;

import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum MealType {
    ONE("모두가능",5),
    TWO("식사 도움",2),
    THREE("요리 가능",3),
    FOUR("콧줄 식사",4);

    private final String label;
    private final int level;

    MealType(String label, int level) {
        this.label = label;
        this.level = level;
    }

    public static MealType fromLabel(String label) {
        for (MealType place : values()) {
            if (place.label.equals(label)) {
                return place;
            }
        }
        throw new CustomException(ErrorCode.INVALID_INPUT, "잘못된 Meal 입력입니다 : " + label + " 필요한 입력 모두가능 / 식사 도움 / 요리 가능 / 콧줄 식사");
    }
}
