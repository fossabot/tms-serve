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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
     * API get users list
     *
     * @param baseReq List acquisition request
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication(value = ApiId.R_USER)
    @GetMapping(value = "/users", produces = ApiVersion.API_VERSION_1)
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
    public ResponseEntity<?> batchDeleteUsers(List<Long> ids) {
        return super.batchDeleteResource(ids);
    }
}
