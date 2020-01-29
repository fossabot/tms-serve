package com.odakota.tms.enums.auth;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
public enum Gender {

    MALE(1),

    FEMALE(2),

    OTHER(0);

    @Setter @Getter
    private Integer value;

    Gender(Integer value) {
        this.value = value;
    }

    public static Gender of(Integer value) {
        for (Gender gender : Gender.values()) {
            if (gender.getValue().equals(value)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Unknown gender with value:" + value);
    }
}
