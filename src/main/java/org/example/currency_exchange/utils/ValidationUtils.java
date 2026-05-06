package org.example.currency_exchange.utils;

import org.example.currency_exchange.exceptions.ValidationException;

public class ValidationUtils {

    private static final int CODE_LENGTH = 3;
    private static final int CURRENCY_PAIR_LENGTH = CODE_LENGTH*2;
    private static final int PATCH_REQUEST_BODY_LENGTH_AFTER_SPLIT = 2;

    public static void validateCodeLength(String code, String message) throws ValidationException {
        if (!ValidationUtils.isValidLengthCode(code)) {
            throw new ValidationException(message);
        }
    }

    public static void validateNotBlank(String value, String message) throws ValidationException {
        if(ValidationUtils.isNullOrBlank(value)) {
            throw new ValidationException(message);
        }
    }

    public static void validateCodeValue(String code, String message) throws ValidationException {
        if (!ValidationUtils.isValidCodeType(code)) {
            throw new ValidationException(message);
        }
    }

    public static void validateCurrencyPairLength(String pair, String message) throws ValidationException {
        if (!ValidationUtils.isValidLengthCurrencyPair(pair)) {
            throw new ValidationException(message);
        }
    }

    public static void validateRateAndAmountValue(String rate, String message) throws ValidationException {
        if (!ValidationUtils.isValidRateAndAmountType(rate)) {
            throw new ValidationException(message);
        }
    }

    public static void validatePatchBodyRequest(String body, String message) throws ValidationException {
        if (!ValidationUtils.isValidRequestBody(body)) {
            throw new ValidationException(message);
        }
    }

    private static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }

    private static boolean isValidLengthCode(String code) {
        return code.length() == CODE_LENGTH;
    }

    private static boolean isValidCodeType(String code) {
        return code.matches("[A-Z]+");
    }

    private static boolean isValidLengthCurrencyPair(String pair) {
        return pair.length() == CURRENCY_PAIR_LENGTH;
    }

    private static boolean isValidRateAndAmountType(String rate) {
        return rate.matches("^[\\d.,]+$");
    }

    private static boolean isValidRequestBody(String body) {
        return body != null && body.contains("=") && body.split("=").length >= PATCH_REQUEST_BODY_LENGTH_AFTER_SPLIT
                && !body.split("=")[1].isEmpty();
    }
}
