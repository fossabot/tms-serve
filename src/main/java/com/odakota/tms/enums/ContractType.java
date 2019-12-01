package com.odakota.tms.enums;

import lombok.Getter;

/**
 * @author haidv
 * @version 1.0
 */
public enum ContractType {

    SEASONAL(1, "seasonal contracts"),

    DEFINITE_TERN(2, "contract for a definite term"),

    INDEFINITELY(3, "contract indefinitely");

    @Getter
    private Integer value;

    @Getter
    private String name;

    ContractType(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static ContractType of(Integer value) {
        for (ContractType contractType : ContractType.values()) {
            if (contractType.getValue().equals(value)) {
                return contractType;
            }
        }
        throw new IllegalArgumentException("Unknown contract type with value:" + value);
    }
}
