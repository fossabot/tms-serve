package com.odakota.tms.business.auth.resource;

import com.odakota.tms.business.auth.entity.User;
import com.odakota.tms.constant.MessageCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
public class LoginResource {

    @NotBlank(message = MessageCode.MSG_REQUIRED)
    private String username;

    @NotBlank(message = MessageCode.MSG_REQUIRED)
    private String password;

    @NotBlank(message = MessageCode.MSG_REQUIRED)
    private String captcha;

    @NotBlank(message = MessageCode.MSG_REQUIRED)
    private String checkKey;

    private String token;

    private User userInfo;
}
