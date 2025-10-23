package com.codeup.minitiendariwi.db;

import java.sql.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConnectionFactory {
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Properties props = new Properties();
                props.load(new FileInputStream("src/main/resources/db.properties"));
                String url = props.getProperty("url");
                String user = props.getProperty("user");
                String password = props.getProperty("password");
                connection = DriverManager.getConnection(url, user, password);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
