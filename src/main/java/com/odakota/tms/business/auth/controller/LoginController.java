package com.odakota.tms.business.auth.controller;

import com.odakota.tms.business.auth.resource.LoginResource;
import com.odakota.tms.business.auth.service.LoginService;
import com.odakota.tms.business.auth.service.UserService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.system.annotations.NoAuthentication;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.config.data.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
public class LoginController {

    private final LoginService loginService;

    private final UserService userService;

    @Autowired
    public LoginController(LoginService loginService, UserService userService) {
        this.loginService = loginService;
        this.userService = userService;
    }

    /**
     * API login with account
     *
     * @param resource login resource
     * @return {@link ResponseEntity}
     */
    @NoAuthentication
    @PostMapping(value = "/account-login", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> accountLogin(@RequestBody LoginResource resource) {
        return ResponseEntity.ok(new ResponseData<>().success(loginService.login(resource)));
    }

    /**
     * API login with phone
     *
     * @param resource login resource
     * @return {@link ResponseEntity}
     */
    @NoAuthentication
    @PostMapping(value = "/phone-login", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> phoneLogin(@RequestBody LoginResource resource) {
        return ResponseEntity.ok(new ResponseData<>().success(loginService.login(resource)));
    }

    /**
     * API logout
     *
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication
    @DeleteMapping(value = "/logout", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }

    /**
     * API get captcha code
     *
     * @return {@link ResponseEntity}
     */
    @NoAuthentication
    @GetMapping(value = "/captcha", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> generateLoginCaptCha() {
        return ResponseEntity.ok(new ResponseData<>().success(loginService.generateLoginCaptCha()));
    }

    /**
     * API send otp login sms
     *
     * @return {@link ResponseEntity}
     */
    @NoAuthentication
    @GetMapping(value = "/sms-otp", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> sendOtp(@RequestParam String phone, @RequestParam Integer smsType) {
        loginService.sendSmsOTP(phone, smsType);
        return ResponseEntity.ok(new ResponseData<>());
    }

    /**
     * API get permission of user after login
     *
     * @return {@link ResponseEntity}
     */
    @RequiredAuthentication
    @GetMapping(value = "/user-permission", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getUserPermission() {
        return ResponseEntity.ok(new ResponseData<>().success(loginService.getUserPermissions()));
    }

    @NoAuthentication
    @GetMapping(value = "/forgot-password/step1", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> forgotPassStep1(@RequestParam String username) {
        return ResponseEntity.ok(new ResponseData<>().success(userService.getUser(username)));
    }

    @NoAuthentication
    @PostMapping(value = "/forgot-password/step2", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> forgotPassStep2() {
        return ResponseEntity.ok(new ResponseData<>());
    }

    @NoAuthentication
    @PostMapping(value = "/forgot-password/step3", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> forgotPassStep3() {
        return ResponseEntity.ok(new ResponseData<>());
    }
}
