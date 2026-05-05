package org.example.currency_exchange.servlets;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.currency_exchange.dto.CurrencyDto;
import org.example.currency_exchange.models.Currency;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@WebServlet("/currencies")
public class CurrenciesServlet extends BaseServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        PrintWriter printWriter = response.getWriter();

        List<Currency> currencies = currencyService.getAllCurrencies();
        List<CurrencyDto> currencyDtos = new ArrayList<>();
        for (Currency currency : currencies) {
            currencyDtos.add(CurrencyDto.currencyToDto(currency));
        }
        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(printWriter, currencyDtos);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");
        code = code.toUpperCase();

        PrintWriter printWriter = response.getWriter();

        validateCodeLength(code, "Код валюты должен состоять из трех букв");
        validateNotBlank(code, "Отсутствует поле code");
        validateNotBlank(name, "Отсутствует поле name");
        validateNotBlank(sign, "Отсутствует поле sign");
        validateCodeValue(code, "Код валюты должен состоять только из латинских букв");


        response.setStatus(HttpServletResponse.SC_CREATED);

        currencyService.putCurrencyIntoDB(code, name, sign);
        Optional<Currency> optional = currencyService.getCurrencyByCode(code);
        if (optional.isEmpty()) {
            throw new NoSuchElementException("Валюта не найдена");
        } else {
            Currency currency = optional.get();
            CurrencyDto currencyDto = CurrencyDto.currencyToDto(currency);
            objectMapper.writeValue(printWriter, currencyDto);

        }
    }
}
