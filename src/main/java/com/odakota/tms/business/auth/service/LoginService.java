package com.odakota.tms.business.auth.service;

import com.odakota.tms.business.auth.entity.Permission;
import com.odakota.tms.business.auth.entity.User;
import com.odakota.tms.business.auth.repository.UserRepository;
import com.odakota.tms.business.auth.resource.LoginResource;
import com.odakota.tms.business.auth.resource.LoginResponse;
import com.odakota.tms.business.auth.resource.userpermission.Auth;
import com.odakota.tms.business.auth.resource.userpermission.Menu;
import com.odakota.tms.business.auth.resource.userpermission.PermissionMetaResource;
import com.odakota.tms.business.auth.resource.userpermission.UserPermissionResource;
import com.odakota.tms.constant.Constant;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.enums.LoginType;
import com.odakota.tms.enums.SmsType;
import com.odakota.tms.system.config.UserSession;
import com.odakota.tms.system.config.exception.CustomException;
import com.odakota.tms.system.config.interceptor.TokenProvider;
import com.odakota.tms.system.service.OtpGenerator;
import com.odakota.tms.system.service.sns.SmsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class LoginService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    private final PermissionService permissionService;

    private final OtpGenerator otpGenerator;

    private final UserSession userSession;

    private final SmsService smsService;

    @Autowired
    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider,
                        PermissionService permissionService, OtpGenerator otpGenerator, UserSession userSession,
                        SmsService smsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.permissionService = permissionService;
        this.otpGenerator = otpGenerator;
        this.userSession = userSession;
        this.smsService = smsService;
    }

    /**
     * Login
     *
     * @param loginResource LoginResource
     * @return Object
     */
    public Object login(LoginResource loginResource, LoginType loginType) {
        String key;
        if (loginType.equals(LoginType.ACCOUNT)) {
            key = loginResource.getCheckKey();
        } else {
            key = Constant.LOGIN_PHONE_PREFIX_KEY + loginResource.getPhone();
        }
        // check captcha
        Map<String, Object> map = otpGenerator.getOPTByKey(key);
        if (map == null || map.size() == 0) {
            throw new CustomException(MessageCode.MSG_CAPTCHA_EXPIRED, HttpStatus.BAD_REQUEST);
        }
        if (!map.get(Constant.OTP_CODE_OTP).equals(loginResource.getCaptcha())) {
            throw new CustomException(MessageCode.MSG_CAPTCHA_INVALID, HttpStatus.BAD_REQUEST);
        }
        // clear cache
        otpGenerator.clearOTPFromCache(key);
        User user = userRepository
                .findByUsernameOrPhoneAndDeletedFlagFalse(loginResource.getUsername(), loginResource.getPhone())
                .orElseThrow(() -> new CustomException(MessageCode.MSG_INVALID_USERNAME_PASS,
                                                       HttpStatus.BAD_REQUEST));
        // check password
        if (loginType.equals(LoginType.ACCOUNT) &&
            !passwordEncoder.matches(loginResource.getPassword(), user.getPassword())) {
            throw new CustomException(MessageCode.MSG_INVALID_USERNAME_PASS, HttpStatus.BAD_REQUEST);
        }
        // check status
        if (user.isDisableFlag()) {
            throw new CustomException(MessageCode.MSG_ACCOUNT_DISABLED, HttpStatus.CONFLICT);
        }
        String token = tokenProvider.generateToken(user.getId(), user.getUsername());
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserInfo(user);
        return response;
    }

    /**
     * Generate login captcha
     *
     * @return map
     */
    public Map<String, String> generateLoginCaptCha() {
        Map<String, String> map = new HashMap<>();
        String key = UUID.randomUUID().toString().replace("-", "");
        String code = otpGenerator.generateOTP(key, 3, null);
        map.put("key", key);
        map.put("code", code);
        return map;
    }

    /**
     * Send sms otp
     */
    public void sendSmsOTP(String phone, Integer smsType) {
        String code = null;
        String message = null;
        if (smsType == SmsType.SMS_LOGIN.getValue()) {
            code = otpGenerator.generateOTP(Constant.LOGIN_PHONE_PREFIX_KEY + phone);
            message = "MA XAC NHAN LOGIN CUA BAN LA: " + code;
        }
        if (smsType == SmsType.SMS_FORGOT.getValue()) {
            code = otpGenerator.generateOTP(Constant.FORGOT_PASS_PREFIX_KEY + phone);
            message = "MA XAC NHAN RESET PASSWORD CUA BAN LA: " + code;
        }
        if (message != null && phone != null) {
            smsService.sendSMSMessage(message, phone);
        }
    }

    public UserPermissionResource getUserPermissions() {
        List<Permission> permissions = permissionService.getResources(userSession.getUserId());
        UserPermissionResource userPermissionResource = new UserPermissionResource();
        List<Menu> menus = new ArrayList<>();
        List<Auth> auths = new ArrayList<>();
        List<Auth> allAuths = new ArrayList<>();
        List<Permission> allAuthList = permissionService.getResources(Constant.MENU_TYPE_BUTTON);
        this.getAllAuthJsonArray(allAuths, allAuthList);
        this.getAuthJsonArray(auths, permissions);
        this.getPermissionJsonArray(menus, permissions, null);
        //Routing menu
        userPermissionResource.setMenu(menus);
        //All permissions configuration (button permissions, access permissions)
        userPermissionResource.setAllAuth(allAuths);
        //Button permissions
        userPermissionResource.setAuth(auths);
        return userPermissionResource;
    }

    private void getAuthJsonArray(List<Auth> auths, List<Permission> permissions) {
        for (Permission permission : permissions) {
            if (permission.getMenuType() == null) {
                continue;
            }
            Auth auth;
            if (permission.getMenuType() == Constant.MENU_TYPE_BUTTON && permission.getStatus()) {
                auth = new Auth();
                auth.setAction(permission.getPerms());
                auth.setType(permission.getPermsType());
                auth.setDescribe(permission.getName());
                auths.add(auth);
            }
        }
    }

    private void getAllAuthJsonArray(List<Auth> allAuths, List<Permission> permissions) {
        Auth allAuth;
        for (Permission permission : permissions) {
            allAuth = new Auth();
            allAuth.setAction(permission.getPerms());
            allAuth.setType(permission.getPermsType());
            allAuth.setDescribe(permission.getName());
            allAuth.setStatus(permission.getStatus());
            allAuths.add(allAuth);
        }
    }

    private Menu getPermissionJsonObject(Permission permission) {
        Menu menu = new Menu();
        // type (0: first-level menu 1: sub-menu 2: button)
        if (permission.getMenuType() == Constant.MENU_TYPE_BUTTON) {
            return null;
        } else if (permission.getMenuType() == Constant.MENU_TYPE_MENU ||
                   permission.getMenuType() == Constant.MENU_TYPE_SUB_MENU) {
            menu.setId(permission.getId());
            menu.setPath(permission.getUrl());
            if (permission.getRoute()) {
                menu.setRoute(1);// generate route
            } else {
                menu.setRoute(0);// indicates that no route is generated
            }

            // Important rules: route name (generate route name by URL, route name for front-end development, page jump use)
            if (!StringUtils.isBlank(permission.getComponentName())) {
                menu.setName(permission.getComponentName());
            } else {
                menu.setName(urlToRouteName(permission.getUrl()));
            }

            // Whether to hide the route, the default is displayed
            if (permission.getHidden() != null) {
                menu.setHidden(permission.getHidden());
            }
            // Aggregate route
            if (permission.getAlwaysShow() != null) {
                menu.setAlwaysShow(permission.getAlwaysShow());
            }
            menu.setComponent(permission.getComponent());
            PermissionMetaResource permissionMetaResource = new PermissionMetaResource();
            // Set by the user whether to cache the page with boolean
            if (permission.getKeepAlive() != null) {
                permissionMetaResource.setKeepAlive(permission.getKeepAlive());
            }

            //Outer chain menu opening method
            if (permission.getInternalOrExternal() != null) {
                permissionMetaResource.setInternalOrExternal(permission.getInternalOrExternal());
            }

            permissionMetaResource.setTitle(permission.getName());
            if (permission.getParentId() != null) {
                // first level menu jump address
                menu.setRedirect(permission.getRedirect());
                if (!StringUtils.isBlank(permission.getIcon())) {
                    permissionMetaResource.setIcon(permission.getIcon());
                }
            } else {
                if (!StringUtils.isBlank(permission.getIcon())) {
                    permissionMetaResource.setIcon(permission.getIcon());
                }
            }
            permissionMetaResource.setUrl(permission.getUrl());
            menu.setMeta(permissionMetaResource);
        }
        return menu;
    }

    private void getPermissionJsonArray(List<Menu> menus, List<Permission> permissions, Menu parentJson) {
        for (Permission permission : permissions) {
            if (permission.getMenuType() == null) {
                continue;
            }
            Long tempPid = permission.getParentId();
            Menu menu = getPermissionJsonObject(permission);
            if (menu == null) {
                continue;
            }
            if (parentJson == null && tempPid == null) {
                menus.add(menu);
                if (!permission.getLeaf()) {
                    getPermissionJsonArray(menus, permissions, menu);
                }
            } else if (parentJson != null && tempPid != null && tempPid.equals(parentJson.getId())) {
                // type 0 first level menu 1 sub menu 2 button
                if (permission.getMenuType() == Constant.MENU_TYPE_BUTTON) {
                    PermissionMetaResource permissionMetaResourceJson = parentJson.getMeta();
                    if (permissionMetaResourceJson.getPermissionList() != null) {
                        permissionMetaResourceJson.getPermissionList().add(menu);
                    } else {
                        List<Menu> permissionList = new ArrayList<>();
                        permissionList.add(menu);
                        permissionMetaResourceJson.setPermissionList(permissionList);
                    }
                    // type 0 first level menu 1 sub menu 2 button
                } else if (permission.getMenuType() == Constant.MENU_TYPE_SUB_MENU ||
                           permission.getMenuType() == Constant.MENU_TYPE_MENU) {
                    if (parentJson.getChildren() != null) {
                        parentJson.getChildren().add(menu);
                    } else {
                        List<Menu> children = new ArrayList<>();
                        children.add(menu);
                        parentJson.setChildren(children);
                    }

                    if (!permission.getLeaf()) {
                        getPermissionJsonArray(menus, permissions, menu);
                    }
                }
            }
        }
    }

    /**
     * Generate a route name from the URL (remove the URL prefix slash and replace the slash '/' in the content with-)
     * Example: URL = /system/role -> RouteName = /system-role
     */
    private String urlToRouteName(String url) {
        if (!StringUtils.isBlank(url)) {
            if (url.startsWith("/")) {
                url = url.substring(1);
            }
            url = url.replace("/", "-");

            // special mark
            url = url.replace(":", "@");
            return url;
        } else {
            return null;
        }
    }
}
