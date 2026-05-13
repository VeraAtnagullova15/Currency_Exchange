package org.example.currency_exchange.service;

import lombok.AllArgsConstructor;
import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.dao.ExchangeRateDao;
import org.example.currency_exchange.exceptions.DataBaseException;
import org.example.currency_exchange.models.Currency;
import org.example.currency_exchange.models.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@AllArgsConstructor
public class ExchangeRateService {

    private ExchangeRateDao exchangeRateDao;
    private CurrencyDao currencyDao;

    public List<ExchangeRate> getAllExchangeRates() throws DataBaseException {
        return exchangeRateDao.getAllExchangeRates();
    }

    public Optional<ExchangeRate> getExchangeRateByCodes(String base, String target) throws DataBaseException {
        return exchangeRateDao.getExchangeRatesByCodes(base, target);
    }

    public void putExchangeRateIntoDB(String base, String target, BigDecimal rate) throws DataBaseException {

        Currency baseCurrency = currencyDao.getCurrencyByCode(base)
                .orElseThrow(() -> new NoSuchElementException("Валюта " + base + " не найдена"));
        Currency targetCurrency = currencyDao.getCurrencyByCode(target)
                .orElseThrow(() -> new NoSuchElementException("Валюта " + target + " не найдена"));

        int baseId = baseCurrency.getId();
        int targetId = targetCurrency.getId();

        exchangeRateDao.putExchangeRateIntoDB(baseId, targetId, rate);
    }

    public void updateExchangeRate(String base, String target, BigDecimal rate) throws DataBaseException {

        Currency baseCurrency = currencyDao.getCurrencyByCode(base)
                .orElseThrow(() -> new NoSuchElementException("Валюта " + base + " не найдена"));
        Currency targetCurrency = currencyDao.getCurrencyByCode(target)
                .orElseThrow(() -> new NoSuchElementException("Валюта " + target + " не найдена"));

        int baseId = baseCurrency.getId();
        int targetId = targetCurrency.getId();

        exchangeRateDao.updateExchangeRate(baseId, targetId, rate);
    }


}
