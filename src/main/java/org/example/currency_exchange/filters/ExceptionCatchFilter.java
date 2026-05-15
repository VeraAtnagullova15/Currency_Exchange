package org.example.currency_exchange.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.currency_exchange.exceptions.AlreadyExistsException;
import org.example.currency_exchange.exceptions.DataBaseException;
import org.example.currency_exchange.exceptions.ValidationException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;


@Slf4j
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
            log.warn("Не найдено", elementException);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            printWriter.write("{\"message\": \"" + elementException.getMessage() + "\"}");
        } catch (ValidationException validationException) {
            log.warn("Ошибка валидации", validationException);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            printWriter.write("{\"message\": \"" + validationException.getMessage() + "\"}");
        } catch (AlreadyExistsException existsException) {
            log.warn("Поле уже существует", existsException);
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            printWriter.write("{\"message\": \"" + existsException.getMessage() + "\"}");
        } catch (IllegalArgumentException argumentException) {
            log.error("Отсутствует поле в запросе", argumentException);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            printWriter.write("{\"message\": \"" + argumentException.getMessage() + "\"}");
        } catch (DataBaseException dataBaseException) {
            log.error("Ошибка при обращении к БД");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            printWriter.write("{\"message\": \"" + dataBaseException.getMessage() + "\"}");
        } catch (Exception e) {
            log.error("Непредвиденная ошибка");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            printWriter.write("{\"message\": \"Ошибка выполнения запроса\"}");
        }
    }
}
