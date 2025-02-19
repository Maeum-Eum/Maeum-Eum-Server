package com.five.Maeum_Eum.controller.user.service;

import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum ToiletingElderType {

    ONE("스스로 배변 가능",1),
    TWO("배변 실수시 도움필요",2),
    THREE("기저귀 케어 필요",3),
    FOUR("유치도뇨, 방관루, 장루 관리",4);

    private final String label;
    private final int level;

    ToiletingElderType(String label, int level) {
        this.label = label;
        this.level = level;
    }

    public static ToiletingElderType fromLabel(String label) {
        for (ToiletingElderType place : values()) {
            if (place.label.equals(label)) {
                return place;
            }
        }
        throw new CustomException(ErrorCode.INVALID_INPUT, "잘못된 Toileting 입력입니다 : " + label
                + " 필요한 입력 : / 스스로 배변 가능 / 배변 실수시 도움필요 / 기저귀 케어 필요 / 유치도뇨, 방관루, 장루 관리");
    }
}
