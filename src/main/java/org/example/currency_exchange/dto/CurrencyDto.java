package org.example.currency_exchange.dto;

import org.example.currency_exchange.models.Currency;

public record CurrencyDto (int id, String name, String code, String sign) {

    public static CurrencyDto currencyToDto (Currency currency) {
        return new CurrencyDto(
                currency.getId(),
        currency.getFullName(),
        currency.getCode(),
        currency.getSign()
        );
    }

}
