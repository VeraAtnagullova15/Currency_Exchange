package org.example.currency_exchange.utils;

public class ValidationUtils {

    private static final int CODE_LENGTH = 3;
    private static final int CURRENCY_PAIR_LENGTH = CODE_LENGTH*2;
    private static final int PATCH_REQUEST_BODY_LENGTH_AFTER_SPLIT = 2;

    public static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }

    public static boolean isValidLengthCode(String code) {
        return code.length() == CODE_LENGTH;
    }

    public static boolean isValidCodeType(String code) {
        return code.matches("[A-Z]+");
    }

    public static boolean isValidLengthCurrencyPair(String pair) {
        return pair.length() == CURRENCY_PAIR_LENGTH;
    }

    public static boolean isValidRateType(String rate) {
        return rate.matches("^[\\d.,]+$");
    }

    public static boolean isValidRequestBody(String body) {
        return body != null && body.contains("=") && body.split("=").length >= PATCH_REQUEST_BODY_LENGTH_AFTER_SPLIT
                && !body.split("=")[1].isEmpty();
    }
}
