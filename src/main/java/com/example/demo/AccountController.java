package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;

public class AccountController {
    @FXML
    private TextField userNameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Label loginMessageLabel;

    @FXML
    private TextField registerUserNameTextField;
    @FXML
    private PasswordField registerPasswordPasswordField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField creditCardNumberTextField;
    @FXML
    private Label registrationMessageLabel;

    public void loginButtonOnAction() {
        if (userNameTextField.getText().isBlank() || passwordPasswordField.getText().isBlank()) {
            loginMessageLabel.setText("Please enter the User Name and Password");
        } else {
            if (authenticateUser(userNameTextField.getText(), passwordPasswordField.getText())) {
                openTicketSalesWindow();  // Открыть новое окно после успешного входа
            } else {
                loginMessageLabel.setText("Invalid username or password. Would you like to register?");
            }
        }
    }

    public void registrationButtonOnAction() {
        String username = registerUserNameTextField.getText().trim();
        String password = registerPasswordPasswordField.getText().trim();
        String email = emailTextField.getText().trim();
        String creditCardNumber = creditCardNumberTextField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || creditCardNumber.isEmpty()) {
            registrationMessageLabel.setText("Please fill in all fields.");
        } else if (!password.matches("^(?=.*[0-9])(?=.*[a-z]).{8,}$")) {
            registrationMessageLabel.setText("Password must be at least 8 characters long and include at least one number and one lowercase letter.");
        } else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            registrationMessageLabel.setText("Please enter a valid email address.");
        } else if (creditCardNumber.length() != 16 || !creditCardNumber.matches("\\d+")) {
            registrationMessageLabel.setText("Credit card number must be 16 digits long.");
        } else {
            registerUser(username, password, email, creditCardNumber);
        }
    }

    private boolean authenticateUser(String username, String password) {
        File file = new File("users.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(" ");
                if (userDetails[0].equals(username) && userDetails[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            loginMessageLabel.setText("An error occurred while reading user data.");
        }
        return false;
    }

    private void registerUser(String username, String password, String email, String creditCardNumber) {
        File file = new File("users.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(username + " " + password + " " + email + " " + creditCardNumber + "\n");
            registrationMessageLabel.setText("Registered successfully!");
        } catch (IOException e) {
            registrationMessageLabel.setText("An error occurred while registering the user.");
        }
    }

    private void openTicketSalesWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/Tickets.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Available Tickets");
            stage.show();

            Stage currentStage = (Stage) userNameTextField.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
