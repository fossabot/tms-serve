package com.odakota.tms.business.auth.controller;

import com.odakota.tms.business.auth.entity.PermissionRole;
import com.odakota.tms.business.auth.resource.PermissionRoleResource;
import com.odakota.tms.business.auth.service.PermissionRoleService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.enums.ApiId;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.base.BaseController;
import com.odakota.tms.system.config.data.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
public class PermissionRoleController extends BaseController<PermissionRole, PermissionRoleResource> {

    private final PermissionRoleService permissionRoleService;

    @Autowired
    public PermissionRoleController(PermissionRoleService permissionRoleService) {
        super(permissionRoleService);
        this.permissionRoleService = permissionRoleService;
    }

    /**
     * API get list permission of role
     *
     * @param roleId role id
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication
    @GetMapping(value = "/permission-roles", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getRolePermissions(@RequestParam(name = "roleId") Long roleId) {
        return ResponseEntity.ok(new ResponseData<>().success(permissionRoleService.getPermissionRoleIds(roleId)));
    }

    /**
     * API save permission role
     *
     * @param resource PermissionRoleResource
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(ApiId.U_ROLE_PERMISSION)
    @PostMapping(value = "/permission-roles", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> saveRolePermissions(@RequestBody PermissionRoleResource resource) {
        permissionRoleService.saveRolePermissions(resource);
        return ResponseEntity.ok(new ResponseData<>());
    }
}
