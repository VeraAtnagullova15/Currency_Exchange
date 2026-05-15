package org.example.currency_exchange.servlets;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.currency_exchange.dto.CurrencyRequestDto;
import org.example.currency_exchange.dto.CurrencyResponseDto;
import org.example.currency_exchange.models.Currency;
import org.example.currency_exchange.service.CurrencyService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@WebServlet("/currencies")
public class CurrenciesServlet extends BaseServlet {

    private ObjectMapper objectMapper;
    private CurrencyService currencyService;

    @Override
    public void init() throws ServletException{
        objectMapper = (ObjectMapper)getServletContext().getAttribute("objectMapper");
        currencyService = (CurrencyService)getServletContext().getAttribute("currencyService");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        List<Currency> currencies = currencyService.getAllCurrencies();
        List<CurrencyResponseDto> currencyResponseDtos = new ArrayList<>();
        for (Currency currency : currencies) {
            currencyResponseDtos.add(CurrencyResponseDto.currencyToDto(currency));
        }
        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(getWriter(response), currencyResponseDtos);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String name = extractNotNullParameter(request, "name");
        String code = extractNotNullParameter(request, "code");
        String sign = extractNotNullParameter(request, "sign");
        code = code.toUpperCase();

        CurrencyRequestDto currencyRequest = new CurrencyRequestDto();
        currencyRequest.setName(name);
        currencyRequest.setCode(code);
        currencyRequest.setSign(sign);

        currencyRequest.validateCurrencyRequest();

        response.setStatus(HttpServletResponse.SC_CREATED);

        currencyService.putCurrencyIntoDB(code, name, sign);
        Optional<Currency> optional = currencyService.getCurrencyByCode(code);
        if (optional.isEmpty()) {
            throw new NoSuchElementException("Валюта не найдена");
        } else {
            Currency currency = optional.get();
            CurrencyResponseDto currencyResponseDto = CurrencyResponseDto.currencyToDto(currency);
            objectMapper.writeValue(getWriter(response), currencyResponseDto);

        }
    }
}
