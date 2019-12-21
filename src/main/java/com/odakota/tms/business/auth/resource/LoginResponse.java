package com.odakota.tms.business.auth.resource;

import com.odakota.tms.business.auth.entity.User;
import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
public class LoginResponse {

    private String token;

    private User userInfo;
}
