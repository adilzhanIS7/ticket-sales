package com.example.demo.services;

import com.example.demo.models.Book;
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

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, name, author FROM books";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("author"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
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

    public List<Ticket> getFilteredTickets(String goingFrom, String goingTo, LocalDate dateWhen, LocalDate dateBack, String price) {
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

        if (price != null && !price.isEmpty()) {
            sql += " AND price = ?";
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

            if (price != null && !price.isEmpty()) {
                stmt.setString(parameterIndex, price);
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

}
