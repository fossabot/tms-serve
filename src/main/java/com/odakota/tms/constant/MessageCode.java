package com.odakota.tms.constant;

/**
 * Message resource class for API
 *
 * @author haidv
 * @version 1.0
 */
public class MessageCode {

    MessageCode() {
    }

    // Common error
    public static final String MSG_VALIDATION = "validation.error";

    public static final String MSG_REQUEST_PARAM_ERROR = "request.param.error";

    public static final String MSG_JSON_MALFORMED = "json.malformed";

    public static final String MSG_JSON_WRITING_ERROR = "json.writing.error";

    public static final String MSG_NOT_FOUND_URL = "not.found.url";

    public static final String MSG_NOT_ACCEPTABLE = "not.acceptable";

    public static final String MSG_REQUEST_PARAM_MISTING = "request.param.misting";

    public static final String MSG_DATABASE_ERROR = "database.error";

    public static final String MSG_REQ_PARAM_NOT_MATCH_TYPE = "request.param.not.match.type";

    public static final String MSG_RUNTIME_EXCEPTION = "runtime.exception";

    public static final String MSG_SEND_MAIL_ERROR = "send.mail.error";

    public static final String MSG_INVALID_OTP = "invalid.verify.otp.code";

    public static final String MSG_OTP_EXPIRED = "otp.expired";

    public static final String MSG_TOKEN_AUTH_ERROR = "token.auth.error";

    public static final String MSG_CODE_NOT_USE = "code.not.use";

    public static final String MSG_TOKEN_NOT_EXISTED = "token.not.exist";

    public static final String MSG_ACCESS_DENIED = "access.denied";

    public static final String MSG_TOKEN_INVALID = "token.invalid";

    public static final String MSG_TOKEN_EXPIRED = "token.expired";

    // Field error code
    public static final String MSG_REQUIRED = "ng.general.required";

    public static final String MSG_NOT_EXISTS = "ng.general.not-existed";

    public static final String MSG_DUPLICATED = "ng.general.duplicated";

    public static final String MSG_NOT_EQUAL = "ng.general.not-equals";

    public static final String MSG_PHONE = "ng.general.phone-valid";

    public static final String MSG_EMAIL = "ng.general.email-valid";

    public static final String MSG_MAX_LENGTH = "ng.general.greater-than-max-length";

    public static final String MSG_NOT_MATCHED = "ng.general.not-matched";

    // Business error code
    public static final String MSG_RESOURCE_NOT_EXIST = "resource.not.exist";

    public static final String MSG_RESOURCE_NOT_EXIST_BY_ID = "resource.not.exist.by.id";

    public static final String MSG_INVALID_USERNAME_PASS = "invalid.username.password";

    public static final String MSG_ACCOUNT_DISABLED= "account.disabled";

    public static final String MSG_LOGIN_FAILED_TOO_MANY= "login.failed.too.many";

    public static final String MSG_ROLE_NOT_UPDATED= "role.not.updated";

    public static final String MSG_ROLE_NOT_DELETED= "role.not.deleted";

    public static final String MSG_DIVISION_NOT_DELETED= "division.not.deleted";

    public static final String MSG_USER_NOT_DELETED= "user.not.deleted";

    public static final String MSG_END_DATE_LESS_THAN= "contract.end.date.less.than.start.date";
}
