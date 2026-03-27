package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/Restaurant_db");
    private static final String USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String PASS = System.getenv().getOrDefault("DB_PASS", "12121212");

    private static DBConnection instance;
    private final Connection connection;

    private DBConnection() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}