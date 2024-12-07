package com.example.oop_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private Label errorLabel; // Add a label in FXML to display errors

    private final String credentialsFile = "src/main/java/com/example/oop_project/credentials.txt"; // Path to the credentials file

    @FXML
    public void handleSubmit(ActionEvent event) {
        String username = usernameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String dob = dobField.getValue() != null ? dobField.getValue().toString() : "Not Selected";
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username and Password cannot be empty!");
            return;
        }

        if (doesUsernameExist(username)) {
            errorLabel.setText("Error: Username already exists!");
        } else {
            saveNewUser(username, password);
            errorLabel.setText("User registered successfully!");
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

    private void saveNewUser(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(credentialsFile, true))) {
            writer.write(username + "|" + password);
            writer.newLine(); // Ensures the next entry is written on a new line
        } catch (IOException e) {
            errorLabel.setText("Error: Unable to save user credentials.");
        }
    }

}
