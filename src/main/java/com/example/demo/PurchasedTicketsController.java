package com.example.demo;

import com.example.demo.models.Ticket;
import com.example.demo.services.DbService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class PurchasedTicketsController {

    @FXML
    private TableView<Ticket> purchasedTicketsTable;

    @FXML
    private TableColumn<Ticket, Integer> idColumn;

    @FXML
    private TableColumn<Ticket, String> goingFromColumn;

    @FXML
    private TableColumn<Ticket, String> goingToColumn;

    @FXML
    private TableColumn<Ticket, String> dateWhenColumn;

    @FXML
    private TableColumn<Ticket, String> dateBackColumn;

    @FXML
    private TableColumn<Ticket, Integer> priceColumn;

    @FXML
    private void initialize() {
        TableColumn<Ticket, Void> deleteColumn = new TableColumn<>("Удалить");
        deleteColumn.setCellFactory(param -> new TableCell<Ticket, Void>() {
            private final Button deleteButton = new Button("Удалить");

            {
                deleteButton.setOnAction(event -> {
                    Ticket ticket = getTableView().getItems().get(getIndex());
                    deleteTicket(ticket);
                    getTableView().getItems().remove(ticket); // Обновление таблицы
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
        purchasedTicketsTable.getColumns().add(deleteColumn);
    }

    public void setPurchasedTickets(List<Ticket> purchasedTickets) {
        ObservableList<Ticket> observablePurchasedTickets = FXCollections.observableArrayList(purchasedTickets);
        purchasedTicketsTable.setItems(observablePurchasedTickets);
    }

    @FXML
    private void handleBackButton() {
        Stage currentStage = (Stage) purchasedTicketsTable.getScene().getWindow();
        currentStage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/Tickets.fxml"));
            Stage mainStage = new Stage();
            mainStage.setScene(new Scene(loader.load()));
            mainStage.setTitle("Главное окно");
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteTicket(Ticket ticket) {
        DbService.deleteTicket(ticket.getId());
    }
}