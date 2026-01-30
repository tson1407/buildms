package vn.edu.fpt.swp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection utility class
 */
public class DBConnection {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=smartwms_db;encrypt=true;trustServerCertificate=true";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "YourPassword123";
    
    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQL Server JDBC Driver not found", e);
        }
    }
    
    /**
     * Get database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    
    /**
     * Close database connection
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
