package org.example.currency_exchange.service;

import org.example.currency_exchange.dao.ExchangeRateDao;
import org.example.currency_exchange.dto.ExchangeResult;
import org.example.currency_exchange.models.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.spec.ECField;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ExchangeService {

    private static final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

    public ExchangeResult exchange (String from, String to, BigDecimal amount) {

        Optional<ExchangeRate> directOptional = exchangeRateDao.getExchangeRatesByCodes(from, to);

        if (directOptional.isPresent()) {
            ExchangeRate directExchangeRate = directOptional.get();
            BigDecimal rate = directExchangeRate.getRate().setScale(2, RoundingMode.HALF_UP);
            BigDecimal convertedAmount = amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
            ExchangeResult result = new ExchangeResult(directExchangeRate.getBaseCurrency(), directExchangeRate.getTargetCurrency(),
                    rate, amount, convertedAmount);
            return result;
        }

        Optional<ExchangeRate> reverseOptional = exchangeRateDao.getExchangeRatesByCodes(to, from);

        if (reverseOptional.isPresent()) {
            ExchangeRate reverseExchangeRate = reverseOptional.get();
            BigDecimal rate = reverseExchangeRate.getRate();
            BigDecimal reverseRate = BigDecimal.ONE.divide(rate, 2, RoundingMode.HALF_UP);
            BigDecimal convertedAmount = amount.multiply(reverseRate).setScale(2, RoundingMode.HALF_UP);
            ExchangeResult result = new ExchangeResult(reverseExchangeRate.getBaseCurrency(), reverseExchangeRate.getTargetCurrency(),
                    reverseRate, amount, convertedAmount);
            return result;
        }

        String USD = "USD";
        Optional<ExchangeRate> USDfromOptional = exchangeRateDao.getExchangeRatesByCodes(USD, from);
        Optional<ExchangeRate> USDtoOptional = exchangeRateDao.getExchangeRatesByCodes(USD, to);

        if (USDfromOptional.isPresent() && USDtoOptional.isPresent()) {
            BigDecimal UsdFromRate = USDfromOptional.get().getRate().setScale(2,RoundingMode.HALF_UP);
            BigDecimal UsdToRate = USDtoOptional.get().getRate().setScale(2, RoundingMode.HALF_UP);
            BigDecimal rate = UsdToRate.divide(UsdFromRate, 2, RoundingMode.HALF_UP);
            BigDecimal convertedAmount = amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
            ExchangeResult result = new ExchangeResult(USDfromOptional.get().getTargetCurrency(),
                    USDtoOptional.get().getTargetCurrency(), rate, amount, convertedAmount);
            return result;
        }

        throw new NoSuchElementException("Курс обмена не найден");
    }

}
