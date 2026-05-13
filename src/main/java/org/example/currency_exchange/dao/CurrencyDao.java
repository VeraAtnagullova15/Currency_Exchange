package org.example.currency_exchange.dao;

import lombok.extern.slf4j.Slf4j;
import org.example.currency_exchange.exceptions.AlreadyExistsException;
import org.example.currency_exchange.exceptions.DataBaseException;
import org.example.currency_exchange.models.Currency;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CurrencyDao {

    private static final String SQL_GET_ALL_CURRENCIES = "SELECT * FROM Currencies";
    private static final String SQL_GET_CURRENCY_BY_CODE = "SELECT * FROM Currencies WHERE Code=?";
    private static final String SQL_PUT_CURRENCY = "INSERT INTO Currencies(Code, FullName, Sign)\n" +
            "VALUES(?, ?, ?)";

    public List<Currency> getAllCurrencies() {

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
            log.error("Ошибка при получении всех валют", sqlException);
            throw new DataBaseException("Ошибка базы данных");
        }

        return currencies;
    }


    public Optional<Currency> getCurrencyByCode(String code) {

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
            log.error("Ошибка при получении валюты по коду: {}", code, sqlException);
            throw new DataBaseException("Ошибка базы данных");
        }

        return Optional.empty();

    }

    public void putCurrencyIntoDB(String code,String name, String sign) {

        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_PUT_CURRENCY);) {

            preparedStatement.setString(1, code);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, sign);

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            if (sqlException instanceof SQLiteException sqLiteException) {
                if (sqLiteException.getResultCode() == SQLiteErrorCode.SQLITE_CONSTRAINT_UNIQUE) {
                    log.error("Ошибка добавления валюты", code, sqLiteException);
                    throw new AlreadyExistsException("Валюта " + code + " уже существует");
                }
            }
            log.error("Ошибка при добавлении валюты", code, sqlException);
            throw new DataBaseException("Ошибка базы данных");
        }
    }


}
