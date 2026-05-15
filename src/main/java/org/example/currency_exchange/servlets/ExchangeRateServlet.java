package org.example.currency_exchange.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dto.ExchangeRatesRequestDto;
import org.example.currency_exchange.dto.ExchangeRateRequestDto;
import org.example.currency_exchange.dto.ExchangeRateResponseDto;
import org.example.currency_exchange.models.ExchangeRate;
import org.example.currency_exchange.service.ExchangeRateService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.example.currency_exchange.utils.ValidationUtils.*;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet {

    private ObjectMapper objectMapper;
    private ExchangeRateService exchangeRateService;

    @Override
    public void init() throws ServletException{
        objectMapper = (ObjectMapper)getServletContext().getAttribute("objectMapper");
        exchangeRateService = (ExchangeRateService)getServletContext().getAttribute("exchangeRateService");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {


        String pairCurrencies = extractNotNullPathInfo(request, "Коды валют отсутствуют в запросе");
        validateCurrencyPairLength(pairCurrencies, "Валютная пара должна состоять из 6 символов");

        String baseCurrency = pairCurrencies.substring(0, 3);
        String targetCurrency = pairCurrencies.substring(3);

        ExchangeRateRequestDto exchangeRateRequest = new ExchangeRateRequestDto();
        exchangeRateRequest.setBaseCurrency(baseCurrency);
        exchangeRateRequest.setTargetCurrency(targetCurrency);

        exchangeRateRequest.validateExchangeRateRequest();

        Optional<ExchangeRate> optional = exchangeRateService.getExchangeRateByCodes(baseCurrency, targetCurrency);

        if (optional.isEmpty()) {
            throw new NoSuchElementException("Обменный курс не найден");
        } else {
            ExchangeRate exchangeRate = optional.get();
            ExchangeRateResponseDto exchangeRateResponseDto = ExchangeRateResponseDto.exchangeRateToDto(exchangeRate);
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(getWriter(response), exchangeRateResponseDto);
        }
    }



    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        if ("PATCH".equalsIgnoreCase(request.getMethod())) {

            String pairCurrencies = extractNotNullPathInfo(request, "Коды валют отсутствуют в запросе");
            validateCurrencyPairLength(pairCurrencies, "Валютная пара должна состоять из 6 символов");

            String baseCurrency = pairCurrencies.substring(0, 3);
            String targetCurrency = pairCurrencies.substring(3);
            String body = request.getReader().readLine();
            validatePatchBodyRequest(body, "Отсутствует поле rate");
            String rate = body.split("=")[1];

            ExchangeRatesRequestDto patchRequestDto = new ExchangeRatesRequestDto();
            patchRequestDto.setBaseCurrency(baseCurrency);
            patchRequestDto.setTargetCurrency(targetCurrency);
            patchRequestDto.setRate(rate);
            patchRequestDto.validateExchangeRatesRequest();

            BigDecimal rateBD = new BigDecimal(rate);

            Optional<ExchangeRate> optionalExchangeRate = exchangeRateService.getExchangeRateByCodes(baseCurrency, targetCurrency);
            if (optionalExchangeRate.isEmpty()) {
                throw new NoSuchElementException("Обменный курс не найден");
            } else {
                exchangeRateService.updateExchangeRate(baseCurrency, targetCurrency, rateBD);
                Optional<ExchangeRate> updated = exchangeRateService.getExchangeRateByCodes(baseCurrency,targetCurrency);
                ExchangeRate exchangeRate = updated.get();
                response.setStatus(HttpServletResponse.SC_OK);
                ExchangeRateResponseDto exchangeRateResponseDto = ExchangeRateResponseDto.exchangeRateToDto(exchangeRate);
                objectMapper.writeValue(getWriter(response), exchangeRateResponseDto);
            }

        } else {
            super.service(request, response);
        }
    }

}
