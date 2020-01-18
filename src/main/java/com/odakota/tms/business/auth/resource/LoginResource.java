package com.odakota.tms.business.auth.resource;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
public class LoginResource {

    private String username;

    private String password;

    private String captcha;

    private String checkKey;

    private String phone;
}
