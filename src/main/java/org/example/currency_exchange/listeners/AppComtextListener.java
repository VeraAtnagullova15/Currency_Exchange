package org.example.currency_exchange.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.dao.ExchangeRateDao;
import org.example.currency_exchange.service.CurrencyService;
import org.example.currency_exchange.service.ExchangeRateService;
import org.example.currency_exchange.service.ExchangeService;

@WebListener
public class AppComtextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ServletContext context = sce.getServletContext();

        ObjectMapper objectMapper = new ObjectMapper();
        CurrencyDao currencyDao = new CurrencyDao();
        ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

        CurrencyService currencyService = new CurrencyService(currencyDao);
        ExchangeRateService exchangeRateService = new ExchangeRateService(exchangeRateDao, currencyDao);
        ExchangeService exchangeService = new ExchangeService(exchangeRateDao);

        context.setAttribute("objectMapper", objectMapper);
        context.setAttribute("currencyService", currencyService);
        context.setAttribute("exchangeRateService", exchangeRateService);
        context.setAttribute("exchangeService", exchangeService);

    }
}
