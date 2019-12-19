package com.odakota.tms.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * storage item type
 *
 * @author haidv
 * @version 1.0
 */
public enum StorageItemType {
    FILE(1),
    FOLDER(2);

    @Getter
    private final Integer value;

    StorageItemType(final Integer value) {
        this.value = value;
    }

    /**
     * get {@link StorageItemType} from the number.
     *
     * @param value {@link StorageItemType} numeric value indicating
     * @return {@link StorageItemType}
     */
    public static StorageItemType of(final Integer value) {
        return Arrays.stream(values())
                     .filter(v -> v.value.equals(value))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException(
                             String.format("StorageItemType = '%s' is not supported.", value)));
    }
}
