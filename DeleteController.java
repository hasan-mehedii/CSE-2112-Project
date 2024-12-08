package com.example.oop_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DeleteController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleDeleteAccount(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username or password cannot be empty.");
            return;
        }

        // Validate credentials and delete the account
        if (deleteAccountFromFile(username, password)) {
            showAlert("Success", "Account deleted successfully.");
            returnToLogin(event);
        } else {
            showAlert("Error", "Invalid username or password. Please try again.");
        }
    }

    private boolean deleteAccountFromFile(String username, String password) {
        File file = new File("src/main/java/com/example/oop_project/credentials.txt");
        List<String> accounts = new ArrayList<>();
        boolean accountDeleted = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    accountDeleted = true; // Found account to delete
                } else {
                    accounts.add(line); // Keep other accounts
                }
            }
        } catch (IOException e) {
            showAlert("Error", "Error reading the accounts file.");
            return false;
        }

        if (!accountDeleted) {
            // If no matching account was found
            return false;
        }

        // Rewrite the file without the deleted account
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String account : accounts) {
                writer.write(account);
                writer.newLine();
            }
        } catch (IOException e) {
            showAlert("Error", "Error updating the accounts file.");
            return false;
        }

        return true;
    }


    private void returnToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oop_project/hello-view.fxml"));
            Parent root = loader.load();

            // Close the current stage (delete window)
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Open the login window with the same size as before
            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(root, 700, 500)); // Explicitly set the width and height
            loginStage.setResizable(false); // Optionally, make it non-resizable
            loginStage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load the login window.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
