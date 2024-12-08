package com.example.oop_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.Scanner;

public class SignupController {

    @FXML
    private TextField usernameField, firstNameField, lastNameField;

    @FXML
    private DatePicker dobField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final String credentialsFile = "src/main/java/com/example/oop_project/credentials.txt";

    @FXML
    public void handleSubmit(ActionEvent event) {
        String username = usernameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String dob = dobField.getValue() != null ? dobField.getValue().toString() : "Not Selected";
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || dob.equals("Not Selected")) {
            errorLabel.setText("All fields are required!");
            return;
        }

        if (doesUsernameExist(username)) {
            errorLabel.setText("Error: Username already exists!");
        } else {
            saveNewUser(username, password, firstName, lastName, dob);
            errorLabel.setText("User registered successfully!");

            // Open login window after successful registration
            openLoginWindow(event);
        }
    }

    private boolean doesUsernameExist(String username) {
        try (Scanner scanner = new Scanner(new File(credentialsFile))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");
                if (parts.length > 0 && parts[0].trim().equals(username)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            errorLabel.setText("Error: Credentials file not found.");
        }
        return false;
    }

    private void saveNewUser(String username, String password, String firstName, String lastName, String dob) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(credentialsFile, true))) {
            // Append new user details in the format: username|password|firstname|lastname|dob|balance|transactions
            writer.write(username + "|" + password + "|" + firstName + "|" + lastName + "|" + dob + "|0.00|");
            writer.newLine();
        } catch (IOException e) {
            errorLabel.setText("Error: Unable to save user credentials.");
        }
    }

    private void openLoginWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oop_project/hello-view.fxml"));
            Parent root = loader.load();

            // Close the current stage (signup window)
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Open the login window with the same dimensions
            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(root, 700, 500)); // Ensure consistent size
            loginStage.setResizable(false); // Disable resizing
            loginStage.show();
        } catch (IOException e) {
            errorLabel.setText("Error: Failed to load the login window.");
        }
    }
}
