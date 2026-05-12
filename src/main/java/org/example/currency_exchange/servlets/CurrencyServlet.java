package org.example.currency_exchange.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.dto.CurrencyDto;
import org.example.currency_exchange.models.Currency;
import org.example.currency_exchange.service.CurrencyService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.example.currency_exchange.utils.ValidationUtils.*;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private CurrencyService currencyService;

    @Override
    public void init() throws ServletException{
        objectMapper = (ObjectMapper)getServletContext().getAttribute("objectMapper");
        currencyService = (CurrencyService)getServletContext().getAttribute("currencyService");
    }

    public CurrencyServlet() {
    }


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
            throw new NoSuchElementException("Валюта не найдена");
        } else {
            Currency currency = optional.get();
            CurrencyDto currencyDto = CurrencyDto.currencyToDto(currency);
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(printWriter, currencyDto);

        }
    }
}
