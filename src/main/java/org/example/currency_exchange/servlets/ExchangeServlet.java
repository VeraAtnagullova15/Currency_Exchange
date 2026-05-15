package org.example.currency_exchange.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.dto.ExchangeRequestDto;
import org.example.currency_exchange.dto.ExchangeResultResponseDto;
import org.example.currency_exchange.service.ExchangeService;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends BaseServlet {

    private ObjectMapper objectMapper;
    private ExchangeService exchangeService;

    @Override
    public void init() throws ServletException{
        objectMapper = (ObjectMapper)getServletContext().getAttribute("objectMapper");
        exchangeService = (ExchangeService)getServletContext().getAttribute("exchangeService");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String from = extractNotNullParameter(request, "from");
        String to = extractNotNullParameter(request, "to");
        String amount = extractNotNullParameter(request, "amount");
        from = from.toUpperCase();
        to = to.toUpperCase();

        ExchangeRequestDto exchangeRequest = new ExchangeRequestDto();
        exchangeRequest.setFrom(from);
        exchangeRequest.setTo(to);
        exchangeRequest.setAmount(amount);
        exchangeRequest.validateExchangeRequest();

        BigDecimal amountBD = new BigDecimal(amount);

        response.setStatus(HttpServletResponse.SC_OK);
        ExchangeResultResponseDto result = exchangeService.exchange(from, to, amountBD);
        objectMapper.writeValue(getWriter(response), result);

    }

}
