package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/RouteSeeker";
    private static final String USER = "postgres";
    private static final String PASSWORD = "developer";
    private static Connection connection = null;

    private Database() {
    }

    public static Connection getConnection() {
        createConnection();
        return connection;
    }


    private static void createConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            System.err.println("nu se poate conecta la baza de date: " + e);
        }
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void rollback() {
        try{
            connection.rollback();
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
    }
}