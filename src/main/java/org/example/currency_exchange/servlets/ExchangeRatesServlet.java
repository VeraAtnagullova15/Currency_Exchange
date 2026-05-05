package org.example.currency_exchange.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dto.ExchangeRateDto;
import org.example.currency_exchange.models.ExchangeRate;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends BaseServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        PrintWriter printWriter = response.getWriter();

        List<ExchangeRate> exchangeRates = exchangeRateService.getAllExchangeRates();
        List<ExchangeRateDto> exchangeRateDtos = new ArrayList<>();

        for (ExchangeRate ex : exchangeRates) {
            exchangeRateDtos.add(ExchangeRateDto.exchangeRateToDto(ex));
        }

        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(printWriter, exchangeRateDtos);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");
        String rate = request.getParameter("rate");
        baseCurrencyCode = baseCurrencyCode.toUpperCase();
        targetCurrencyCode = targetCurrencyCode.toUpperCase();

        PrintWriter printWriter = response.getWriter();

        validateNotBlank(baseCurrencyCode, "Отсутствует поле baseCurrencyCode");
        validateNotBlank(targetCurrencyCode, "Отсутствует поле targetCurrencyCode");
        validateNotBlank(rate, "Отсутствует поле rate");
        validateCodeLength(baseCurrencyCode, "Код валюты должен состоять из трех букв");
        validateCodeLength(targetCurrencyCode, "Код валюты должен состоять из трех букв");
        validateCodeValue(baseCurrencyCode, "Код валюты должен состоять только из латинских букв");
        validateCodeValue(targetCurrencyCode, "Код валюты должен состоять только из латинских букв");
        validateRateAndAmountValue(rate, "Значение rate должно состоять только из цифр");

        BigDecimal rateBD = new BigDecimal(rate);
        if (baseCurrencyCode.equals(targetCurrencyCode)) {
            rateBD = new BigDecimal(1);
        }

        response.setStatus(HttpServletResponse.SC_CREATED);
        exchangeRateService.putExchangeRateIntoDB(baseCurrencyCode, targetCurrencyCode, rateBD);

        Optional<ExchangeRate> optionalExchangeRate = exchangeRateService.getExchangeRateByCodes(baseCurrencyCode, targetCurrencyCode);
        if (optionalExchangeRate.isEmpty()) {
            throw new NoSuchElementException("Обменный курс не найден");
        } else {
            ExchangeRate exchangeRate = optionalExchangeRate.get();
            ExchangeRateDto exchangeRateDto = ExchangeRateDto.exchangeRateToDto(exchangeRate);
            objectMapper.writeValue(printWriter, exchangeRateDto);
        }
    }

}
