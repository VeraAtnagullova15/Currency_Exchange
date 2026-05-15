package org.example.currency_exchange.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dto.ExchangeRateResponseDto;
import org.example.currency_exchange.dto.ExchangeRatesRequestDto;
import org.example.currency_exchange.models.ExchangeRate;
import org.example.currency_exchange.service.ExchangeRateService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends BaseServlet {

    private ObjectMapper objectMapper;
    private ExchangeRateService exchangeRateService;

    @Override
    public void init() throws ServletException{
        objectMapper = (ObjectMapper)getServletContext().getAttribute("objectMapper");
        exchangeRateService = (ExchangeRateService)getServletContext().getAttribute("exchangeRateService");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        List<ExchangeRate> exchangeRates = exchangeRateService.getAllExchangeRates();
        List<ExchangeRateResponseDto> exchangeRateResponseDtos = new ArrayList<>();

        for (ExchangeRate ex : exchangeRates) {
            exchangeRateResponseDtos.add(ExchangeRateResponseDto.exchangeRateToDto(ex));
        }

        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(getWriter(response), exchangeRateResponseDtos);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String baseCurrencyCode = extractNotNullParameter(request, "baseCurrencyCode");
        String targetCurrencyCode = extractNotNullParameter(request, "targetCurrencyCode");
        String rate = extractNotNullParameter(request, "rate");
        baseCurrencyCode = baseCurrencyCode.toUpperCase();
        targetCurrencyCode = targetCurrencyCode.toUpperCase();

        ExchangeRatesRequestDto exchangeRatesRequest = new ExchangeRatesRequestDto();
        exchangeRatesRequest.setBaseCurrency(baseCurrencyCode);
        exchangeRatesRequest.setTargetCurrency(targetCurrencyCode);
        exchangeRatesRequest.setRate(rate);
        exchangeRatesRequest.validateExchangeRatesRequest();

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
            ExchangeRateResponseDto exchangeRateResponseDto = ExchangeRateResponseDto.exchangeRateToDto(exchangeRate);
            objectMapper.writeValue(getWriter(response), exchangeRateResponseDto);
        }
    }

}
