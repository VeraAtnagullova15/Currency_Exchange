package org.example.currency_exchange.dto;

import org.example.currency_exchange.models.Currency;
import org.example.currency_exchange.models.ExchangeRate;

import java.math.BigDecimal;

public record ExchangeRateDto(int id, Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {

    public static ExchangeRateDto exchangeRateToDto(ExchangeRate exchangeRate) {
        return new ExchangeRateDto(
                exchangeRate.getId(),
                exchangeRate.getBaseCurrency(),
                exchangeRate.getTargetCurrency(),
                exchangeRate.getRate()
        );
    }
}


