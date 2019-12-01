package com.odakota.tms.business.auth.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordResource {

    @Setter @Getter
    private String userName;

    @Setter @Getter
    private String password;

    @Setter @Getter
    private String passwordConfirm;

    @Setter @Getter
    private String verifyCode;
}
