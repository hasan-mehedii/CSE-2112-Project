package com.example.oop_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;

public class MainController {

    @FXML
    private void handleCheckBalance(ActionEvent event) {
        showAlert("Check Balance", "This feature allows you to check your account balance.");
    }

    @FXML
    private void handleCashOut(ActionEvent event) {
        showAlert("Cash Out", "This feature allows you to withdraw money from your account.");
    }

    @FXML
    private void handleDeposit(ActionEvent event) {
        showAlert("Deposit", "This feature allows you to deposit money into your account.");
    }

    @FXML
    private void handleDeleteAccount(ActionEvent event) {
        showAlert("Delete Account", "This feature allows you to permanently delete your account.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
