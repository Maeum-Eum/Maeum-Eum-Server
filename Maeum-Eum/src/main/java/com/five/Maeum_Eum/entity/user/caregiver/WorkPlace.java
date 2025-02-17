package com.five.Maeum_Eum.entity.user.caregiver;

import lombok.Getter;

@Getter
public enum WorkPlace {
    WALK_15_MIN("도보15분"),
    WALK_20_MIN("도보20분"),
    WITHIN_3KM("3KM이내"),
    WITHIN_5KM("5KM이내");

    private final String value;

    WorkPlace(final String value) {
        this.value = value;
    }
}
