package org.example.currency_exchange.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.dao.ExchangeRateDao;
import org.example.currency_exchange.dto.ExchangeRateDto;
import org.example.currency_exchange.models.ExchangeRate;
import org.example.currency_exchange.service.CurrencyService;
import org.example.currency_exchange.service.ExchangeRateService;
import org.example.currency_exchange.service.ExchangeService;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.example.currency_exchange.utils.ValidationUtils.*;


@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private ExchangeRateService exchangeRateService;

    @Override
    public void init() throws ServletException{
        this.objectMapper = new ObjectMapper();
        this.exchangeRateService = new ExchangeRateService(new ExchangeRateDao(new CurrencyDao()), new CurrencyDao());
    }

    public ExchangeRateServlet() {
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String pathInfo = request.getPathInfo().substring(1);
        validateNotBlank(pathInfo, "Код валюты отсутствует в адресе");

        String pairCurrencies = pathInfo.toUpperCase();
        validateCurrencyPairLength(pairCurrencies, "Валютная пара должна состоять из 6 символов");

        String baseCurrency = pairCurrencies.substring(0, 3);
        String targetCurrency = pairCurrencies.substring(3);

        PrintWriter printWriter = response.getWriter();

        validateNotBlank(baseCurrency, "Код валюты отсутствует в адресе");
        validateNotBlank(targetCurrency, "Код валюты отсутствует в адресе");
        validateCodeLength(baseCurrency, "Код валюты должен состоять из трех букв");
        validateCodeLength(targetCurrency, "Код валюты должен состоять из трех букв");
        validateCodeValue(baseCurrency, "Код валюты должен состоять только из латинских букв");
        validateCodeValue(targetCurrency, "Код валюты должен состоять только из латинских букв");

        Optional<ExchangeRate> optional = exchangeRateService.getExchangeRateByCodes(baseCurrency, targetCurrency);

        if (optional.isEmpty()) {
            throw new NoSuchElementException("Обменный курс не найден");
        } else {
            ExchangeRate exchangeRate = optional.get();
            ExchangeRateDto exchangeRateDto = ExchangeRateDto.exchangeRateToDto(exchangeRate);
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(printWriter, exchangeRateDto);
        }
    }



    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        if ("PATCH".equalsIgnoreCase(request.getMethod())) {

            String pathInfo = request.getPathInfo().substring(1);
            validateNotBlank(pathInfo, "Код валюты отсутствует в адресе");

            String pairCurrencies = pathInfo.toUpperCase();
            validateCurrencyPairLength(pairCurrencies, "Валютная пара должна состоять из 6 символов");

            String baseCurrency = pairCurrencies.substring(0, 3);
            String targetCurrency = pairCurrencies.substring(3);
            String body = request.getReader().readLine();
            System.out.println("body: " + body);
            validatePatchBodyRequest(body, "Отсутствует поле rate");
            String rate = body.split("=")[1];

            PrintWriter printWriter = response.getWriter();

            validateNotBlank(baseCurrency, "Код валюты отсутствует в адресе");
            validateNotBlank(targetCurrency, "Код валюты отсутствует в адресе");
            validateNotBlank(rate, "rate не указан");
            validateCodeLength(baseCurrency, "Код валюты должен состоять из трех букв");
            validateCodeLength(targetCurrency, "Код валюты должен состоять из трех букв");
            validateCodeValue(baseCurrency, "Код валюты должен состоять только из латинских букв");
            validateCodeValue(targetCurrency, "Код валюты должен состоять только из латинских букв");
            validateRateAndAmountValue(rate, "Значение rate должно состоять из цифр");

            BigDecimal rateBD = new BigDecimal(rate);

            Optional<ExchangeRate> optionalExchangeRate = exchangeRateService.getExchangeRateByCodes(baseCurrency, targetCurrency);
            if (optionalExchangeRate.isEmpty()) {
                throw new NoSuchElementException("Обменный курс не найден");
            } else {
                exchangeRateService.updateExchangeRate(baseCurrency, targetCurrency, rateBD);
                Optional<ExchangeRate> updated = exchangeRateService.getExchangeRateByCodes(baseCurrency,targetCurrency);
                ExchangeRate exchangeRate = updated.get();
                response.setStatus(HttpServletResponse.SC_OK);
                ExchangeRateDto exchangeRateDto = ExchangeRateDto.exchangeRateToDto(exchangeRate);
                objectMapper.writeValue(printWriter, exchangeRateDto);
            }

        } else {
            super.service(request, response);
        }
    }

}
