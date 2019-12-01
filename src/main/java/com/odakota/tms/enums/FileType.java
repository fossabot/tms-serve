package com.odakota.tms.enums;

import lombok.Getter;

/**
 * @author haidv
 * @version 1.0
 */
public enum FileType {

    EXCEL(1, "excel"),

    CSV(2, "csv"),

    PDF(3, "pdf");

    @Getter
    private Integer value;

    @Getter
    private String name;

    FileType(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static FileType of(Integer value) {
        for (FileType fileType : FileType.values()) {
            if (fileType.getValue().equals(value)) {
                return fileType;
            }
        }
        throw new IllegalArgumentException("Unknown file type with value:" + value);
    }
}
