package com.five.Maeum_Eum.controller.work;

import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum DailyType {
    ONE("청소 및 주변 정돈",1),
    TWO("목욕 보조",2),
    THREE("병원 동행",3),
    FOUR("간단한 운동 지원",4),
    FIVE("말벗 지원",4),
    SIX("인지자극 활동",4);

    private final String label;
    private final int level;

    DailyType(String label, int level) {
        this.label = label;
        this.level = level;
    }

    public static DailyType fromLabel(String label) {
        for (DailyType place : values()) {
            if (place.label.equals(label)) {
                return place;
            }
        }
        throw new CustomException(ErrorCode.INVALID_INPUT, "잘못된 Daily 입력입니다 : " + label
                + " 필요한 입력 : 청소 및 주변 정돈 / 목욕 보조 / 병원 동행 / 간단한 운동 지원 / 말벗 지원 / 인지자극 활동 ");
    }
}


