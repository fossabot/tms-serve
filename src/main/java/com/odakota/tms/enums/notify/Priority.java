package com.odakota.tms.enums.notify;

import lombok.Getter;

/**
 * @author haidv
 * @version 1.0
 */
public enum Priority {

    LOW(1),
    MEDIUM(2),
    HIGH(3);

    @Getter
    private Integer value;

    Priority(Integer value) {
        this.value = value;
    }

    public static Priority of(Integer value) {
        if (value == null) {
            return null;
        }
        for (Priority priority : Priority.values()) {
            if (priority.getValue().equals(value)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Unknown priority with value:" + value);
    }
}
