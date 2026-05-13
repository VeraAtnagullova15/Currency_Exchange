package org.example.currency_exchange.service;

import lombok.AllArgsConstructor;
import org.example.currency_exchange.dao.ExchangeRateDao;
import org.example.currency_exchange.dto.ExchangeResult;
import org.example.currency_exchange.models.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.NoSuchElementException;
import java.util.Optional;

@AllArgsConstructor
public class ExchangeService {

    private ExchangeRateDao exchangeRateDao;


    public ExchangeResult exchange(String from, String to, BigDecimal amount) {

        BigDecimal scaledAmount = amount.setScale(2, RoundingMode.HALF_UP);

        return directExchange(from, to, amount)
                .or(() -> reverseExchange(from, to, scaledAmount))
                .or(() -> exchangeByUsd(from, to, scaledAmount))
                .orElseThrow(() -> new NoSuchElementException("Обменный курс не найден"));
    }

    private Optional<ExchangeResult> directExchange(String from, String to, BigDecimal amount) {

        Optional<ExchangeRate> directOptional = exchangeRateDao.getExchangeRatesByCodes(from, to);

        if (directOptional.isPresent()) {
            ExchangeRate directExchangeRate = directOptional.get();
            BigDecimal rate = directExchangeRate.getRate();
            BigDecimal convertedAmount = amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
            ExchangeResult result = new ExchangeResult(directExchangeRate.getBaseCurrency(), directExchangeRate.getTargetCurrency(),
                    rate, amount, convertedAmount);
            return Optional.of(result);
        }
        return Optional.empty();
    }

    private Optional<ExchangeResult> reverseExchange(String from, String to, BigDecimal amount) {

        Optional<ExchangeRate> reverseOptional = exchangeRateDao.getExchangeRatesByCodes(to, from);

        if (reverseOptional.isPresent()) {
            ExchangeRate reverseExchangeRate = reverseOptional.get();
            BigDecimal rate = reverseExchangeRate.getRate();
            BigDecimal reverseRate = BigDecimal.ONE.divide(rate, 6, RoundingMode.HALF_UP);
            BigDecimal convertedAmount = amount.multiply(reverseRate).setScale(2, RoundingMode.HALF_UP);
            ExchangeResult result = new ExchangeResult(reverseExchangeRate.getBaseCurrency(), reverseExchangeRate.getTargetCurrency(),
                    reverseRate, amount, convertedAmount);
            return Optional.of(result);
        }
        return Optional.empty();
    }

    private Optional<ExchangeResult> exchangeByUsd(String from, String to, BigDecimal amount) {

        String USD = "USD";
        Optional<ExchangeRate> USDfromOptional = exchangeRateDao.getExchangeRatesByCodes(USD, from);
        Optional<ExchangeRate> USDtoOptional = exchangeRateDao.getExchangeRatesByCodes(USD, to);

        if (USDfromOptional.isPresent() && USDtoOptional.isPresent()) {
            BigDecimal UsdFromRate = USDfromOptional.get().getRate();
            BigDecimal UsdToRate = USDtoOptional.get().getRate();
            BigDecimal rate = UsdToRate.divide(UsdFromRate, 6, RoundingMode.HALF_UP);
            BigDecimal convertedAmount = amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
            ExchangeResult result = new ExchangeResult(USDfromOptional.get().getTargetCurrency(),
                    USDtoOptional.get().getTargetCurrency(), rate, amount, convertedAmount);
            return Optional.of(result);
        }
        return Optional.empty();
    }

}
