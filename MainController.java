package com.example.oop_project;

import javafx.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;


public class MainController {

    // User object to store the logged-in user's data
    private HelloController.User loggedInUser;

    @FXML
    private TextField balanceField;  // TextField to display balance on main menu

    // Method to set the logged-in user from HelloController
    public void setUser(HelloController.User user) {
        this.loggedInUser = user;
        // Initialize balance field with current balance
        balanceField.setText(String.format("%.2f", loggedInUser.currentBalance()));
    }

    @FXML
    private void handleCheckBalance(ActionEvent event) {
        // Show the current balance
        showAlert("Account Balance", "Your current balance is: " + loggedInUser.currentBalance());
    }

    private void updateUserFile() {
        String filePath = "src/main/java/com/example/oop_project/credentials.txt";
        StringBuilder updatedContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts[0].equals(loggedInUser.username())) {
                    // Update the current user's entry
                    updatedContent.append(String.format(
                            "%s|%s|%s|%s|%s|%.2f|%s\n",
                            loggedInUser.username(), loggedInUser.password(),
                            loggedInUser.firstName(), loggedInUser.lastName(),
                            loggedInUser.dateOfBirth(), loggedInUser.currentBalance(),
                            loggedInUser.lastTransactions()
                    ));
                } else {
                    updatedContent.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to read the user file.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(updatedContent.toString());
        } catch (IOException e) {
            showAlert("Error", "Failed to update the user file.");
        }
    }


    @FXML
    private void handleCashOut(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Cash Out");
        dialog.setHeaderText("Enter the amount you want to withdraw:");

        dialog.showAndWait().ifPresent(amountStr -> {
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount > 0 && amount <= loggedInUser.currentBalance()) {
                    // Update balance and transaction history
                    double newBalance = loggedInUser.currentBalance() - amount;
                    String newTransaction = String.format("%s: -%.2f", java.time.LocalDate.now(), amount);
                    String updatedTransactions = loggedInUser.lastTransactions().isEmpty() ?
                            newTransaction : loggedInUser.lastTransactions() + "," + newTransaction;

                    loggedInUser = new HelloController.User(
                            loggedInUser.username(), loggedInUser.password(), loggedInUser.firstName(),
                            loggedInUser.lastName(), loggedInUser.dateOfBirth(),
                            newBalance, updatedTransactions);

                    // Update balance display and save changes to file
                    balanceField.setText(String.format("%.2f", newBalance));
                    updateUserFile();
                    showAlert("Cash Out Successful", "You have successfully withdrawn " + amount);
                } else {
                    showAlert("Error", "Insufficient balance or invalid amount.");
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid amount entered.");
            }
        });
    }


    @FXML
    private void handleDeposit(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Deposit");
        dialog.setHeaderText("Enter the amount you want to deposit:");

        dialog.showAndWait().ifPresent(amountStr -> {
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount > 0) {
                    // Update balance and transaction history
                    double newBalance = loggedInUser.currentBalance() + amount;
                    String newTransaction = String.format("%s: +%.2f", java.time.LocalDate.now(), amount);
                    String updatedTransactions = loggedInUser.lastTransactions().isEmpty() ?
                            newTransaction : loggedInUser.lastTransactions() + "," + newTransaction;

                    loggedInUser = new HelloController.User(
                            loggedInUser.username(), loggedInUser.password(), loggedInUser.firstName(),
                            loggedInUser.lastName(), loggedInUser.dateOfBirth(),
                            newBalance, updatedTransactions);

                    // Update balance display and save changes to file
                    balanceField.setText(String.format("%.2f", newBalance));
                    updateUserFile();
                    showAlert("Deposit Successful", "You have successfully deposited " + amount);
                } else {
                    showAlert("Error", "Invalid deposit amount.");
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid amount entered.");
            }
        });
    }


    @FXML
    private void handleDeleteAccount(ActionEvent event) {
        // Confirmation alert before opening the delete window
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to delete your account?");

        if (confirmationAlert.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
            // Close the current window before opening the delete account window
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Load the delete account window
            openDeleteAccountWindow();
        }
    }

    private void openDeleteAccountWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oop_project/deleteaccount.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Account Deletion");
            stage.setScene(new Scene(root, 700, 500)); // Set width and height here
            stage.setResizable(false); // Prevent resizing
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load the delete account window.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
