package com.odakota.tms.constant;

/**
 * Constant definition class for API
 *
 * @author haidv
 * @version 1.0
 */
public class RegexConstant {

    public static final String REGEX_NUMERIC = "^([-+]?[0-9]+)$";
    public static final String REGEX_POSITIVE_NUMERIC = "^[0-9]*$";
    public static final String REGEX_DECIMAL = "^([-+]?[0-9]+([.]{1}[0-9]+)?)$";
    public static final String REGEX_POSITIVE_DECIMAL = "^([0-9]+([.]{1}[0-9]+)?)$";
    public static final String REGEX_ALPHABET_NUMBER = "^[a-zA-Z0-9]*$";
    public static final String REGEX_PHONE_NO = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[- /0-9]*$";
    public static final String REGEX_INTERNATIONAL_PHONE = "^\\+(?:[0-9] ?){6,14}[0-9]$";
    public static final String REGEX_MAIL_ADDRESS =
            "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public static final String REGEX_NAME = "^[a-zA-Z\\s\\p{L}]+";
    public static final String REGEX_USERNAME = "^[a-zA-Z0-9._-]{6,64}$";
    public static final String REGEX_PASS = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!])$";
    public static final String REGEX_CREDIT_CARD =
            "^(?:(?<visa>4[0-9]{12}(?:[0-9]{3})?)|(?<mastercard>5[1-5][0-9]{14})|" +
            "(?<discover>6(?:011|5[0-9]{2})[0-9]{12})|(?<amex>3[47][0-9]{13})|" +
            "(?<diners>3(?:0[0-5]|[68][0-9])?[0-9]{11})|(?<jcb>(?:2131|1800|35[0-9]{3})[0-9]{11}))$";

    private RegexConstant() {
    }
}
