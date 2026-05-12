package org.example.currency_exchange.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currency_exchange.exceptions.AlreadyExistsException;
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

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        PrintWriter printWriter = response.getWriter();

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (NoSuchElementException elementException) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            printWriter.write("{\"message\": \"" + elementException.getMessage() + "\"}");
        } catch (ValidationException validationException) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            printWriter.write("{\"message\": \"" + validationException.getMessage() + "\"}");
        } catch (AlreadyExistsException existsException) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            printWriter.write("{\"message\": \"" + existsException.getMessage() + "\"}");
        } catch (DataBaseException dataBaseException) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            printWriter.write("{\"message\": \"" + dataBaseException.getMessage() + "\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            printWriter.write("{\"message\": \"Ошибка выполнения запроса\"}");
        }
    }
}
