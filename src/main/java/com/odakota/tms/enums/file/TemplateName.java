package com.odakota.tms.enums.file;

import lombok.Getter;

/**
 * @author haidv
 * @version 1.0
 */
public enum TemplateName {

    TEMPLATE_CREATE_ACCOUNT("create-account.html"),
    TEMPLATE_RESET_PASS("reset-pass.html"),
    TEMPLATE_CONFIRM("confirm.html");

    @Getter
    private String value;

    TemplateName(String value) {
        this.value = value;
    }
}
