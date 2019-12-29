package com.odakota.tms.business.auth.controller;

import com.odakota.tms.business.auth.entity.Role;
import com.odakota.tms.business.auth.resource.RoleResource;
import com.odakota.tms.business.auth.service.RoleService;
import com.odakota.tms.business.transfers.ExportService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.constant.FieldConstant;
import com.odakota.tms.enums.ApiId;
import com.odakota.tms.enums.FileGroup;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.base.BaseController;
import com.odakota.tms.system.base.BaseParameter;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
public class RoleController extends BaseController<Role, RoleResource> {

    private final RoleService service;

    private final ExportService<Role> exportService;

    @Autowired
    protected RoleController(RoleService service, ExportService<Role> exportService) {
        super(service);
        this.service = service;
        this.exportService = exportService;
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
     * API get role by id
     *
     * @param id role id
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.R_ROLE)
    @GetMapping(value = "/roles/{id}", produces = ApiVersion.API_VERSION_1)
    @ApiOperation(value = "", authorizations = @Authorization(FieldConstant.API_KEY))
    public ResponseEntity<?> getRole(@PathVariable Long id) {
        return super.getResource(id);
    }

    /**
     * API create new role
     *
     * @param resource {@link RoleResource}
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.C_ROLE)
    @PostMapping(value = "/roles", produces = ApiVersion.API_VERSION_1)
    @ApiOperation(value = "", authorizations = @Authorization(FieldConstant.API_KEY))
    public ResponseEntity<?> createRole(@Validated @RequestBody RoleResource resource) {
        return super.createResource(resource);
    }

    /**
     * API update role
     *
     * @param id       role id
     * @param resource {@link RoleResource}
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.U_ROLE)
    @PutMapping(value = "/roles/{id}", produces = ApiVersion.API_VERSION_1)
    @ApiOperation(value = "", authorizations = @Authorization(FieldConstant.API_KEY))
    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody RoleResource resource) {
        return super.updateResource(id, resource);
    }

    /**
     * API delete role
     *
     * @param id role id
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.D_ROLE)
    @DeleteMapping(value = "/roles/{id}", produces = ApiVersion.API_VERSION_1)
    @ApiOperation(value = "", authorizations = @Authorization(FieldConstant.API_KEY))
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        return super.deleteResource(id);
    }

    /**
     * API batch delete role
     *
     * @param ids list role id
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.D_ROLE)
    @DeleteMapping(value = "/roles", produces = ApiVersion.API_VERSION_1)
    @ApiOperation(value = "", authorizations = @Authorization(FieldConstant.API_KEY))
    public ResponseEntity<Void> batchDeleteRole(@RequestParam List<Long> ids) {
        return super.batchDeleteResource(ids);
    }

    /**
     * API export role
     *
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.E_ROLE)
    @GetMapping(value = "/roles/export", produces = ApiVersion.API_VERSION_1)
    @ApiOperation(value = "", authorizations = @Authorization(FieldConstant.API_KEY))
    public ResponseEntity<byte[]> exportRole(HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                MediaType.APPLICATION_OCTET_STREAM);//MediaType.parseMediaType("application/vnd.ms-excel")
        return new ResponseEntity<>(exportService.export(FileGroup.ROLE, response), headers, HttpStatus.OK);
    }
}
