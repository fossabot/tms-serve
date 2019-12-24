package com.odakota.tms.business.auth.controller;

import com.odakota.tms.business.auth.entity.UserRole;
import com.odakota.tms.business.auth.resource.UserRoleResource;
import com.odakota.tms.business.auth.service.UserRoleService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.constant.FieldConstant;
import com.odakota.tms.enums.ApiId;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.base.BaseController;
import com.odakota.tms.system.config.data.ResponseData;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
public class UserRoleController extends BaseController<UserRole, UserRoleResource> {

    private final UserRoleService userRoleService;

    @Autowired
    public UserRoleController(UserRoleService userRoleService) {
        super(userRoleService);
        this.userRoleService = userRoleService;
    }

    /**
     * API get list role id of user
     *
     * @param userId userId
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.R_ROLE)
    @GetMapping(value = "/user-roles", produces = ApiVersion.API_VERSION_1)
    @ApiOperation(value = "", authorizations = @Authorization(FieldConstant.API_KEY))
    public ResponseEntity<?> getRoles(@RequestParam Long userId) {
        return ResponseEntity.ok(new ResponseData<>().success(userRoleService.getUserRoleIds(userId)));
    }
}
