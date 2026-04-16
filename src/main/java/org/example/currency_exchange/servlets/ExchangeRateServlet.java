package org.example.currency_exchange.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String couple = request.getPathInfo().substring(1);

        PrintWriter printWriter = response.getWriter();

        if ((couple == null) || (couple.equals("") || (couple.length() != 6))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            printWriter.write("{\"message\": \"Код валютной пары отсутствует в адресе\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            printWriter.write(couple);
        }
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        if ("PATCH".equalsIgnoreCase(request.getMethod())) {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            PrintWriter printWriter = response.getWriter();

            String rateParameter = request.getParameter("rate");
            if ((rateParameter == null) || (rateParameter.equals(""))) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                printWriter.write("{\"message\": \"Отсутствует поле rate\"}");
            } else {
                try {
                    double rate = Double.parseDouble(request.getParameter("rate"));
                    if (rate <= 0) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        printWriter.write("{\"message\": \"Некорректное значение rate\"}");
                    } else {
                        response.setStatus(HttpServletResponse.SC_OK);
                        printWriter.write(String.valueOf(rate));
                        // TODO: change rate in DB
                    }
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    printWriter.write("{\"message\": \"Rate должен быть числом\"}");
                }
            }

        } else {
            super.service(request, response);
        }
    }

}
