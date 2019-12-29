package com.odakota.tms.business.auth.resource;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
public class LoginResponse {

    private String token;

    private UserResource userInfo;
}
