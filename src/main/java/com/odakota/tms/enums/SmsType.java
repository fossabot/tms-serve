package com.odakota.tms.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
public enum SmsType {

    SMS_LOGIN(1),
    SMS_FORGOT(2);

    @Setter @Getter
    private int value;

    SmsType(int value) {
        this.value = value;
    }

    public static SmsType of(int value) {
        for (SmsType smsType : SmsType.values()) {
            if (smsType.getValue() == value) {
                return smsType;
            }
        }
        throw new IllegalArgumentException("Unknown smsType with value:" + value);
    }
}
