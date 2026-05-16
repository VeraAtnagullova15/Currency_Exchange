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

}
