package com.five.Maeum_Eum.entity.user.elder;

import java.util.HashMap;
import java.util.Map;

public enum ElderFamily {
    NO_FAMILY("관계없어요"),
    NOT_IN_HOME("집에 없어요"),
    IN_HOME("집에 있어요");

    private final String value;
    private static final Map<String, ElderFamily> map = new HashMap<>();

    static {
        for (ElderFamily family : ElderFamily.values()) {
            map.put(family.value, family);
        }
    }

    ElderFamily(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ElderFamily fromValue(String value) {
        return map.get(value);
    }
}

