package org.example.currency_exchange.utils;

public class ValidationUtils {

    private static final int CODE_LENGTH = 3;

    public static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }

    public static boolean isValidLengthCode(String code) {
        return code.length() == CODE_LENGTH;
    }

    public static boolean isValidCodeType(String code) {
        return code.matches("[A-Z]+");
    }
}
