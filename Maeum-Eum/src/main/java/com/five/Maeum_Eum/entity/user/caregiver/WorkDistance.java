package com.five.Maeum_Eum.entity.user.caregiver;

import lombok.Getter;

@Getter
public enum WorkDistance {
    WALK_15("도보15분이내", 1.25),
    WALK_20("도보20분이내", 1.65),
    KM_3("3km", 3.0),
    KM_5("5km", 5.0);

    private final String label;
    private final double distance;

    WorkDistance(String label, double distance) {
        this.label = label;
        this.distance = distance;
    }

    // 입력된 문자열을 WorkDistance enum으로 변환하는 메서드
    public static WorkDistance fromLabel(String label) {
        for (WorkDistance place : values()) {
            if (place.label.equals(label)) {
                return place;
            }
        }
        throw new IllegalArgumentException("잘못된 거리범위 값입니다: " + label);
    }
}
