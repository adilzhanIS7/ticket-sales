package com.example.demo;

import com.example.demo.models.Ticket;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.DbService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class TicketsController {
    @FXML
    public TextField priceFromFilter;
    @FXML
    public TextField priceToFilter;
    public Button showPurchasedTicketsButton;
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

        priceFromFilter.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("\\d*") ? change : null)));
        priceToFilter.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("\\d*") ? change : null)));

        TableColumn<Ticket, Void> buyColumn = new TableColumn<>("Купить");
        buyColumn.setCellFactory(param -> new TableCell<>() {
            private final Button buyButton = new Button("Купить");

            {
                buyButton.setOnAction(event -> {
                    Ticket ticket = getTableView().getItems().get(getIndex());
                    System.out.println("Покупка билета: " + ticket.getId());
                    String token = AuthenticationService.getTokenFromFile();
                    int userId = AuthenticationService.getUserIdFromToken(token);
                    DbService.saveUserTicket(userId, ticket.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buyButton);
                }
            }
        });
        ticketsTable.getColumns().add(buyColumn);
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
        int priceFrom = 0;
        int priceTo = 0;

        if (!priceFromFilter.getText().isEmpty()) {
            priceFrom = Integer.parseInt(priceFromFilter.getText());
        }
        if (!priceToFilter.getText().isEmpty()) {
            priceTo = Integer.parseInt(priceToFilter.getText());
        }

        List<Ticket> filteredTickets = dbService.getFilteredTickets(goingFrom.toUpperCase(), goingTo.toUpperCase(), dateWhen, dateBack, priceFrom, priceTo);
        ObservableList<Ticket> observableFilteredTickets = FXCollections.observableArrayList(filteredTickets);
        ticketsTable.setItems(observableFilteredTickets);
    }

    @FXML
    private void showPurchasedTickets() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/PurchasedTickets.fxml"));
            Parent root = loader.load();

            PurchasedTicketsController controller = loader.getController();

            // Загрузка данных о приобретенных билетах
            String token = AuthenticationService.getTokenFromFile();
            int userId = AuthenticationService.getUserIdFromToken(token);
            List<Ticket> purchasedTickets = dbService.getPurchasedTickets(userId);
            controller.setPurchasedTickets(purchasedTickets);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Your Tickets");
            stage.show();

            // Закрытие текущего окна
            Stage currentStage = (Stage) showPurchasedTicketsButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}