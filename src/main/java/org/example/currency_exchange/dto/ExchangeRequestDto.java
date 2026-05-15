package org.example.currency_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.example.currency_exchange.utils.ValidationUtils.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRequestDto {

    private String from;
    private String to;
    private String amount;

    public void validateExchangeRequest() {
        validateNotBlank(from, "Отсутствует поле from");
        validateNotBlank(to, "Отсутствует поле to");
        validateNotBlank(amount, "Отсутствует поле amount");
        validateCodeLength(from, "Код валюты from должен состоять из трех букв");
        validateCodeLength(to, "Код валюты to должен состоять из трех букв");
        validateRateAndAmountValue(amount, "Значение amount должно состоять из цифр");
    }
}
