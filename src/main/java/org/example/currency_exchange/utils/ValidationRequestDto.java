package org.example.currency_exchange.utils;

import org.example.currency_exchange.dto.CurrencyRequestDto;
import org.example.currency_exchange.dto.ExchangeRateRequestDto;
import org.example.currency_exchange.dto.ExchangeRatesRequestDto;
import org.example.currency_exchange.dto.ExchangeRequestDto;

import static org.example.currency_exchange.utils.ValidationUtils.*;

public class ValidationRequestDto {

    public static void validateCurrencyRequest(CurrencyRequestDto currencyRequestDto) {
        validateCodeLength(currencyRequestDto.getCode(),  "Код валюты должен состоять из трех букв");
        validateNotBlank(currencyRequestDto.getCode(), "Отсутствует поле code");
        validateNotBlank(currencyRequestDto.getName(), "Отсутствует поле name");
        validateNotBlank(currencyRequestDto.getSign(), "Отсутствует поле sign");
        validateCodeValue(currencyRequestDto.getCode(), "Код валюты должен состоять только из латинских букв");
    }

    public static void validateExchangeRateRequest(ExchangeRateRequestDto exchangeRateRequestDto) {
        validateNotBlank(exchangeRateRequestDto.getBaseCurrency(), "Код валюты отсутствует в адресе");
        validateNotBlank(exchangeRateRequestDto.getTargetCurrency(), "Код валюты отсутствует в адресе");
        validateCodeLength(exchangeRateRequestDto.getBaseCurrency(), "Код валюты должен состоять из трех букв");
        validateCodeLength(exchangeRateRequestDto.getTargetCurrency(), "Код валюты должен состоять из трех букв");
        validateCodeValue(exchangeRateRequestDto.getBaseCurrency(), "Код валюты должен состоять только из латинских букв");
        validateCodeValue(exchangeRateRequestDto.getTargetCurrency(), "Код валюты должен состоять только из латинских букв");
    }

    public static void validateExchangeRatesRequest(ExchangeRatesRequestDto exchangeRatesRequestDto) {
        validateNotBlank(exchangeRatesRequestDto.getBaseCurrency(), "Код валюты отсутствует в адресе");
        validateNotBlank(exchangeRatesRequestDto.getTargetCurrency(), "Код валюты отсутствует в адресе");
        validateNotBlank(exchangeRatesRequestDto.getRate(), "rate не указан");
        validateCodeLength(exchangeRatesRequestDto.getBaseCurrency(), "Код валюты должен состоять из трех букв");
        validateCodeLength(exchangeRatesRequestDto.getTargetCurrency(), "Код валюты должен состоять из трех букв");
        validateCodeValue(exchangeRatesRequestDto.getBaseCurrency(), "Код валюты должен состоять только из латинских букв");
        validateCodeValue(exchangeRatesRequestDto.getTargetCurrency(), "Код валюты должен состоять только из латинских букв");
        validateRateAndAmountValue(exchangeRatesRequestDto.getRate(), "Значение rate должно состоять из цифр");
    }

    public static void validateExchangeRequest(ExchangeRequestDto exchangeRequestDto) {
        validateNotBlank(exchangeRequestDto.getFrom(), "Отсутствует поле from");
        validateNotBlank(exchangeRequestDto.getTo(), "Отсутствует поле to");
        validateNotBlank(exchangeRequestDto.getAmount(), "Отсутствует поле amount");
        validateCodeLength(exchangeRequestDto.getFrom(), "Код валюты from должен состоять из трех букв");
        validateCodeLength(exchangeRequestDto.getTo(), "Код валюты to должен состоять из трех букв");
        validateRateAndAmountValue(exchangeRequestDto.getAmount(), "Значение amount должно состоять из цифр");
    }
}
