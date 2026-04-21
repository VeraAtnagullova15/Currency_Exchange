package org.example.currency_exchange.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

        private static final String URL =
                "jdbc:sqlite:/Users/veraatnagullova/IdeaProjects/Currency_Exchange/src/main/resources/currency_exchange.db";

        static {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("SQLite driver not founded", e);
            }
        }

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL);
        }

}
