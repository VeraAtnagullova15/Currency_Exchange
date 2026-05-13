package org.example.currency_exchange.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.example.currency_exchange.dto.ExchangeResult;
import org.example.currency_exchange.service.ExchangeService;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

import static org.example.currency_exchange.utils.ValidationUtils.*;


@NoArgsConstructor
@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private ExchangeService exchangeService;

    @Override
    public void init() throws ServletException{
        objectMapper = (ObjectMapper)getServletContext().getAttribute("objectMapper");
        exchangeService = (ExchangeService)getServletContext().getAttribute("exchangeService");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String amount = request.getParameter("amount");
        from = from.toUpperCase();
        to = to.toUpperCase();

        PrintWriter printWriter = response.getWriter();

        validateNotBlank(from, "Отсутствует поле from");
        validateNotBlank(to, "Отсутствует поле to");
        validateNotBlank(amount, "Отсутствует поле amount");
        validateCodeLength(from, "Код валюты from должен состоять из трех букв");
        validateCodeLength(to, "Код валюты to должен состоять из трех букв");
        validateRateAndAmountValue(amount, "Значение amount должно состоять из цифр");

        BigDecimal amountBD = new BigDecimal(amount);

        response.setStatus(HttpServletResponse.SC_OK);
        ExchangeResult result = exchangeService.exchange(from, to, amountBD);
        objectMapper.writeValue(printWriter, result);

    }

}
