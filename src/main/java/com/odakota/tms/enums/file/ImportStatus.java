package com.odakota.tms.enums.file;

import lombok.Getter;

/**
 * @author haidv
 * @version 1.0
 */
public enum ImportStatus {

    IMPORTING(0),

    SUCCESS(1),

    ERROR(2);

    @Getter
    private Integer value;

    ImportStatus(Integer value) {
        this.value = value;
    }

    public static ImportStatus of(Integer value) {
        for (ImportStatus importStatus : ImportStatus.values()) {
            if (importStatus.getValue().equals(value)) {
                return importStatus;
            }
        }
        throw new IllegalArgumentException("Unknown import status with value:" + value);
    }
}
