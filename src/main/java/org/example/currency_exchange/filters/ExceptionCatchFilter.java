package org.example.currency_exchange.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.exceptions.DataBaseException;
import org.example.currency_exchange.exceptions.ValidationException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;


@WebFilter("/*")
public class ExceptionCatchFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse)servletResponse;
        PrintWriter printWriter = response.getWriter();

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (NoSuchElementException elementException) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            printWriter.write("{\"message\": \"Указанная валюта не найдена\"}");
        } catch (ValidationException validationException) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            printWriter.write("{\"message\": \"" + validationException.getMessage() + "\"}");
        } catch (DataBaseException dataBaseException) {
            if (dataBaseException.getMessage().contains("UNIQUE constraint failed")) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                printWriter.write("{\"message\": \"Поле с такими данными уже существует\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                printWriter.write("{\"message\": \"Ошибка базы данных\"}");
            }
        }
    }
}
