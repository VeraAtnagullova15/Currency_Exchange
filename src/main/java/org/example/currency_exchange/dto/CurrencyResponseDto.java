package org.example.currency_exchange.dto;

import org.example.currency_exchange.models.Currency;

public record CurrencyResponseDto(int id, String name, String code, String sign) {

    public static CurrencyResponseDto currencyToDto (Currency currency) {
        return new CurrencyResponseDto(
                currency.getId(),
        currency.getFullName(),
        currency.getCode(),
        currency.getSign()
        );
    }

}
