package com.example.demo.services;

import com.example.demo.models.Ticket;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DbService {
    private static final String URL = "jdbc:postgresql://localhost:5432/test_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT id, going_from, going_to, date_when, date_back, price FROM tickets";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Ticket ticket = new Ticket(
                        rs.getInt("id"),
                        rs.getString("going_from"),
                        rs.getString("going_to"),
                        rs.getDate("date_when"),
                        rs.getDate("date_back"),
                        rs.getBigDecimal("price"));
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public List<Ticket> getFilteredTickets(String goingFrom, String goingTo, LocalDate dateWhen, LocalDate dateBack, int priceFrom, int priceTo) {
        List<Ticket> filteredTickets = new ArrayList<>();
        String sql = "SELECT id, going_from, going_to, date_when, date_back, price FROM tickets WHERE 1=1";

        if (goingFrom != null && !goingFrom.isEmpty()) {
            sql += " AND going_from = ?";
        }

        if (goingTo != null && !goingTo.isEmpty()) {
            sql += " AND going_to = ?";
        }

        if (dateWhen != null && dateBack != null) {
            sql += " AND date_when BETWEEN ? AND ?";
        } else if (dateWhen != null) {
            sql += " AND date_when >= ?";
        } else if (dateBack != null) {
            sql += " AND date_back <= ?";
        }

        if (priceFrom > 0 && priceTo > 0) {
            sql += " AND price BETWEEN ? AND ?";
        } else if (priceFrom > 0) {
            sql += " AND price >= ?";
        } else if (priceTo > 0) {
            sql += " AND price <= ?";
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int parameterIndex = 1;

            if (goingFrom != null && !goingFrom.isEmpty()) {
                stmt.setString(parameterIndex++, goingFrom);
            }

            if (goingTo != null && !goingTo.isEmpty()) {
                stmt.setString(parameterIndex++, goingTo);
            }

            if (dateWhen != null && dateBack != null) {
                stmt.setDate(parameterIndex++, Date.valueOf(dateWhen));
                stmt.setDate(parameterIndex++, Date.valueOf(dateBack));
            } else if (dateWhen != null) {
                stmt.setDate(parameterIndex++, Date.valueOf(dateWhen));
            } else if (dateBack != null) {
                stmt.setDate(parameterIndex++, Date.valueOf(dateBack));
            }

            if (priceFrom > 0 && priceTo > 0) {
                stmt.setInt(parameterIndex++, priceFrom);
                stmt.setInt(parameterIndex++, priceTo);
            } else if (priceFrom > 0) {
                stmt.setInt(parameterIndex++, priceFrom);
            } else if (priceTo > 0) {
                stmt.setInt(parameterIndex++, priceTo);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Ticket ticket = new Ticket(
                            rs.getInt("id"),
                            rs.getString("going_from"),
                            rs.getString("going_to"),
                            rs.getDate("date_when"),
                            rs.getDate("date_back"),
                            rs.getBigDecimal("price"));
                    filteredTickets.add(ticket);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return filteredTickets;
    }

    public boolean AuthenticateUser(String username, String password) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean SaveUser(String username, String password, String email, String creditCardNumber) {
        String sql = "INSERT INTO users (username, password, email, card_number) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, creditCardNumber);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
