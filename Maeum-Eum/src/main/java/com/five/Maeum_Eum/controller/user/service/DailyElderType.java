package com.five.Maeum_Eum.controller.user.service;

import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum DailyElderType {

    ONE("청소, 빨래 보조",1),
    TWO("목욕 보조(몸씻기)",2),
    THREE("병원 동행",3),
    FOUR("산책,간단한 운동 지원",4),
    FIVE("말벗 등 정서 지원",5),
    SIX("인지 자극 활동",6);

    private final String label;
    private final int level;

    DailyElderType (String label, int level) {
        this.label = label;
        this.level = level;
    }

    public static DailyElderType  fromLabel(String label) {
        for (DailyElderType  place : values()) {
            if (place.label.equals(label)) {
                return place;
            }
        }
        throw new CustomException(ErrorCode.INVALID_INPUT, "잘못된 Daily 입력입니다 : " + label
                + " 필요한 입력 : / 청소, 빨래 보조 / 목욕 보조(몸씻기) / 병원 동행 / 산책,간단한 운동 지원 / 말벗 등 정서 지원 / 인지 자극 활동");
    }
}