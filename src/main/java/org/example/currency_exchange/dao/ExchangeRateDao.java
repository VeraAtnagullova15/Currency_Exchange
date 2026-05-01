package org.example.currency_exchange.dao;

import org.example.currency_exchange.exceptions.DataBaseException;
import org.example.currency_exchange.models.Currency;
import org.example.currency_exchange.models.ExchangeRate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ExchangeRateDao {

    private static final CurrencyDao currencyDao = new CurrencyDao();
    private static final String SQL_GET_ALL_EXCHANGE_RATES = "SELECT * FROM ExchangeRates";
    private static final String SQL_GET_EXCHANGE_RATE = """
            SELECT ex.ID, ex.BaseCurrencyId, ex.TargetCurrencyId, ex.Rate
            FROM ExchangeRates ex
            JOIN Currencies base ON ex.BaseCurrencyId = base.ID
            JOIN Currencies target ON ex.TargetCurrencyId = target.ID
            WHERE base.Code=? AND target.Code=?            
            """;




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
                        .orElseThrow(NoSuchElementException::new);
                Currency targetCurrency = currencyDao.getCurrencyById(resultSet.getInt("TargetCurrencyId"))
                        .orElseThrow(NoSuchElementException::new);

                exchangeRate.setBaseCurrency(baseCurrency);
                exchangeRate.setTargetCurrency(targetCurrency);

                exchangeRates.add(exchangeRate);
            }
        } catch (SQLException sqlException) {
            throw new DataBaseException(sqlException.getMessage());
        }

        return exchangeRates;

    }

    public Optional<ExchangeRate> getExchangeRatesByCodes(String base, String target) {

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
                            .orElseThrow(NoSuchElementException::new);
                    Currency targetCurrency = currencyDao.getCurrencyById(resultSet.getInt("TargetCurrencyId"))
                            .orElseThrow(NoSuchElementException::new);

                    exchangeRate.setBaseCurrency(baseCurrency);
                    exchangeRate.setTargetCurrency(targetCurrency);

                    return Optional.of(exchangeRate);
                }
            }
        } catch (SQLException sqlException) {
            throw new DataBaseException(sqlException.getMessage());
        }

        return Optional.empty();

    }


}
