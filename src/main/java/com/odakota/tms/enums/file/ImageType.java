package com.odakota.tms.enums.file;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author haidv
 * @version 1.0
 */
public enum ImageType {

    BRAND_IMAGE(1),
    AVATAR(2);

    @Getter
    private Integer value;

    ImageType(Integer value) {
        this.value = value;
    }

    /**
     * Get file path from string
     *
     * @param value a string indicating the file path
     * @return file path instance
     */
    public static ImageType of(Integer value) {
        return Arrays.stream(values()).filter(v -> v.value.equals(value)).findFirst().orElseThrow(
                () -> new IllegalArgumentException(String.format("ImageType = '%s' is not supported.", value)));
    }
}
