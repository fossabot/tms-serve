package com.odakota.tms.system.config.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odakota.tms.constant.FieldConstant;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.system.annotations.NoAuthentication;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.config.exception.UnAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author haidv
 * @version 1.0
 */
@Slf4j
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
        logRequest(request);
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

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws IOException {
        logResponse(response);
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

    /**
     * Logging request
     *
     * @param request HttpServletRequest
     */
    private void logRequest(HttpServletRequest request) {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        String requestId = UUID.randomUUID().toString();
        ThreadContext.put(FieldConstant.REQUEST_ID, requestId);
        ThreadContext.put("startTime", String.valueOf(System.currentTimeMillis()));
        log.info("-----------------------------------logging request-----------------------------------");
        log.info("RequestId      : {}", requestId);
        log.info("URI            : {}", requestWrapper.getRequestURI());
        log.info("Method         : {}", request.getMethod());
        log.info("Request Headers: {}", new ObjectMapper().valueToTree(getRequestHeaders(requestWrapper)));
        log.info("Remote Address : {}", requestWrapper.getRemoteAddr());
        log.info("-------------------------------------------------------------------------------------");
    }

    /**
     * Logging result of request
     *
     * @param response HttpServletResponse
     */
    private void logResponse(HttpServletResponse response) {
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        log.info("-----------------------------------logging response-----------------------------------");
        log.info("RequestId       : {}", ThreadContext.get(FieldConstant.REQUEST_ID));
        log.info("HttpStatus      : {}", responseWrapper.getStatus());
        log.info("Response Headers: {}", new ObjectMapper().valueToTree(getResponseHeaders(responseWrapper)));
        log.info("TakeTime        : {}ms", System.currentTimeMillis() - Long.parseLong(ThreadContext.get("startTime")));
        log.info("--------------------------------------------------------------------------------------");
        ThreadContext.remove(FieldConstant.REQUEST_ID);
    }

    private Object getRequestHeaders(HttpServletRequest request) {
        Map<String, Object> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;

    }

    private Map<String, Object> getResponseHeaders(ContentCachingResponseWrapper response) {
        Map<String, Object> headers = new HashMap<>();
        Collection<String> headerNames = response.getHeaderNames();
        for (String headerName : headerNames) {
            headers.put(headerName, response.getHeader(headerName));
        }
        return headers;
    }
}
