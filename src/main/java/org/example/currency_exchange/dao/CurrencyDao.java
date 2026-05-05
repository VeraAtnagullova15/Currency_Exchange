package org.example.currency_exchange.dao;

import org.example.currency_exchange.exceptions.DataBaseException;
import org.example.currency_exchange.models.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao {

    private static final String SQL_GET_ALL_CURRENCIES = "SELECT * FROM Currencies";
    private static final String SQL_GET_CURRENCY_BY_CODE = "SELECT * FROM Currencies WHERE Code=?";
    private static final String SQL_PUT_CURRENCY = "INSERT INTO Currencies(Code, FullName, Sign)\n" +
            "VALUES(?, ?, ?)";
    private static final String SQL_GET_CURRENCY_BY_ID = "SELECT * FROM Currencies WHERE ID=?";

    public List<Currency> getAllCurrencies() throws DataBaseException {

        List<Currency> currencies = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_ALL_CURRENCIES);) {


            while (resultSet.next()) {
                Currency currency = new Currency();

                currency.setId(resultSet.getInt("ID"));
                currency.setCode(resultSet.getString("Code"));
                currency.setFullName(resultSet.getString("FullName"));
                currency.setSign(resultSet.getString("Sign"));

                currencies.add(currency);
            }
        } catch (SQLException sqlException) {
            throw new DataBaseException("Ошибка базы данных");
        }

        return currencies;
    }


    public Optional<Currency> getCurrencyByCode(String code) throws DataBaseException {

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_CURRENCY_BY_CODE);) {

            preparedStatement.setString(1, code);

            try (ResultSet resultSet = preparedStatement.executeQuery();) {

                if (resultSet.next()) {
                    Currency currency = new Currency();
                    currency.setId(resultSet.getInt("ID"));
                    currency.setCode(resultSet.getString("Code"));
                    currency.setFullName(resultSet.getString("FullName"));
                    currency.setSign(resultSet.getString("Sign"));
                    return Optional.of(currency);
                }
            }
        } catch (SQLException sqlException) {
            throw new DataBaseException("Ошибка базы данных");
        }

        return Optional.empty();

    }

    public void putCurrencyIntoDB(String code,String name, String sign) throws DataBaseException {

        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_PUT_CURRENCY);) {

            preparedStatement.setString(1, code);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, sign);

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            if (sqlException.getMessage().contains("UNIQUE constraint failed")) {
                throw new DataBaseException("Валюта уже существует");
            }
            throw new DataBaseException("Ошибка базы данных");
        }
    }

    public Optional<Currency> getCurrencyById(int id) throws DataBaseException {

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_CURRENCY_BY_ID);) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery();) {

                if (resultSet.next()) {
                    Currency currency = new Currency();
                    currency.setId(resultSet.getInt("ID"));
                    currency.setCode(resultSet.getString("Code"));
                    currency.setFullName(resultSet.getString("FullName"));
                    currency.setSign(resultSet.getString("Sign"));
                    return Optional.of(currency);
                }
            }
        } catch (SQLException sqlException) {
            throw new DataBaseException("Ошибка базы данных");
        }

        return Optional.empty();

    }


}
