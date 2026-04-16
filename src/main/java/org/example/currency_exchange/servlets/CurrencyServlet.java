package org.example.currency_exchange.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String resp = request.getPathInfo().substring(1);

        PrintWriter printWriter = response.getWriter();

        if ((resp.equals("")) || (resp == null) || (resp.length() != 3)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            printWriter.write("{\"message\": \"Код валюты отсутствует в адресе\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            printWriter.write(resp);
        }

    }
}
