package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

 class DatabaseTest {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/blooddonationmanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Connection to the database was successful!");
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
}
