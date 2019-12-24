package com.odakota.tms.business.auth.controller;

import com.odakota.tms.business.auth.entity.Permission;
import com.odakota.tms.business.auth.resource.PermissionResource;
import com.odakota.tms.business.auth.service.PermissionService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.constant.FieldConstant;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.base.BaseController;
import com.odakota.tms.system.config.data.ResponseData;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
public class PermissionController extends BaseController<Permission, PermissionResource> {

    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        super(permissionService);
        this.permissionService = permissionService;
    }

    /**
     * API get list permissions
     *
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication
    @GetMapping(value = "/permissions", produces = ApiVersion.API_VERSION_1)
    @ApiOperation(value = "", authorizations = @Authorization(FieldConstant.API_KEY))
    public ResponseEntity<?> getPermissions() {
        return ResponseEntity.ok(new ResponseData<>().success(permissionService.getPermissions()));
    }
}
