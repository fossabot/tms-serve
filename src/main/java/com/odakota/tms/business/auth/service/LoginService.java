package com.odakota.tms.business.auth.service;

import com.odakota.tms.business.auth.entity.AccessToken;
import com.odakota.tms.business.auth.entity.Permission;
import com.odakota.tms.business.auth.entity.User;
import com.odakota.tms.business.auth.mapper.AuthMapper;
import com.odakota.tms.business.auth.repository.AccessTokenRepository;
import com.odakota.tms.business.auth.repository.UserRepository;
import com.odakota.tms.business.auth.resource.LoginResource;
import com.odakota.tms.business.auth.resource.LoginResponse;
import com.odakota.tms.business.auth.resource.ResetPasswordResource;
import com.odakota.tms.business.auth.resource.userpermission.Auth;
import com.odakota.tms.business.auth.resource.userpermission.Menu;
import com.odakota.tms.business.auth.resource.userpermission.PermissionMetaResource;
import com.odakota.tms.business.auth.resource.userpermission.UserPermissionResource;
import com.odakota.tms.constant.Constant;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.constant.SmsMessageConstant;
import com.odakota.tms.enums.auth.Client;
import com.odakota.tms.enums.auth.LoginType;
import com.odakota.tms.enums.auth.SmsType;
import com.odakota.tms.enums.auth.TokenType;
import com.odakota.tms.system.config.UserSession;
import com.odakota.tms.system.config.exception.CustomException;
import com.odakota.tms.system.config.interceptor.TokenProvider;
import com.odakota.tms.system.service.OtpGenerator;
import com.odakota.tms.system.service.sns.SmsService;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
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

    private final UserRoleService userRoleService;

    private final AccessTokenRepository accessTokenRepository;

    private final UserSession userSession;

    private final SmsService smsService;

    private AuthMapper mapper = Mappers.getMapper(AuthMapper.class);

    @Autowired
    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider,
                        PermissionService permissionService, OtpGenerator otpGenerator,
                        UserRoleService userRoleService,
                        AccessTokenRepository accessTokenRepository,
                        UserSession userSession, SmsService smsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.permissionService = permissionService;
        this.otpGenerator = otpGenerator;
        this.userRoleService = userRoleService;
        this.accessTokenRepository = accessTokenRepository;
        this.userSession = userSession;
        this.smsService = smsService;
    }

    /**
     * Login
     *
     * @param loginResource LoginResource
     * @return Object
     */
    public Object login(LoginResource loginResource, Client client, LoginType loginType) {
        // check captcha
        String key = this.checkLoginCaptcha(loginResource, loginType);
        LoginResponse response = new LoginResponse();
        List<Long> roleIds = null;
        Long id;
        User user = null;
        if (client.equals(Client.ADMIN)) {
            user = this.validateUser(loginResource, loginType);
            // get list role id
            roleIds = userRoleService.getUserRoleIds(user.getId());
            id = user.getId();
        } else {
            id = (long) 1;
        }
        String jti = UUID.randomUUID().toString();
        String refreshJti = UUID.randomUUID().toString();
        // save information to access token
        AccessToken accessToken = new AccessToken();
        accessToken.setJti(jti);
        accessToken.setRefreshJti(refreshJti);
        accessToken.setCreateDate(new Date());
        accessToken.setClient(client.name());
        accessToken.setUserId(id);
        accessTokenRepository.save(accessToken);
        Map<String, Object> map = new HashMap<>();
        map.put(Constant.TOKEN_CLAIM_USER_ID, id);
        map.put(Constant.TOKEN_CLAIM_ROLE_ID, StringUtils.join(roleIds, ","));
        if (client.equals(Client.ADMIN)) {
            map.put(Constant.TOKEN_CLAIM_BRANCH_ID, user.getBranchId());
            map.put(Constant.TOKEN_CLAIM_BRAND_ID, user.getBrandId());
            // generate token
            response.setToken(tokenProvider.generateToken(TokenType.ACCESS, Client.ADMIN, jti, map));
            response.setRefresh(tokenProvider.generateToken(TokenType.REFRESH, Client.ADMIN, refreshJti, map));
            user.setPassword(null);
            response.setUserInfo(mapper.convertToResource(user));
        } else {
            // generate token
            response.setToken(tokenProvider.generateToken(TokenType.ACCESS, Client.CUSTOMER, jti, map));
            response.setRefresh(tokenProvider.generateToken(TokenType.REFRESH, Client.CUSTOMER, refreshJti, map));
        }
        // clear cache
        otpGenerator.clearOTPFromCache(key);
        return response;
    }


    /**
     * logout
     */
    public void logout() {
        accessTokenRepository.deleteAccessTokenByJti(userSession.getTokenId());
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
    public void sendSmsOTP(String phone, SmsType smsType) {
        String code;
        String message = null;
        if (smsType.equals(SmsType.LOGIN)) {
            code = otpGenerator.generateOTP(Constant.LOGIN_PHONE_PREFIX_KEY + phone);
            message = SmsMessageConstant.SMS_LOGIN + code;
        }
        if (smsType.equals(SmsType.FORGOT)) {
            code = otpGenerator.generateOTP(Constant.FORGOT_PASS_PREFIX_KEY + phone);
            message = SmsMessageConstant.SMS_FORGOT + code;
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

    /**
     * Check login captcha to match
     *
     * @param loginResource LoginResource
     * @param loginType     loginType
     */
    private String checkLoginCaptcha(LoginResource loginResource, LoginType loginType) {
        String key;
        if (loginType.equals(LoginType.ACCOUNT)) {
            key = loginResource.getCheckKey();
        } else {
            key = Constant.LOGIN_PHONE_PREFIX_KEY + loginResource.getPhone();
        }
        // check captcha
        validateOtp(key, loginResource.getCaptcha());
        return key;
    }

    /**
     * Check forgot password captcha to match
     *
     * @param phone   String
     * @param captcha String
     */
    public void checkForGotCaptcha(String phone, String captcha) {
        String key = Constant.FORGOT_PASS_PREFIX_KEY + phone;
        // check captcha
        validateOtp(key, captcha);
        // clear cache
        otpGenerator.clearOTPFromCache(key);
    }

    /**
     * Reset pass
     *
     * @param resource {@link ResetPasswordResource}
     */
    public User resetPassword(ResetPasswordResource resource) {
        User user = userRepository
                .findByUsernameAndDeletedFlagFalse(resource.getUsername())
                .orElseThrow(() -> new CustomException(MessageCode.MSG_INVALID_USERNAME_PASS, HttpStatus.BAD_REQUEST));
        user.setPassword(passwordEncoder.encode(resource.getPassword()));
        user = userRepository.save(user);
        return user;
    }

    /**
     * Validate OTP to match
     *
     * @param key String
     * @param otp String
     */
    private void validateOtp(String key, String otp) {
        Map<String, Object> map = otpGenerator.getOPTByKey(key);
        if (map == null || map.size() == 0) {
            throw new CustomException(MessageCode.MSG_CAPTCHA_EXPIRED, HttpStatus.BAD_REQUEST);
        }
        if (!map.get(Constant.OTP_CODE_OTP).equals(otp)) {
            throw new CustomException(MessageCode.MSG_CAPTCHA_INVALID, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Validate user login admin site
     *
     * @param loginResource LoginResource
     * @param loginType     LoginType
     * @return User
     */
    private User validateUser(LoginResource loginResource, LoginType loginType) {
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
        return user;
    }
}
