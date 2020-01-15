package com.odakota.tms.system.config.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.odakota.tms.business.auth.repository.AccessTokenRepository;
import com.odakota.tms.business.auth.repository.PermissionRoleRepository;
import com.odakota.tms.constant.FieldConstant;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.enums.ApiId;
import com.odakota.tms.system.annotations.NoAuthentication;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.config.UserSession;
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
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author haidv
 * @version 1.0
 */
@Slf4j
@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    private final TokenProvider tokenProvider;

    private final UserSession userSession;

    private final AccessTokenRepository accessTokenRepository;

    private final PermissionRoleRepository permissionRoleRepository;

    @Autowired
    public AuthenticationInterceptor(TokenProvider tokenProvider, UserSession userSession,
                                     AccessTokenRepository accessTokenRepository,
                                     PermissionRoleRepository permissionRoleRepository) {
        this.tokenProvider = tokenProvider;
        this.userSession = userSession;
        this.accessTokenRepository = accessTokenRepository;
        this.permissionRoleRepository = permissionRoleRepository;
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
                                @Nullable Exception ex) {
        logResponse(response);
    }

    private boolean validatePrivateApi(Method method, String token) throws UnAuthorizedException {

        // parse info of token to user session
        tokenProvider.parseTokenInfoToUserSession(token);
        // check token exist in db
        if (!accessTokenRepository.existsByJti(userSession.getTokenId())) {
            throw new UnAuthorizedException(MessageCode.MSG_TOKEN_INVALID, HttpStatus.UNAUTHORIZED);
        }
        for (Annotation annotation : method.getDeclaredAnnotations()) {
            if (annotation instanceof RequiredAuthentication) {
                ApiId apiId = ((RequiredAuthentication) annotation).value();
                if (ApiId.DEFAULT.getValue().equals(apiId.getValue())) {
                    return true;
                }
                // Check request has permission
                if (!permissionRoleRepository.existsByApiIdAndRoleIdIn(apiId.getValue(), userSession.getRoleIds())) {
                    throw new UnAuthorizedException(MessageCode.MSG_ACCESS_DENIED, HttpStatus.FORBIDDEN);
                }
                return true;
            }
        }
        return true;
    }

    /**
     * Logging request
     *
     * @param request HttpServletRequest
     */
    private void logRequest(HttpServletRequest request) {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        String requestId = request.getHeader(FieldConstant.REQUEST_ID);
        ThreadContext.put(FieldConstant.REQUEST_ID, requestId);
        ThreadContext.put(FieldConstant.START_TIME_KEY, String.valueOf(System.currentTimeMillis()));
        log.info("---------------------------------------------------------------------------------------");
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode parentNode = objectMapper.createObjectNode();
        parentNode.put("URI", requestWrapper.getRequestURI());
        parentNode.put("Method", request.getMethod());
        parentNode.put("Remote Address", requestWrapper.getRemoteAddr());
        parentNode.put("Request Headers", objectMapper.valueToTree(getRequestHeaders(requestWrapper)));
        log.info(parentNode.toString());
    }

    /**
     * Logging result of request
     *
     * @param response HttpServletResponse
     */
    private void logResponse(HttpServletResponse response) {
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode parentNode = objectMapper.createObjectNode();
        parentNode.put("HttpStatus", responseWrapper.getStatus());
        parentNode
                .put("TakeTime",
                     System.currentTimeMillis() - Long.parseLong(ThreadContext.get(FieldConstant.START_TIME_KEY)) +
                     "ms");
        parentNode.put("Response Headers", objectMapper.valueToTree(getResponseHeaders(responseWrapper)));
        log.info(parentNode.toString());
        log.info("--------------------------------------------------------------------------------------");
        ThreadContext.remove(FieldConstant.REQUEST_ID);
        ThreadContext.remove(FieldConstant.START_TIME_KEY);
    }

    private Object getRequestHeaders(HttpServletRequest request) {
        Map<String, Object> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (headerName.equalsIgnoreCase("Authorization")) {
                headers.put(headerName, "<<Not log record>>");
                continue;
            }
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
