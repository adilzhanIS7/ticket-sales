package com.example.demo;

import com.example.demo.models.Ticket;
import com.example.demo.services.DbService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class TicketsController {
    public TextField goingFromFilter;
    public TextField goingToFilter;
    @FXML
    private TableView<Ticket> ticketsTable;
    private DbService dbService = new DbService();

    public void initialize() {
        loadTickets();
    }

    private void loadTickets() {
        String goingFrom = goingFromFilter.getText();
        String goingTo = goingToFilter.getText();

        List<Ticket> tickets = dbService.getFilteredTickets(goingFrom, goingTo);

        ObservableList<Ticket> observableTickets = FXCollections.observableArrayList(tickets);
        ticketsTable.setItems(observableTickets);
    }

    @FXML
    private void filterTickets() {
        String goingFrom = goingFromFilter.getText();
        String goingTo = goingToFilter.getText();
        List<Ticket> filteredTickets = dbService.getFilteredTickets(goingFrom, goingTo);
        ObservableList<Ticket> observableFilteredTickets = FXCollections.observableArrayList(filteredTickets);
        ticketsTable.setItems(observableFilteredTickets);
    }
}
