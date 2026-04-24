package org.example.currency_exchange.dao;

import java.sql.SQLException;
import org.example.currency_exchange.models.Currency;
import java.util.List;

public class TestDao {
    public static void main(String[] args) {
        CurrencyDao dao = new CurrencyDao();

        try {
            List<Currency> currencies = dao.getAllCurrencies();

            for (Currency currency : currencies) {
                System.out.println(currency.getId() + "," + currency.getCode() + "," + currency.getFullName() + "," + currency.getSign());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        try{
//            Currency currency = dao.getCurrencyByCode("USD");
//            System.out.println(currency);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

//        try {
//            dao.putCurrencyIntoDB("TRY", "Turkish Lira", "₺");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}
