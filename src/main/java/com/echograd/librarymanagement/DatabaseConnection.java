package com.echograd.librarymanagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;
    private static DatabaseConnection instance;
    private static String USERNAME;
    private static String PASSWORD;
    private static String URL;
    private static Properties prop;

    private DatabaseConnection() {
        try {
            prop = new Properties();
            InputStream input = new FileInputStream("src/main/resources/config.properties");
            prop.load(input);

            USERNAME = prop.getProperty("db.user");
            PASSWORD = prop.getProperty("db.password");
            URL = "jdbc:mysql://" + prop.getProperty("db.host") + ":" + prop.getProperty("db.port") + "/" + prop.getProperty("db.database");
            config.setJdbcUrl(URL);
            config.setUsername(USERNAME);
            config.setPassword(PASSWORD);
            ds = new HikariDataSource(config);
            input.close();

        } catch (IOException e) {
            System.err.println("Error reading properties file: " + e.getMessage());
        }
    }

    public static DatabaseConnection getInstance() throws IOException {
        if (instance == null){
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
