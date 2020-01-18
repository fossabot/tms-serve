package com.odakota.tms.enums.auth;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
public enum ApiId {

    DEFAULT("Default"),
    C_BRAND("brand:create"),
    R_BRAND("brand:read"),
    U_BRAND("brand:update"),
    D_BRAND("brand:delete"),
    C_BRANCH("branch:create"),
    R_BRANCH("branch:read"),
    U_BRANCH("branch:update"),
    D_BRANCH("branch:delete"),
    E_BRANCH("branch:export"),
    I_BRANCH("branch:import"),
    C_ROLE("role:create"),
    R_ROLE("role:read"),
    U_ROLE("role:update"),
    D_ROLE("role:delete"),
    E_ROLE("role:export"),
    I_ROLE("role:import"),
    U_ROLE_PERMISSION("role:permission:update"),
    C_USER("user:create"),
    R_USER("user:read"),
    U_USER("user:update"),
    D_USER("user:delete"),
    E_USER("user:export"),
    I_USER("user:import");

    @Setter @Getter
    private String value;

    ApiId(String value) {
        this.value = value;
    }

//    public static ApiId of(String value) {
//        for (ApiId apiId : ApiId.values()) {
//            if (apiId.getValue().equals(value)) {
//                return apiId;
//            }
//        }
//        throw new IllegalArgumentException("Unknown ApiId with value:" + value);
//    }
}
