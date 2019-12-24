package com.odakota.tms.constant;

/**
 * @author haidv
 * @version 1.0
 */
public class Constant {

    // DEFAULT VALUE
    public static final int ROLE_ID_DEFAULT = 1;
    public static final int USER_ID_DEFAULT = 1;
    public static final int BRANCH_ID_DEFAULT = 1;
    public static final int NUMBER_OF_ROLE_DEFAULT = 2;
    public static final int NUMBER_OF_ACCOUNT_DEFAULT = 2;
    public static final String IMAGE_PATH_DEFAULT = "/avatar/default.jpg";
    public static final int MENU_TYPE_SUB_MENU = 1;
    public static final int MENU_TYPE_MENU = 0;
    public static final int MENU_TYPE_BUTTON = 2;
    // OPERATION FILTER
    public static final String OPERATION_EQUAL = "=";
    public static final String OPERATION_NOT_EQUAL = "!=";
    public static final String OPERATION_LIKE = "like";
    // TOKEN
    public static final String TOKEN_CLAIM_USER_ID = "userId";
    public static final String TOKEN_CLAIM_ROLE_ID = "roleIds";
    public static final String TOKEN_CLAIM_USER_NAME = "username";
    public static final String TOKEN_CLAIM_JTI = "jti";
    // DATETIME FORMAT
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DD_MM_YYYY_HH_MM_SS = "dd-MM-yyyy hh:mm:ss";
    // JOB TASK NAME
    public static final String IMPORT_USER = "JTN0001";
    public static final String JTN0001_PATH_FILE = "file-path";
    // SUBJECT EMAIL
    public static final String SUBJECT_CREATE_ACCOUNT = "Login information";
    public static final String SUBJECT_OTP_CONFIRM = "Verify email";
    // OTP PREFIX KEY
    public static final String LOGIN_PHONE_PREFIX_KEY = "login-otp-";
    public static final String FORGOT_PASS_PREFIX_KEY = "forgot-otp-";
    // OTP FIELD NAME
    public static final String OTP_CODE_OTP = "otp";
    public static final String OTP_DATA = "data";

    Constant() {
    }
}
