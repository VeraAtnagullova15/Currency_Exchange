package org.example.currency_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.example.currency_exchange.utils.ValidationUtils.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRequestDto {

    private String name;
    private String code;
    private String sign;

    public void validateCurrencyRequest() {
        validateCodeLength(code, "Код валюты должен состоять из трех букв");
        validateNotBlank(code, "Отсутствует поле code");
        validateNotBlank(code, "Отсутствует поле name");
        validateNotBlank(sign, "Отсутствует поле sign");
        validateCodeValue(code, "Код валюты должен состоять только из латинских букв");
    }

}
