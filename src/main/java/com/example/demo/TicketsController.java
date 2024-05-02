package com.example.demo;

import com.example.demo.models.Book;
import com.example.demo.services.DbService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import java.util.List;

public class TicketsController {
    @FXML
    private TableView<Book> ticketsTable;

    public void initialize() {
        loadTickets();
    }

    private void loadTickets() {
        DbService dbService = new DbService();
        List<Book> tickets = dbService.getAllBooks(); // Метод, который загружает билеты из БД
        ObservableList<Book> observableTickets = FXCollections.observableArrayList(tickets);
        ticketsTable.setItems(observableTickets);
    }
}
