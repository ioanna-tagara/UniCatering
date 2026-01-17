package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/student_catering";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Ergasia2025!";

    public static Connection getConnection() throws SQLException {
        try {
            // Load the PostgreSQL driver (optional in newer JDBC versions)
            Class.forName("org.postgresql.Driver");
            
            // Create and return the connection
            return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC driver not found.", e);
        } catch (SQLException e) {
            throw new SQLException("Failed to establish a database connection.", e);
        }
    }
}
