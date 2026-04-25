package org.example.currency_exchange.servlets;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.currency_exchange.models.Currency;
import org.example.currency_exchange.service.CurrencyService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private final CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        PrintWriter printWriter = response.getWriter();

        try {
            List<Currency> currencies = currencyService.getAllCurrencies();
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(printWriter, currencies);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            printWriter.write("{\"message\": \"Ошибка базы данных\"}");

        }


    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");

        PrintWriter printWriter = response.getWriter();

        if ((name == null) || (name.equals("")) ||
                (code == null) || (code.length() != 3) ||
                (sign == null) || (sign.equals(""))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            printWriter.write("{\"message\": \"Отсутствует одно из полей\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_CREATED);
            try {
                currencyService.putCurrencyIntoDB(code, name, sign);
                Optional<Currency> optional = currencyService.getCurrencyByCode(code);
                Currency currency = optional.get();
                objectMapper.writeValue(printWriter, currency);
            } catch (SQLException e) {
                if (e.getMessage().contains("UNIQUE constraint failed")) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    printWriter.write("{\"message\": \"Валюта с таким кодом уже существует\"}");
                } else {
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    printWriter.write("{\"message\": \"Ошибка базы данных\"}");
                }
            }

        }
    }
}
