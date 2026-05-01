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
import java.util.ArrayList;
import java.util.List;


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

        //TODO: put information in DataBase

        PrintWriter printWriter = response.getWriter();

        if (baseCurrencyCode == null || baseCurrencyCode.equals("") || baseCurrencyCode.length() != 3 ||
                targetCurrencyCode == null || targetCurrencyCode.equals("") || targetCurrencyCode.length() != 3 ||
                rate == null || rate.equals("")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            printWriter.write("{\"message\": \"Отсутствует одно из полей\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_CREATED);
            printWriter.write("{}");
        }


    }
}
