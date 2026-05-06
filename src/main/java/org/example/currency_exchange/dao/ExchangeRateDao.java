package org.example.currency_exchange.dao;

import org.example.currency_exchange.exceptions.DataBaseException;
import org.example.currency_exchange.models.Currency;
import org.example.currency_exchange.models.ExchangeRate;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ExchangeRateDao {

    private final CurrencyDao currencyDao;
    private static final String SQL_GET_ALL_EXCHANGE_RATES = "SELECT * FROM ExchangeRates";
    private static final String SQL_GET_EXCHANGE_RATE = """
            SELECT ex.ID, ex.BaseCurrencyId, ex.TargetCurrencyId, ex.Rate
            FROM ExchangeRates ex
            JOIN Currencies base ON ex.BaseCurrencyId = base.ID
            JOIN Currencies target ON ex.TargetCurrencyId = target.ID
            WHERE base.Code=? AND target.Code=?            
            """;
    private static final String SQL_PUT_EXCHANGE_RATE = "INSERT INTO ExchangeRates(BaseCurrencyID, TargetCurrencyID, Rate)" +
            " VALUES(?,?,?)";

    private static final String SQL_UPDATE_EXCHANGE_RATE = "UPDATE ExchangeRates SET Rate=? " +
            "WHERE BaseCurrencyId=? AND TargetCurrencyId=?";

    public ExchangeRateDao(CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }


    public List<ExchangeRate> getAllExchangeRates() throws DataBaseException {

        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_GET_ALL_EXCHANGE_RATES);) {

            while(resultSet.next()) {
                ExchangeRate exchangeRate = new ExchangeRate();

                exchangeRate.setId(resultSet.getInt("ID"));
                exchangeRate.setRate(resultSet.getBigDecimal("Rate"));

                Currency baseCurrency = currencyDao.getCurrencyById(resultSet.getInt("BaseCurrencyId"))
                        .orElseThrow(() -> new NoSuchElementException("Валюта не найдена"));
                Currency targetCurrency = currencyDao.getCurrencyById(resultSet.getInt("TargetCurrencyId"))
                        .orElseThrow(() -> new NoSuchElementException("Валюта не найдена"));

                exchangeRate.setBaseCurrency(baseCurrency);
                exchangeRate.setTargetCurrency(targetCurrency);

                exchangeRates.add(exchangeRate);
            }
        } catch (SQLException sqlException) {
            throw new DataBaseException(sqlException.getMessage());
        }

        return exchangeRates;

    }

    public Optional<ExchangeRate> getExchangeRatesByCodes(String base, String target) throws DataBaseException {

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_EXCHANGE_RATE);) {

            preparedStatement.setString(1, base);
            preparedStatement.setString(2, target);

            try (ResultSet resultSet = preparedStatement.executeQuery();) {

                if (resultSet.next()) {
                    ExchangeRate exchangeRate = new ExchangeRate();
                    exchangeRate.setId(resultSet.getInt("ID"));
                    exchangeRate.setRate(resultSet.getBigDecimal("Rate"));

                    Currency baseCurrency = currencyDao.getCurrencyById(resultSet.getInt("BaseCurrencyId"))
                            .orElseThrow(() -> new NoSuchElementException("Валюта " + base + "не найдена"));
                    Currency targetCurrency = currencyDao.getCurrencyById(resultSet.getInt("TargetCurrencyId"))
                            .orElseThrow(() -> new NoSuchElementException("Валюта " + target + " не найдена"));

                    exchangeRate.setBaseCurrency(baseCurrency);
                    exchangeRate.setTargetCurrency(targetCurrency);

                    return Optional.of(exchangeRate);
                }
            }
        } catch (SQLException sqlException) {
            throw new DataBaseException("Ошибка базы данных");
        }

        return Optional.empty();

    }

    public void putExchangeRateIntoDB(int baseId, int targetId, BigDecimal rate) throws DataBaseException {

        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_PUT_EXCHANGE_RATE);) {

            preparedStatement.setInt(1, baseId);
            preparedStatement.setInt(2, targetId);
            preparedStatement.setBigDecimal(3, rate);

            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            if (sqlException.getMessage().contains("UNIQUE constraint failed")) {
                throw new DataBaseException("Обменный курс уже существует");
            }
            throw new DataBaseException("Ошибка базы данных");
        }
    }


    public void updateExchangeRate(int baseId, int targetId, BigDecimal rate) {

        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_EXCHANGE_RATE);) {

            preparedStatement.setBigDecimal(1, rate);
            preparedStatement.setInt(2, baseId);
            preparedStatement.setInt(3, targetId);

            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            throw new DataBaseException("Ошибка базы данных");
        }
    }

}
