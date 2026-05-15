package org.example.currency_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.example.currency_exchange.utils.ValidationUtils.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRatesRequestDto {

    private String baseCurrency;
    private String targetCurrency;
    private String rate;

    public void validateExchangeRatesRequest() {
        validateNotBlank(baseCurrency, "Код валюты отсутствует в адресе");
        validateNotBlank(targetCurrency, "Код валюты отсутствует в адресе");
        validateNotBlank(rate, "rate не указан");
        validateCodeLength(baseCurrency, "Код валюты должен состоять из трех букв");
        validateCodeLength(targetCurrency, "Код валюты должен состоять из трех букв");
        validateCodeValue(baseCurrency, "Код валюты должен состоять только из латинских букв");
        validateCodeValue(targetCurrency, "Код валюты должен состоять только из латинских букв");
        validateRateAndAmountValue(rate, "Значение rate должно состоять из цифр");
    }
}
