package org.example.currency_exchange.dto;

import org.example.currency_exchange.models.Currency;

import java.math.BigDecimal;

public record ExchangeResultResponseDto(Currency base, Currency target, BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount) {
}
