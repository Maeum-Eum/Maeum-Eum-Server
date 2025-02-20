package com.five.Maeum_Eum.controller.work;

import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum MobilityType {
    ONE("모두가능",5),
    TWO("이동 도움",2),
    THREE("휠체어 이동 보조",3),
    FOUR("침대에서 휠체어 이동 돕기",4);

    private final String label;
    private final int level;

    MobilityType(String label, int level) {
        this.label = label;
        this.level = level;
    }

    public static MobilityType fromLabel(String label) {
        for (MobilityType place : values()) {
            if (place.label.equals(label)) {
                return place;
            }
        }
        throw new CustomException(ErrorCode.INVALID_INPUT, "잘못된 Mobility 입력입니다 : " + label
        + "맞는 입력 : 모두가능 / 이동 도움 / 휠체어 이동 보조 / 침대에서 휠체어 이동 돕기"
        );
    }
}


