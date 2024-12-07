package com.example.oop_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SignupController {

    @FXML
    private TextField usernameField, firstNameField, lastNameField;

    @FXML
    private DatePicker dobField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void handleSubmit(ActionEvent event) {
        // Retrieving the values from the form fields
        String username = usernameField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String dob = dobField.getValue() != null ? dobField.getValue().toString() : "Not Selected";
        String password = passwordField.getText();

        // Output the values for now (you can replace this with actual processing logic)
        System.out.println("Username: " + username);
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Date of Birth: " + dob);
        System.out.println("Password: " + password);
    }
}
