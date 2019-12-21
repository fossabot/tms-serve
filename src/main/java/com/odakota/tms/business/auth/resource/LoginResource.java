package com.odakota.tms.business.auth.resource;

import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.system.annotations.groups.OnCreate;
import com.odakota.tms.system.annotations.groups.OnUpdate;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
public class LoginResource {

    @NotBlank(message = MessageCode.MSG_REQUIRED, groups = OnCreate.class)
    private String username;

    @NotBlank(message = MessageCode.MSG_REQUIRED, groups = OnCreate.class)
    private String password;

    @NotBlank(message = MessageCode.MSG_REQUIRED, groups = {OnCreate.class, OnUpdate.class})
    private String captcha;

    @NotBlank(message = MessageCode.MSG_REQUIRED, groups = OnCreate.class)
    private String checkKey;

    @NotBlank(message = MessageCode.MSG_REQUIRED, groups = OnUpdate.class)
    private String phone;
}
