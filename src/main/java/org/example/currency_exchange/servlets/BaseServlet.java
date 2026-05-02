package org.example.currency_exchange.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import org.example.currency_exchange.exceptions.ValidationException;
import org.example.currency_exchange.service.CurrencyService;
import org.example.currency_exchange.service.ExchangeRateService;
import org.example.currency_exchange.utils.ValidationUtils;

public abstract class BaseServlet extends HttpServlet {

    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected final CurrencyService currencyService = new CurrencyService();
    protected final ExchangeRateService exchangeRateService = new ExchangeRateService();

    protected void validateCodeLength(String code, String message) throws ValidationException {
        if (!ValidationUtils.isValidLengthCode(code)) {
            throw new ValidationException(message);
        }
    }

    protected void validateNotBlank(String value, String message) throws ValidationException {
        if(ValidationUtils.isNullOrBlank(value)) {
            throw new ValidationException(message);
        }
    }

    protected void validateCodeValue(String code, String message) throws ValidationException {
        if (!ValidationUtils.isValidCodeType(code)) {
            throw new ValidationException(message);
        }
    }

    protected void validateCurrencyPairLength(String pair, String message) throws ValidationException {
        if (!ValidationUtils.isValidLengthCurrencyPair(pair)) {
            throw new ValidationException(message);
        }
    }

    protected void validateRateValue(String rate, String message) {
        if (!ValidationUtils.isValidRateType(rate)) {
            throw new ValidationException(message);
        }
    }

    protected void validatePatchBodyRequest(String body, String message) {
        if (!ValidationUtils.isValidRequestBody(body)) {
            throw new ValidationException(message);
        }
    }

}
