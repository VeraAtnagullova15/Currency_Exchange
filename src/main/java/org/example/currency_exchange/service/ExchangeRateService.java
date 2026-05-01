package org.example.currency_exchange.service;

import org.example.currency_exchange.dao.ExchangeRateDao;
import org.example.currency_exchange.exceptions.DataBaseException;
import org.example.currency_exchange.models.Currency;
import org.example.currency_exchange.models.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ExchangeRateService {

    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

    public List<ExchangeRate> getAllExchangeRates() throws DataBaseException {
        return exchangeRateDao.getAllExchangeRates();
    }

    public Optional<ExchangeRate> getExchangeRateByCodes(String base, String target) throws DataBaseException {
        return exchangeRateDao.getExchangeRatesByCodes(base, target);
    }

    public void putExchangeRateIntoDB(String base, String target, BigDecimal rate) throws DataBaseException {
        exchangeRateDao.putExchangeRateIntoDB(base, target, rate);
    }


}
