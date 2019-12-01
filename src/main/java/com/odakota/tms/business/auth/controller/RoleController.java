package com.odakota.tms.business.auth.controller;

import com.odakota.tms.business.auth.entity.Role;
import com.odakota.tms.business.auth.resource.RoleResource;
import com.odakota.tms.business.auth.service.RoleService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.enums.ApiId;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.base.BaseController;
import com.odakota.tms.system.base.BaseParameter;
import com.odakota.tms.system.base.BaseResponse;
import com.odakota.tms.system.config.data.ResponseData;
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
    public ResponseEntity<ResponseData<BaseResponse<RoleResource>>> getRoles(
            @ModelAttribute @Valid BaseParameter baseReq) {
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
    public ResponseEntity<ResponseData<RoleResource>> getRole(@PathVariable Long id) {
        return super.getResource(id);
    }

    /**
     * New role creation API
     *
     * @param resource resource
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.C_ROLE)
    @PostMapping(value = "/roles", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<ResponseData<RoleResource>> createRole(@Validated @RequestBody RoleResource resource) {
//        if (service.existsByRoleName(null, resource.getRoleName())) {
//            result.rejectValue(Constant.ROLE_NAME, EXISTED, MessageCode.MSG_DUPLICATED);
//        }
        return super.createResource(resource);
    }

    /**
     * Role update API
     *
     * @param id       Resource identifier
     * @param resource resource
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.U_ROLE)
    @PutMapping(value = "/roles/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<ResponseData<RoleResource>> updateRole(@PathVariable Long id,
                                                                 @RequestBody RoleResource resource) {
        return super.updateResource(id, resource);
    }

    /**
     * Role deletion API
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
