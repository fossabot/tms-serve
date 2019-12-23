package com.odakota.tms.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
public enum ApiId {

    DEFAULT(-1, "Default"),
    C_ROLE(1, "Create role"),
    R_ROLE(2, "Read role"),
    U_ROLE(3, "Update role"),
    D_ROLE(4, "Delete role"),
    C_USER(9, "Create user"),
    R_USER(10, "Read user"),
    U_USER(11, "Update user"),
    D_USER(12, "Delete user"),
    C_BRANCH(13, "Create division"),
    R_BRANCH(14, "Read division"),
    U_BRANCH(15, "Update division"),
    D_BRANCH(16, "Delete division"),
    C_WORK(17, "Create work"),
    R_WORK(18, "Read work"),
    U_WORK(19, "Update work"),
    C_IMPORT_FILE(20, "Import file"),
    R_IMPORT_FILE(21, "Read list import file"),
    C_CONTRACT(22, "Create contract"),
    R_CONTRACT(23, "Read contract"),
    U_CONTRACT(24, "Update contract"),
    D_CONTRACT(25, "Delete contract");

    @Setter @Getter
    private Integer value;

    @Setter @Getter
    private String label;

    ApiId(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    public static ApiId of(Integer value) {
        for (ApiId apiId : ApiId.values()) {
            if (apiId.getValue().equals(value)) {
                return apiId;
            }
        }
        throw new IllegalArgumentException("Unknown ApiId with value:" + value);
    }
}
