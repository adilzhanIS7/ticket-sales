package com.example.demo;

import com.example.demo.models.Ticket;
import com.example.demo.services.DbService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.List;

public class TicketsController {
    @FXML
    public TextField priceFromFilter;
    @FXML
    public TextField priceToFilter;
    @FXML
    private TableView<Ticket> ticketsTable;
    @FXML
    private TextField goingFromFilter;
    @FXML
    private TextField goingToFilter;
    @FXML
    private DatePicker dateWhenFilter;
    @FXML
    private DatePicker dateBackFilter;

    private final DbService dbService = new DbService();

    public void initialize() {
        loadTickets();
    }

    private void loadTickets() {
        List<Ticket> tickets = dbService.getAllTickets();
        ObservableList<Ticket> observableTickets = FXCollections.observableArrayList(tickets);
        ticketsTable.setItems(observableTickets);
    }

    @FXML
    private void filterTickets() {
        String goingFrom = goingFromFilter.getText();
        String goingTo = goingToFilter.getText();
        LocalDate dateWhen = dateWhenFilter.getValue();
        LocalDate dateBack = dateBackFilter.getValue();
        String priceFrom = priceFromFilter.getText();
        String priceTo = priceToFilter.getText();

        List<Ticket> filteredTickets = dbService.getFilteredTickets(goingFrom, goingTo, dateWhen, dateBack, "");
        ObservableList<Ticket> observableFilteredTickets = FXCollections.observableArrayList(filteredTickets);
        ticketsTable.setItems(observableFilteredTickets);
    }

    private String dateToString(LocalDate date) {
        if (date != null) {
            return date.toString();
        } else {
            return null;
        }
    }
}