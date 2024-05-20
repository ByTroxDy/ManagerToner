package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
	private static final String URL = "jdbc:mysql://localhost/rentingtoners";
    private static final String USER = "manager";
    private static final String PASSWORD = "manager";
    private static Connection conn;

    public static Connection getConnection() throws SQLException {
        if (conn == null) {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return conn;
    }

    public static void closeConnection() throws SQLException {
        if (conn != null) {
            conn.close();
            conn = null;
        }
    }
}
