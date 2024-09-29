package com.mycompany.library.database;

import com.mycompany.library.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static String dbUrl;
    private static String username;
    private static String password;

    static {
        loadDatabaseConfig();
    }

    private static void loadDatabaseConfig() {
        DatabaseConfig config = new DatabaseConfig();
        dbUrl = config.getDbUrl();
        username = config.getDbUsername();
        password = config.getDbPassword();
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, username, password);
    }
}
