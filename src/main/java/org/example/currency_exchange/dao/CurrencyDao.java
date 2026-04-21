package org.example.currency_exchange.dao;

import org.example.currency_exchange.models.Currency;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDao {

    public List<Currency> getAllCurrencies() throws SQLException {
        List<Currency> currencies = new ArrayList<>();
        String sql = "SELECT * FROM Currencies";

        try(Connection connection = ConnectionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);) {


            while (resultSet.next()) {
                Currency currency = new Currency();

                currency.setId(resultSet.getInt("ID"));
                currency.setCode(resultSet.getString("Code"));
                currency.setFullName(resultSet.getString("FullName"));
                currency.setSign(resultSet.getString("Sign"));

                currencies.add(currency);
            }
        }

        return currencies;
    }


}
