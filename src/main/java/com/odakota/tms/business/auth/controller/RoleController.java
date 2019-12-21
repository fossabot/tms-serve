package com.odakota.tms.business.auth.controller;

import com.odakota.tms.business.auth.entity.Role;
import com.odakota.tms.business.auth.resource.RoleResource;
import com.odakota.tms.business.auth.service.RoleService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.constant.FieldConstant;
import com.odakota.tms.enums.ApiId;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.base.BaseController;
import com.odakota.tms.system.base.BaseParameter;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
public class RoleController extends BaseController<Role, RoleResource> {

    private final RoleService service;

    @Autowired
    protected RoleController(RoleService service) {
        super(service);
        this.service = service;
    }

    /**
     * API get roles list
     *
     * @param baseReq List acquisition request
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.R_ROLE)
    @GetMapping(value = "/roles", produces = ApiVersion.API_VERSION_1)
    @ApiOperation(value = "", authorizations = @Authorization(FieldConstant.API_KEY))
    public ResponseEntity<?> getRoles(@ModelAttribute @Valid BaseParameter baseReq) {
        return super.getResources(baseReq);
    }

    /**
     * API get role
     *
     * @param id Resource identifier (long)
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.R_ROLE)
    @GetMapping(value = "/roles/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getRole(@PathVariable Long id) {
        return super.getResource(id);
    }

    /**
     * API create new role
     *
     * @param resource resource
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.C_ROLE)
    @PostMapping(value = "/roles", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> createRole(@Validated @RequestBody RoleResource resource) {
        return super.createResource(resource);
    }

    /**
     * API update role
     *
     * @param id       Resource identifier
     * @param resource resource
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.U_ROLE)
    @PutMapping(value = "/roles/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody RoleResource resource) {
        return super.updateResource(id, resource);
    }

    /**
     * API delete role
     *
     * @param id Resource identifier
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.D_ROLE)
    @DeleteMapping(value = "/roles/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        return super.deleteResource(id);
    }
}
