package org.example.currency_exchange.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dto.CurrencyDto;
import org.example.currency_exchange.exceptions.DataBaseException;
import org.example.currency_exchange.exceptions.ValidationException;
import org.example.currency_exchange.models.Currency;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Optional;

@WebServlet("/currency/*")
public class CurrencyServlet extends BaseServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        PrintWriter printWriter = response.getWriter();

        String pathInfo = request.getPathInfo();
        String code = pathInfo.substring(1).toUpperCase();

        validateNotBlank(code, "Код валюты отсутствует в адресе");
        validateCodeLength(code, "Поле code должно состоять из трех букв");
        validateCodeValue(code, "Поле code должно состоять только из латинских букв");

        Optional<Currency> optional = currencyService.getCurrencyByCode(code);


        if (optional.isEmpty()) {
            throw new NoSuchElementException();
        } else {
            Currency currency = optional.get();
            CurrencyDto currencyDto = CurrencyDto.toDto(currency);
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(printWriter, currencyDto);

        }
    }
}
