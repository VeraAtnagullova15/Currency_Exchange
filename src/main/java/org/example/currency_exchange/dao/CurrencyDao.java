package org.example.currency_exchange.dao;

import org.example.currency_exchange.models.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDao {

    public List<Currency> getAllCurrencies() throws SQLException {

        List<Currency> currencies = new ArrayList<>();
        String sql = "SELECT * FROM Currencies";

        try (Connection connection = ConnectionManager.getConnection();
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

    public Currency getCurrencyByCode(String code) throws SQLException {

        String sql = "SELECT * FROM Currencies WHERE Code=?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setString(1, code);

            try (ResultSet resultSet = preparedStatement.executeQuery();) {

                if (resultSet.next()) {
                    Currency currency = new Currency();
                    currency.setId(resultSet.getInt("ID"));
                    currency.setCode(resultSet.getString("Code"));
                    currency.setFullName(resultSet.getString("FullName"));
                    currency.setSign(resultSet.getString("Sign"));
                    return currency;
                }
            }
        }

        return null;

    }

    public void putCurrencyIntoDB(String code,String name, String sign) throws SQLException {

        String sql = "INSERT OR IGNORE INTO Currencies(Code, FullName, Sign)\n" +
                "VALUES(?, ?, ?)";

        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setString(1, code);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, sign);

            preparedStatement.executeUpdate();
        }
    }


}
