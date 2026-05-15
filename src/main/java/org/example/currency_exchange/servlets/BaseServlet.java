package org.example.currency_exchange.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public abstract class BaseServlet extends HttpServlet {

    protected PrintWriter getWriter(HttpServletResponse response) throws IOException {
        return response.getWriter();
    }

    protected String extractNotNullPathInfo(HttpServletRequest request, String message) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            throw new IllegalArgumentException(message);
        }
        return pathInfo.substring(1).toUpperCase();
    }

    protected String extractNotNullParameter(HttpServletRequest request, String parameter) {
        String value = request.getParameter(parameter);
        if (value == null) {
            throw new IllegalArgumentException("Отсутствует поле " + parameter);
        }
        return value;

    }

}
