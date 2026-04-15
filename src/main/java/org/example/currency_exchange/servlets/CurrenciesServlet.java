package org.example.currency_exchange.servlets;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        //TODO: get information from DataBase
        PrintWriter printWriter = response.getWriter();
        printWriter.write("[]");

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        //TODO: put information in DataBase
        response.setStatus(HttpServletResponse.SC_CREATED);
        PrintWriter printWriter = response.getWriter();
        printWriter.write("{}");
    }
}
