package com.odakota.tms.constant;

/**
 * Message resource class for API
 *
 * @author haidv
 * @version 1.0
 */
public class MessageCode {

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
    public static final String MSG_METHOD_NOT_SUPPORT = "method.not.support";
    // Field error code
    public static final String MSG_REQUIRED = "ng.general.required";
    public static final String MSG_PHONE = "ng.general.phone.valid";
    public static final String MSG_EMAIL = "ng.general.email.valid";
    public static final String MSG_MAX_LENGTH = "ng.general.greater.than.max.length";
    public static final String MSG_NOT_MATCHED = "ng.general.not.matched";
    // Business error code
    public static final String MSG_RESOURCE_NOT_EXIST = "resource.not.exist";
    public static final String MSG_INVALID_USERNAME_PASS = "invalid.username.password";
    public static final String MSG_ACCOUNT_DISABLED = "account.disabled";
    public static final String MSG_USER_NOT_DELETED = "user.not.deleted";
    public static final String MSG_USER_NOT_UPDATED = "user.not.updated";
    public static final String MSG_PHONE_EXISTED = "phone.existed";
    public static final String MSG_EMAIL_EXISTED = "email.existed";
    public static final String MSG_USER_NAME_EXISTED = "username.existed";
    public static final String MSG_ROLE_CODE_EXISTED = "role.code.existed";
    public static final String MSG_ROLE_NOT_DELETED = "role.not.deleted";
    public static final String MSG_ROLE_NOT_UPDATED = "role.not.updated";
    public static final String MSG_CAPTCHA_EXPIRED = "captcha.expired";
    public static final String MSG_CAPTCHA_INVALID = "captcha.invalid";
    public static final String MSG_DOWNLOAD_S3_FAIL = "download.s3.fail";
    public static final String MSG_UPLOAD_S3_FAIL = "upload.s3.fail";
    public static final String MSG_DELETE_S3_FAIL = "delete.s3.fail";
    public static final String MSG_COPY_S3_FAIL = "copy.s3.fail";
    public static final String MSG_FILE_NOT_FOUND = "file.not.existed";
    public static final String MSG_IMAGE_MAX_SIZE = "image.max.size";
    public static final String MSG_INVALID_IMAGE_TYPE = "invalid.image-type";
    public static final String MSG_BRANCH_CODE_EXISTED = "branch.code.existed";
    public static final String MSG_BRANCH_NOT_DELETED = "branch.not.deleted";
    public static final String MSG_BRAND_NOT_DELETED = "brand.not.deleted";
    public static final String MSG_BRAND_NOT_EXISTED = "brand.not.existed";

    MessageCode() {
    }
}
