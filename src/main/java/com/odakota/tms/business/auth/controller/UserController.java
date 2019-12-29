package com.odakota.tms.business.auth.controller;

import com.odakota.tms.business.auth.entity.User;
import com.odakota.tms.business.auth.resource.UserResource;
import com.odakota.tms.business.auth.service.UserService;
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
public class UserController extends BaseController<User, UserResource> {

    private final UserService userService;

    private final ExportService<User> exportService;

    @Autowired
    public UserController(UserService userService,
                          ExportService<User> exportService) {
        super(userService);
        this.userService = userService;
        this.exportService = exportService;
    }

    /**
     * API get users list
     *
     * @param baseReq List acquisition request
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.R_USER)
    @GetMapping(value = "/users", produces = ApiVersion.API_VERSION_1)
    @ApiOperation(value = "", authorizations = @Authorization(FieldConstant.API_KEY))
    public ResponseEntity<?> getUsers(@ModelAttribute @Valid BaseParameter baseReq) {
        return super.getResources(baseReq);
    }

    /**
     * API create new user
     *
     * @param userResource {@link UserResource}
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.C_USER)
    @PostMapping(value = "/users", produces = ApiVersion.API_VERSION_1)
    @ApiOperation(value = "", authorizations = @Authorization(FieldConstant.API_KEY))
    public ResponseEntity<?> createUsers(@RequestBody @Validated UserResource userResource) {
        return super.createResource(userResource);
    }

    /**
     * API update user
     *
     * @param id           userId
     * @param userResource {@link UserResource}
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.U_USER)
    @PutMapping(value = "/users/{id}", produces = ApiVersion.API_VERSION_1)
    @ApiOperation(value = "", authorizations = @Authorization(FieldConstant.API_KEY))
    public ResponseEntity<?> updateUsers(@PathVariable Long id, @RequestBody @Validated UserResource userResource) {
        return super.updateResource(id, userResource);
    }

    /**
     * API delete user
     *
     * @param id user id
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.D_USER)
    @DeleteMapping(value = "/users/{id}", produces = ApiVersion.API_VERSION_1)
    @ApiOperation(value = "", authorizations = @Authorization(FieldConstant.API_KEY))
    public ResponseEntity<?> deleteUsers(@PathVariable Long id) {
        return super.deleteResource(id);
    }

    /**
     * API batch delete user
     *
     * @param ids list user id
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.D_USER)
    @DeleteMapping(value = "/users", produces = ApiVersion.API_VERSION_1)
    @ApiOperation(value = "", authorizations = @Authorization(FieldConstant.API_KEY))
    public ResponseEntity<?> batchDeleteUsers(List<Long> ids) {
        return super.batchDeleteResource(ids);
    }

    /**
     * API export users
     *
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.E_USER)
    @GetMapping(value = "/users/export", produces = ApiVersion.API_VERSION_1)
    @ApiOperation(value = "", authorizations = @Authorization(FieldConstant.API_KEY))
    public ResponseEntity<byte[]> exportUser(HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                MediaType.APPLICATION_OCTET_STREAM);//MediaType.parseMediaType("application/vnd.ms-excel")
        return new ResponseEntity<>(exportService.export(FileGroup.USER, response), headers, HttpStatus.OK);
    }
}
