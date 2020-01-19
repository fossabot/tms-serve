package com.odakota.tms.enums.notify;

import lombok.Getter;

/**
 * @author haidv
 * @version 1.0
 */
public enum MsgType {

    NOTIFICATION_BULLETIN(1),
    SYSTEM(2);

    @Getter
    private Integer value;

    MsgType(Integer value) {
        this.value = value;
    }

    public static MsgType of(Integer value) {
        if (value == null) {
            return null;
        }
        for (MsgType msgType : MsgType.values()) {
            if (msgType.getValue().equals(value)) {
                return msgType;
            }
        }
        throw new IllegalArgumentException("Unknown msgType with value:" + value);
    }
}
