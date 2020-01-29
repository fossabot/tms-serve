package com.odakota.tms.enums.file;

import lombok.Getter;

import java.util.Arrays;

/**
 * an enumeration that indicates the storage type
 *
 * @author haidv
 * @version 1.0
 */
public enum StorageType {

    CDN("cdn"),
    FILES("files"),
    EXPORTS("exports");

    @Getter
    private final String value;

    StorageType(final String value) {
        this.value = value;
    }

    /**
     * get link storage type from the string
     *
     * @param value {@link StorageType} numeric value indicating
     * @return {@link StorageType}
     */
    public static StorageType of(final String value) {
        return Arrays.stream(values())
                     .filter(v -> v.value.equals(value))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException(
                             String.format("StorageType = '%s' is not supported.", value)));
    }
}
