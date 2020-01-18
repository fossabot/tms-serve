package com.odakota.tms.system.annotations;

import com.odakota.tms.enums.auth.ApiId;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotated elements are in will check api access.
 *
 * @author haidv
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredAuthentication {

    ApiId value() default ApiId.DEFAULT;
}