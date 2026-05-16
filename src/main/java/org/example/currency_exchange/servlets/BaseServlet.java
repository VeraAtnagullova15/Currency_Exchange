package org.example.currency_exchange.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static org.example.currency_exchange.utils.ValidationUtils.*;

public abstract class BaseServlet extends HttpServlet {

    protected PrintWriter getWriter(HttpServletResponse response) throws IOException {
        return response.getWriter();
    }

    protected String extractValidPathInfoCode(HttpServletRequest request) {
        String code = request.getPathInfo();
        if (code == null || code.equals("/")) {
            throw new IllegalArgumentException("Пустое значение");
        }
        code = code.substring(1).toUpperCase();
        validateCodeLength(code, "Поле code должно состоять из трех букв");
        validateCodeValue(code, "Поле code должно состоять только из латинских букв");
        return code;
    }

    protected String extractValidPathInfoPairCurrencies(HttpServletRequest request) {
        String pair = request.getPathInfo();
        if (pair == null || pair.equals("/")) {
            throw new IllegalArgumentException("Пустое значение");
        }
        pair = pair.substring(1).toUpperCase();
        validateCurrencyPairLength(pair, "Валютная пара должна состоять из 6 символов");
        return pair;
    }

    protected String extractNotNullParameter(HttpServletRequest request, String parameter) {
        String value = request.getParameter(parameter);
        if (value == null) {
            throw new IllegalArgumentException("Отсутствует поле " + parameter);
        }
        return value;
    }

    protected String extractNotNullCode(HttpServletRequest request, String code) {
        String value = request.getParameter(code);
        if (value == null) {
            throw new IllegalArgumentException("Отсутствует поле " + code);
        }
        return value.toUpperCase();
    }

    protected String extractValidParameterFromBodyRequest(HttpServletRequest request) throws IOException {
        String body = request.getReader().readLine();
        validatePatchBodyRequest(body, "Отсутствует поле rate");
        String rate = body.split("=")[1];
        return rate;
    }

}
