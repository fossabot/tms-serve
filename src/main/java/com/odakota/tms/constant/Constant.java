package com.odakota.tms.constant;

/**
 * @author haidv
 * @version 1.0
 */
public class Constant {

    Constant() {
    }

    // DEFAULT VALUE
    public static final int ROLE_ID_DEFAULT = 1;

    public static final int ACCOUNT_ID_DEFAULT = 1;

    public static final int NUMBER_OF_ROLE_DEFAULT = 2;

    public static final int NUMBER_OF_ACCOUNT_DEFAULT = 2;

    public static final String IMAGE_PATH_DEFAULT = "/avatar/default.jpg";

    // OPERATION FILTER
    public static final String OPERATION_EQUAL = "=";

    public static final String OPERATION_NOT_EQUAL = "!=";

    public static final String OPERATION_LIKE = "like";

    // TOKEN
    public static final String TOKEN_CLAIM_USER_ID = "userId";

    public static final String TOKEN_CLAIM_USER_NAME = "username";

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
    public static final String RESET_PASS_PREFIX_KEY = "reset-pass-";

    // OTP FIELD NAME
    public static final String OTP_CODE_OTP = "otp";

    public static final String OTP_DATA = "data";

    // STACK_TRACE_ELEMENT FIELD NAME
    public static final String STACK_TRACE_TYPE = "type";

    public static final String STACK_TRACE = "stacktrace";

    // ROLE FIELD NAME
    public static final String ROLE_NAME = "roleName";

    // DIVISION FIELD NAME
    public static final String DIVISION_NAME = "name";

    // USER FIELD NAME
    public static final String USER_IDENTITY_CARD = "identityCard";

    public static final String USER_PASSPORT = "passport";

    public static final String USER_DIVISION_ID = "divisionId";

    public static final String USER_FULL_NAME = "fullName";

    // ACCOUNT FIELD NAME
    public static final String ACCOUNT_USER_NAME = "userName";

    public static final String ACCOUNT_PASS = "password";

    // WORK FIELD NAME
    public static final String USER_ID = "userId";

    public static final String WORK_EMAIL = "emailEnterprise";

    // CONTRACT FIELD NAME
    public static final String END_DATE = "endDate";

    public static final String ID = "id";
}
