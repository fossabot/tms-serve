package com.odakota.tms.utils;

import com.odakota.tms.constant.RegexConstant;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * @author haidv
 * @version 1.0
 */
public class RandomUtils {

    public static String generateAlphaNumeric(int n) {

        // length is bounded by 256 Character
        byte[] array = new byte[256];
        new Random().nextBytes(array);

        String randomString = new String(array, StandardCharsets.UTF_8);

        // Create a StringBuffer to store the result
        StringBuilder r = new StringBuilder();

        // remove all spacial char
        String alphaNumericString = randomString.replaceAll(RegexConstant.REGEX_ALPHABET_NUMBER, "");

        // Append first 20 alphanumeric characters
        // from the generated random String into the result
        for (int k = 0; k < alphaNumericString.length(); k++) {

            if (Character.isLetter(alphaNumericString.charAt(k)) && (n > 0)
                || Character.isDigit(alphaNumericString.charAt(k)) && (n > 0)) {
                r.append(alphaNumericString.charAt(k));
                n--;
            }
        }

        // return the resultant string
        return r.toString();
    }
}
