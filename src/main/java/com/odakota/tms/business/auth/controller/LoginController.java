package com.odakota.tms.business.auth.controller;

import com.odakota.tms.business.auth.resource.LoginResource;
import com.odakota.tms.business.auth.service.LoginService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.system.annotations.NoAuthentication;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.config.data.ResponseData;
import com.odakota.tms.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * Login API
     *
     * @param resource login resource
     * @return {@link ResponseEntity}
     */
    @SuppressWarnings("unchecked")
    @NoAuthentication
    @PostMapping(value = "/login", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<ResponseData> login(@RequestBody LoginResource resource) {
        return ResponseEntity.ok(new ResponseData().success(loginService.login(resource)));
    }

    /**
     * Logout API
     */
    @RequiredAuthentication
    @DeleteMapping(value = "/logout", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }

    @SuppressWarnings("unchecked")
    @NoAuthentication
    @GetMapping(value = "/capcha", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<ResponseData<Map<String, String>>> getCheckCode() {
        Map<String, String> map = new HashMap<>();
        String code = RandomUtils.generateAlphaNumeric(1);
        String key = code + UUID.randomUUID().toString();
        map.put("key", key);
        map.put("code", code);
        return ResponseEntity.ok(new ResponseData<>().success(map));
    }

    @RequiredAuthentication
    @GetMapping(value = "/user-permission", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<ResponseData> getUserPermissionByToken() {
        return ResponseEntity.ok(new ResponseData<>().success(loginService.getPermissions((long)1)));
    }
}
