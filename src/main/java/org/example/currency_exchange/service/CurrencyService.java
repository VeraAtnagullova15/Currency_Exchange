package org.example.currency_exchange.service;

import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.models.Currency;

import java.sql.SQLException;
import java.util.List;

public class CurrencyService {

    private final CurrencyDao currencyDao = new CurrencyDao();

    public List<Currency> getAllCurrencies () throws SQLException {
        return currencyDao.getAllCurrencies();
    }

    public Currency getCurrencyByCode(String code) throws SQLException{
        return currencyDao.getCurrencyByCode(code);
    }

    public void putCurrencyIntoDB(String code, String name, String sign) throws SQLException {
        currencyDao.putCurrencyIntoDB(code, name, sign);
    }
}
