package org.example.currency_exchange.service;

import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.exceptions.DataBaseException;
import org.example.currency_exchange.models.Currency;

import java.util.List;
import java.util.Optional;

public class CurrencyService {

    private final CurrencyDao currencyDao = new CurrencyDao();

    public List<Currency> getAllCurrencies () throws DataBaseException {
        return currencyDao.getAllCurrencies();
    }

    public Optional<Currency> getCurrencyByCode(String code) throws DataBaseException {
        return currencyDao.getCurrencyByCode(code);
    }

    public void putCurrencyIntoDB(String code, String name, String sign) throws DataBaseException {
        currencyDao.putCurrencyIntoDB(code, name, sign);
    }
}
