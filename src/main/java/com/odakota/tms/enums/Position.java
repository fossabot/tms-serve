package com.odakota.tms.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
public enum Position {

    MEMBER(1, "Member");

    @Setter @Getter
    private Integer value;

    @Setter @Getter
    private String label;

    Position(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    public static Position of(Integer value) {
        for (Position position : Position.values()) {
            if (position.getValue().equals(value)) {
                return position;
            }
        }
        throw new IllegalArgumentException("Unknown position with value:" + value);
    }
}
