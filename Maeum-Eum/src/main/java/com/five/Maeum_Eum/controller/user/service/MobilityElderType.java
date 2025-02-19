package com.five.Maeum_Eum.controller.user.service;

import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum MobilityElderType {

    ONE("스스로 거동 가능",1),
    TWO("이동시 부족 도움",2),
    THREE("휠체어 이동 보조",3),
    FOUR("거동 불가",4);

    private final String label;
    private final int level;

    MobilityElderType(String label, int level) {
        this.label = label;
        this.level = level;
    }

    public static MobilityElderType fromLabel(String label) {
        for (MobilityElderType place : values()) {
            if (place.label.equals(label)) {
                return place;
            }
        }
        throw new CustomException(ErrorCode.INVALID_INPUT, "잘못된 Mobility 입력입니다 : " + label
                + " 필요한 입력 : / 스스로 거동 가능 / 이동시 부족 도움 / 휠체어 이동 보조 / 거동 불가");
    }
}
