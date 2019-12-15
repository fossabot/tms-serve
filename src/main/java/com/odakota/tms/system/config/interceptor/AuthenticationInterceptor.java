package com.odakota.tms.system.config.interceptor;

import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.system.annotations.NoAuthentication;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.config.exception.UnAuthorizedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author haidv
 * @version 1.0
 */
@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    private final TokenProvider tokenProvider;

    @Autowired
    public AuthenticationInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // reject if not accept header
        if (StringUtils.isEmpty(request.getHeader("Accept"))) {
            throw new HttpMediaTypeNotAcceptableException("");
        }
        String authHeader = request.getHeader("Authorization");
        Method method = ((HandlerMethod) handler).getMethod();
        // Allow for accessing public APIs
        if (method.isAnnotationPresent(NoAuthentication.class)) {
            return true;
        }
        // Validate all private APIs
        if (method.isAnnotationPresent(RequiredAuthentication.class)) {
            if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer")) {
                throw new UnAuthorizedException(MessageCode.MSG_TOKEN_AUTH_ERROR, HttpStatus.UNAUTHORIZED);
            } else {
                return validatePrivateApi(method, authHeader.replace("Bearer ", ""));
            }
        }
        // If request doesn't match with any handle method, it will be reject
        throw new UnAuthorizedException(MessageCode.MSG_CODE_NOT_USE, HttpStatus.FORBIDDEN);
    }

    private boolean validatePrivateApi(Method method, String token) throws UnAuthorizedException {

//        // check token exist in db
//        if (!tokenService.existsByToken(token)) {
//            throw new UnAuthorizedException(getMessage(MessageCode.MSG_TOKEN_NOT_EXISTED), HttpStatus.UNAUTHORIZED);
//        }
        // parse info of token to user session
        tokenProvider.parseTokenInfoToUserSession(token);
//        for (Annotation annotation : method.getDeclaredAnnotations()) {
//            if (annotation instanceof RequiredAuthentication) {
//                ApiId apiId = ((RequiredAuthentication) annotation).value();
//                if (ApiId.DEFAULT.getValue().equals(apiId.getValue())) {
//                    return true;
//                }
//                // Check request has permission
//                if (!permissionRoleService.checkPermissionOfApi(apiId, userSession.getRoleId())) {
//                    throw new UnAuthorizedException(getMessage(MessageCode.MSG_ACCESS_DENIED), HttpStatus.FORBIDDEN);
//                }
//                return true;
//            }
//        }
        return true;
    }
}
