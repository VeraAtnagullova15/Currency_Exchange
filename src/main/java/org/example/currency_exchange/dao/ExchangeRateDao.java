package org.example.currency_exchange.dao;

import org.example.currency_exchange.exceptions.AlreadyExistsException;
import org.example.currency_exchange.exceptions.DataBaseException;
import org.example.currency_exchange.models.Currency;
import org.example.currency_exchange.models.ExchangeRate;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDao {

    private final CurrencyDao currencyDao;
    private static final String SQL_GET_ALL_EXCHANGE_RATES = """
SELECT er.ID AS er_id, er.Rate AS rate, 
       base.ID AS base_id, base.Code AS base_code, base.FullName AS base_name, base.Sign AS base_sign, 
       target.ID AS target_id, target.Code AS target_code, target.FullName AS target_name, target.Sign AS target_sign 
FROM ExchangeRates er
JOIN Currencies base ON er.BaseCurrencyId = base.ID
JOIN Currencies target ON er.TargetCurrencyId = target.ID
       
""";
    private static final String SQL_GET_EXCHANGE_RATE = """
SELECT er.ID AS er_id, er.Rate AS rate, 
       base.ID AS base_id, base.Code AS base_code, base.FullName AS base_name, base.Sign AS base_sign, 
       target.ID AS target_id, target.Code AS target_code, target.FullName AS target_name, target.Sign AS target_sign 
FROM ExchangeRates er
JOIN Currencies base ON er.BaseCurrencyId = base.ID
JOIN Currencies target ON er.TargetCurrencyId = target.ID
            WHERE base.Code=? AND target.Code=?            
            """;
    private static final String SQL_PUT_EXCHANGE_RATE = "INSERT INTO ExchangeRates(BaseCurrencyID, TargetCurrencyID, Rate)" +
            " VALUES(?,?,?)";

    private static final String SQL_UPDATE_EXCHANGE_RATE = "UPDATE ExchangeRates SET Rate=? " +
            "WHERE BaseCurrencyId=? AND TargetCurrencyId=?";

    public ExchangeRateDao(CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }


    public List<ExchangeRate> getAllExchangeRates() {

        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_GET_ALL_EXCHANGE_RATES);) {

            while(resultSet.next()) {

                Currency baseCurrency = new Currency();
                baseCurrency.setId(resultSet.getInt("base_id"));
                baseCurrency.setCode(resultSet.getString("base_code"));
                baseCurrency.setFullName(resultSet.getString("base_name"));
                baseCurrency.setSign(resultSet.getString("base_sign"));

                Currency targetCurrency = new Currency();
                targetCurrency.setId(resultSet.getInt("target_id"));
                targetCurrency.setCode(resultSet.getString("target_code"));
                targetCurrency.setFullName(resultSet.getString("target_name"));
                targetCurrency.setSign(resultSet.getString("target_sign"));

                ExchangeRate exchangeRate = new ExchangeRate();
                exchangeRate.setId(resultSet.getInt("er_id"));
                exchangeRate.setRate(resultSet.getBigDecimal("rate"));
                exchangeRate.setBaseCurrency(baseCurrency);
                exchangeRate.setTargetCurrency(targetCurrency);

                exchangeRates.add(exchangeRate);
            }
        } catch (SQLException sqlException) {
            throw new DataBaseException("Ошибка базы данных");
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

                    Currency baseCurrency = new Currency();
                    baseCurrency.setId(resultSet.getInt("base_id"));
                    baseCurrency.setCode(resultSet.getString("base_code"));
                    baseCurrency.setFullName(resultSet.getString("base_name"));
                    baseCurrency.setSign(resultSet.getString("base_sign"));

                    Currency targetCurrency = new Currency();
                    targetCurrency.setId(resultSet.getInt("target_id"));
                    targetCurrency.setCode(resultSet.getString("target_code"));
                    targetCurrency.setFullName(resultSet.getString("target_name"));
                    targetCurrency.setSign(resultSet.getString("target_sign"));

                    ExchangeRate exchangeRate = new ExchangeRate();
                    exchangeRate.setId(resultSet.getInt("er_id"));
                    exchangeRate.setRate(resultSet.getBigDecimal("rate"));
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

    public void putExchangeRateIntoDB(int baseId, int targetId, BigDecimal rate) {

        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_PUT_EXCHANGE_RATE);) {

            preparedStatement.setInt(1, baseId);
            preparedStatement.setInt(2, targetId);
            preparedStatement.setBigDecimal(3, rate);

            preparedStatement.executeUpdate();

        } catch (SQLException sqlException) {
            if (sqlException.getMessage().contains("UNIQUE constraint failed")) {
                throw new AlreadyExistsException("Обменный курс уже существует");
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
