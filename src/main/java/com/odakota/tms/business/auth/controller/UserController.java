package com.odakota.tms.business.auth.controller;

import com.odakota.tms.business.auth.entity.User;
import com.odakota.tms.business.auth.resource.UserResource;
import com.odakota.tms.business.auth.service.UserService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.enums.ApiId;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.base.BaseController;
import com.odakota.tms.system.base.BaseParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
public class UserController extends BaseController<User, UserResource> {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    /**
     * API get roles list
     *
     * @param baseReq List acquisition request
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.R_ROLE)
    @GetMapping(value = "/users", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getUsers(@ModelAttribute @Valid BaseParameter baseReq) {
        return super.getResources(baseReq);
    }
}
