package com.odakota.tms.enums.notify;

import lombok.Getter;

/**
 * @author haidv
 * @version 1.0
 */
public enum SendStatus {

    UNPUBLISHED(0),
    PUBLISHED(1),
    REVOKED(2);

    @Getter
    private Integer value;

    SendStatus(Integer value) {
        this.value = value;
    }

    public static SendStatus of(Integer value) {
        if (value == null) {
            return null;
        }
        for (SendStatus sendStatus : SendStatus.values()) {
            if (sendStatus.getValue().equals(value)) {
                return sendStatus;
            }
        }
        throw new IllegalArgumentException("Unknown sendStatus with value:" + value);
    }
}
