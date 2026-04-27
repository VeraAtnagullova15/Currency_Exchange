package org.example.currency_exchange.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dto.CurrencyDto;
import org.example.currency_exchange.models.Currency;
import org.example.currency_exchange.service.CurrencyService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private final CurrencyService currencyService= new CurrencyService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

        PrintWriter printWriter = response.getWriter();

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.length() != 4) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            printWriter.write("{\"message\": \"Код валюты отсутствует в адресе\"}");
            return;
        }

        String code = pathInfo.toUpperCase().substring(1);

        try {
            Optional<Currency> optional = currencyService.getCurrencyByCode(code);
            Currency currency = optional.get();
            CurrencyDto currencyDto = CurrencyDto.toDto(currency);

            if (optional.isPresent()) {
                response.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(printWriter, currencyDto);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                printWriter.write("{\"message\": \"Валюта не найдена\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            printWriter.write("{\"message\": \"Ошибка базы данных\"}");
        }
    }
}
