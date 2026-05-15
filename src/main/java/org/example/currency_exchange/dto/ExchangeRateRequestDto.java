package org.example.currency_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.example.currency_exchange.utils.ValidationUtils.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateRequestDto {

    private String baseCurrency;
    private String targetCurrency;

    public void validateExchangeRateRequest() {
        validateNotBlank(baseCurrency, "Код валюты отсутствует в адресе");
        validateNotBlank(targetCurrency, "Код валюты отсутствует в адресе");
        validateCodeLength(baseCurrency, "Код валюты должен состоять из трех букв");
        validateCodeLength(targetCurrency, "Код валюты должен состоять из трех букв");
        validateCodeValue(baseCurrency, "Код валюты должен состоять только из латинских букв");
        validateCodeValue(targetCurrency, "Код валюты должен состоять только из латинских букв");
    }


}
