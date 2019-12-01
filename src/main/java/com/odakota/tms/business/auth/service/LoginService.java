package com.odakota.tms.business.auth.service;

import com.odakota.tms.business.auth.entity.Permission;
import com.odakota.tms.business.auth.entity.User;
import com.odakota.tms.business.auth.repository.UserRepository;
import com.odakota.tms.business.auth.resource.*;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.system.config.exception.CustomException;
import com.odakota.tms.system.config.interceptor.TokenProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                        TokenProvider tokenProvider, PermissionService permissionService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.permissionService = permissionService;
    }

    public Object login(LoginResource loginResource) {
        // check capcha

        User user = userRepository.findByUsername(loginResource.getUsername())
                                  .orElseThrow(() -> new CustomException(MessageCode.MSG_INVALID_USERNAME_PASS,
                                                                         HttpStatus.BAD_REQUEST));

        if (!passwordEncoder.matches(loginResource.getPassword(), user.getPassword())) {
            throw new CustomException(MessageCode.MSG_INVALID_USERNAME_PASS, HttpStatus.BAD_REQUEST);
        }
        if (user.isDisableFlag()) {
            throw new CustomException(MessageCode.MSG_ACCOUNT_DISABLED, HttpStatus.CONFLICT);
        }
        String token = tokenProvider.generateToken(user.getId(), user.getUsername());

        loginResource = new LoginResource();
        loginResource.setToken(token);
        loginResource.setUserInfo(user);
        return loginResource;
    }

//    public JsonObject getPermissions(Long userId) {
//        List<Permission> permissions = permissionService.getResources(userId);
//        JsonObject jsonObject = new JsonObject();
//        JsonArray menuJsonArray = new JsonArray();
//        JsonArray authJsonArray = new JsonArray();
//        JsonArray allAuthJsonArray = new JsonArray();
//        List<Permission> allAuthList = permissionService.getResources(2);
//        this.getAllAuthJsonArray(allAuthJsonArray, allAuthList);
//        this.getAuthJsonArray(authJsonArray, permissions);
//        this.getPermissionJsonArray(menuJsonArray, permissions, null);
//        //Routing menu
//        jsonObject.add("menu", menuJsonArray);
//        //Button permissions
//        jsonObject.add("auth", authJsonArray);
//        //All permissions configuration (button permissions, access permissions)
//        jsonObject.add("allAuth", allAuthJsonArray);
//        return jsonObject;
//    }

    public Example getPermissions(Long userId) {
        List<Permission> permissions = permissionService.getResources(userId);
        Example example = new Example();
        List<Menu> menus = new ArrayList<>();
        List<Auth> auths = new ArrayList<>();
        List<Auth> allAuths = new ArrayList<>();
        List<Permission> allAuthList = permissionService.getResources(2);
        this.getAllAuthJsonArray(allAuths, allAuthList);
        this.getAuthJsonArray(auths, permissions);
        this.getPermissionJsonArray(menus, permissions, null);
        //Routing menu
        example.setMenu(menus);
        //All permissions configuration (button permissions, access permissions)
        example.setAllAuth(allAuths);
        //Button permissions
        example.setAuth(auths);
        return example;
    }

    private void getAuthJsonArray(List<Auth> auths, List<Permission> permissions) {
        for (Permission permission : permissions) {
            if (permission.getMenuType() == null) {
                continue;
            }
            Auth auth;
            if (permission.getMenuType() == 2 && permission.getStatus()) {
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


//    private void getAuthJsonArray(JsonArray jsonArray, List<Permission> permissions) {
//        for (Permission permission : permissions) {
//            if (permission.getMenuType() == null) {
//                continue;
//            }
//            JsonObject json;
//            if (permission.getMenuType() == 2 && permission.getStatus()) {
//                json = new JsonObject();
//                json.addProperty("action", permission.getPerms());
//                json.addProperty("type", permission.getPermsType());
//                json.addProperty("describe", permission.getName());
//                jsonArray.add(json);
//            }
//        }
//    }
//
//    private void getAllAuthJsonArray(JsonArray jsonArray, List<Permission> permissions) {
//        JsonObject json;
//        for (Permission permission : permissions) {
//            json = new JsonObject();
//            json.addProperty("action", permission.getPerms());
//            json.addProperty("status", permission.getStatus());
//            json.addProperty("type", permission.getPermsType());
//            json.addProperty("describe", permission.getName());
//            jsonArray.add(json);
//        }
//    }
//
//    private JsonObject getPermissionJsonObject(Permission permission) {
//        JsonObject json = new JsonObject();
//       // type (0: first-level menu 1: sub-menu 2: button)
//        if (permission.getMenuType() == 2) {
//            return null;
//        } else if (permission.getMenuType() == 0 || permission.getMenuType() == 1) {
//            json.addProperty("id", permission.getId());
//            if (permission.getRoute()) {
//                json.addProperty("route", 1);// generate route
//            } else {
//                json.addProperty("route", 0);// indicates that no route is generated
//            }
//
//            json.addProperty("path", permission.getUrl());
//
//            // Important rules: route name (generate route name by URL, route name for front-end development, page jump use)
//            if (!StringUtils.isBlank(permission.getComponentName())) {
//                json.addProperty("name", permission.getComponentName());
//            } else {
//                json.addProperty("name", urlToRouteName(permission.getUrl()));
//            }
//
//            // Whether to hide the route, the default is displayed
//            if (permission.getHidden() == null) {
//                json.addProperty("hidden", true);
//            } else {
//                json.addProperty("hidden", permission.getHidden());
//            }
//            // Aggregate route
//            if (permission.getAlwaysShow() == null) {
//                json.addProperty("alwaysShow", true);
//            } else {
//                json.addProperty("alwaysShow", permission.getAlwaysShow());
//            }
//            json.addProperty("component", permission.getComponent());
//            JsonObject meta = new JsonObject();
//            // Set by the user whether to cache the page with boolean
//            if (permission.getKeepAlive() == null) {
//                meta.addProperty("keepAlive", true);
//            } else {
//                meta.addProperty("keepAlive", permission.getKeepAlive());
//            }
//
//            //Outer chain menu opening method
//            if (permission.getInternalOrExternal() == null) {
//                meta.addProperty("internalOrExternal", true);
//            } else {
//                meta.addProperty("internalOrExternal", permission.getInternalOrExternal());
//            }
//
//            meta.addProperty("title", permission.getName());
//            if (permission.getParentId() != null) {
//                // first level menu jump address
//                json.addProperty("redirect", permission.getRedirect());
//                if (!StringUtils.isBlank(permission.getIcon())) {
//                    meta.addProperty("icon", permission.getIcon());
//                }
//            } else {
//                if (!StringUtils.isBlank(permission.getIcon())) {
//                    meta.addProperty("icon", permission.getIcon());
//                }
//            }
//            meta.addProperty("url", permission.getUrl());
//            json.add("meta", meta);
//        }
//
//        return json;
//    }

    private Menu getPermissionJsonObject(Permission permission) {
        Menu menu = new Menu();
        // type (0: first-level menu 1: sub-menu 2: button)
        if (permission.getMenuType() == 2) {
            return null;
        } else if (permission.getMenuType() == 0 || permission.getMenuType() == 1) {
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
            Meta meta = new Meta();
            // Set by the user whether to cache the page with boolean
            if (permission.getKeepAlive() != null) {
                meta.setKeepAlive(permission.getKeepAlive());
            }

            //Outer chain menu opening method
            if (permission.getInternalOrExternal() != null) {
                meta.setInternalOrExternal(permission.getInternalOrExternal());
            }

            meta.setTitle(permission.getName());
            if (permission.getParentId() != null) {
                // first level menu jump address
                menu.setRedirect(permission.getRedirect());
                if (!StringUtils.isBlank(permission.getIcon())) {
                    meta.setIcon(permission.getIcon());
                }
            } else {
                if (!StringUtils.isBlank(permission.getIcon())) {
                    meta.setIcon(permission.getIcon());
                }
            }
            meta.setUrl(permission.getUrl());
            menu.setMeta(meta);
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
                if (permission.getMenuType() == 2) {
                    Meta metaJson = parentJson.getMeta();
                    if (metaJson.getPermissionList() != null) {
                        metaJson.getPermissionList().add(menu);
                    } else {
                        List<Menu> permissionList = new ArrayList<>();
                        permissionList.add(menu);
                        metaJson.setPermissionList(permissionList);
                    }
                    // type 0 first level menu 1 sub menu 2 button
                } else if (permission.getMenuType() == 1 || permission.getMenuType() == 0) {
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

//    private void getPermissionJsonArray(JsonArray jsonArray, List<Permission> permissions, JsonObject parentJson) {
//        for (Permission permission : permissions) {
//            if (permission.getMenuType() == null) {
//                continue;
//            }
//            Long tempPid = permission.getParentId();
//            JsonObject json = getPermissionJsonObject(permission);
//            if (json == null) {
//                continue;
//            }
//            if (parentJson == null && tempPid == null) {
//                jsonArray.add(json);
//                if (!permission.getLeaf()) {
//                    getPermissionJsonArray(jsonArray, permissions, json);
//                }
//            } else if (parentJson != null && tempPid != null && tempPid.equals(parentJson.get("id").getAsLong())) {
//                // type 0 first level menu 1 sub menu 2 button
//                if (permission.getMenuType() == 2) {
//                    JsonObject metaJson = parentJson.getAsJsonObject("meta");
//                    if (metaJson.has("permissionList")) {
//                        metaJson.getAsJsonArray("permissionList").add(json);
//                    } else {
//                        JsonArray permissionList = new JsonArray();
//                        permissionList.add(json);
//                        metaJson.add("permissionList", permissionList);
//                    }
//                    // type 0 first level menu 1 sub menu 2 button
//                } else if (permission.getMenuType() == 1 || permission.getMenuType() == 0) {
//                    if (parentJson.has("children")) {
//                        parentJson.getAsJsonArray("children").add(json);
//                    } else {
//                        JsonArray children = new JsonArray();
//                        children.add(json);
//                        parentJson.add("children", children);
//                    }
//
//                    if (!permission.getLeaf()) {
//                        getPermissionJsonArray(jsonArray, permissions, json);
//                    }
//                }
//            }
//        }
//    }

    /**
     * Generate a route name from the URL (remove the URL prefix slash and replace the slash '/' in the content with-)
     * Example: URL = /system/role -> RouteName = /system-role
     *
     * @return
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
