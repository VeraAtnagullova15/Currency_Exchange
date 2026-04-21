package org.example.currency_exchange.servlets;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.currency_exchange.dao.CurrencyDao;
import org.example.currency_exchange.models.Currency;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private final CurrencyDao currencyDao = new CurrencyDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter printWriter = response.getWriter();

        try {
            List<Currency> currencies = currencyDao.getAllCurrencies();
            response.setStatus(HttpServletResponse.SC_OK);
            printWriter.write(currencies.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            printWriter.write("{\"message\": \"Ошибка базы данных\"}");

        }


    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");
        //TODO: put information in DataBase

        PrintWriter printWriter = response.getWriter();

        if ((name == null) || (name.equals("")) ||
                (code == null) || (code.length() != 3) ||
                (sign == null) || (sign.equals(""))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            printWriter.write("{\"message\": \"Отсутствует одно из полей\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_CREATED);
            printWriter.write("{}");
        }
    }
}
