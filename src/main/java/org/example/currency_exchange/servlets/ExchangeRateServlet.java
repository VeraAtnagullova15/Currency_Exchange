package org.example.currency_exchange.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dto.ExchangeRateDto;
import org.example.currency_exchange.models.ExchangeRate;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Optional;


@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String pathInfo = request.getPathInfo().substring(1);
        validateNotBlank(pathInfo, "Код валюты отсутствует в адресе");

        String pairCurrencies = pathInfo.toUpperCase();
        validateCurrencyPairLength(pairCurrencies, "Валютная пара должна состоять из 6 символов");

        String baseCurrency = pairCurrencies.substring(0, 3);
        String targetCurrency = pairCurrencies.substring(3);

        PrintWriter printWriter = response.getWriter();

        validateNotBlank(baseCurrency, "Код валюты отсутствует в адресе");
        validateNotBlank(targetCurrency, "Код валюты отсутствует в адресе");
        validateCodeLength(baseCurrency, "Код валюты должен состоять из трех букв");
        validateCodeLength(targetCurrency, "ПКод валюты должен состоять из трех букв");
        validateCodeValue(baseCurrency, "Код валюты должен состоять только из латинских букв");
        validateCodeValue(targetCurrency, "Код валюты должен состоять только из латинских букв");

        Optional<ExchangeRate> optional = exchangeRateService.getExchangeRateByCodes(baseCurrency, targetCurrency);

        if (optional.isEmpty()) {
            throw new NoSuchElementException();
        } else {
            ExchangeRate exchangeRate = optional.get();
            ExchangeRateDto exchangeRateDto = ExchangeRateDto.exchangeRateToDto(exchangeRate);
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(printWriter, exchangeRateDto);
        }
    }



    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        if ("PATCH".equalsIgnoreCase(request.getMethod())) {

            PrintWriter printWriter = response.getWriter();

            String rateParameter = request.getParameter("rate");
            if ((rateParameter == null) || (rateParameter.equals(""))) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                printWriter.write("{\"message\": \"Отсутствует поле rate\"}");
            } else {
                try {
                    double rate = Double.parseDouble(request.getParameter("rate"));
                    if (rate <= 0) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        printWriter.write("{\"message\": \"Некорректное значение rate\"}");
                    } else {
                        response.setStatus(HttpServletResponse.SC_OK);
                        printWriter.write(String.valueOf(rate));
                        // TODO: change rate in DB
                    }
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    printWriter.write("{\"message\": \"Rate должен быть числом\"}");
                }
            }

        } else {
            super.service(request, response);
        }
    }

}
