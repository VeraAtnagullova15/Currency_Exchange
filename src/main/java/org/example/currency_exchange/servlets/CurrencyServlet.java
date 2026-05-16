package org.example.currency_exchange.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.currency_exchange.dto.CurrencyResponseDto;
import org.example.currency_exchange.models.Currency;
import org.example.currency_exchange.service.CurrencyService;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.example.currency_exchange.utils.ValidationUtils.*;


@Slf4j
@WebServlet("/currency/*")
public class CurrencyServlet extends BaseServlet {

    private ObjectMapper objectMapper;
    private CurrencyService currencyService;

    @Override
    public void init() throws ServletException{
        objectMapper = (ObjectMapper)getServletContext().getAttribute("objectMapper");
        currencyService = (CurrencyService)getServletContext().getAttribute("currencyService");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String code = extractValidPathInfoCode(request);
        log.info(code);


        Optional<Currency> optional = currencyService.getCurrencyByCode(code);


        if (optional.isEmpty()) {
            throw new NoSuchElementException("Валюта не найдена");
        } else {
            Currency currency = optional.get();
            CurrencyResponseDto currencyResponseDto = CurrencyResponseDto.currencyToDto(currency);
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(getWriter(response), currencyResponseDto);

        }
    }
}
