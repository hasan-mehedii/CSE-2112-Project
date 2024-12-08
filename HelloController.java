package com.example.oop_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.scene.Parent;

public class HelloController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private User loggedInUser;

    // Handle the login action
    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Check if the credentials are valid and retrieve user data
        if (checkCredentials(username, password)) {
            // After successful login, you can use the loggedInUser object for user-specific data
            openMainMenuWindow();
        } else {
            errorLabel.setText("Invalid username or password!");
        }
    }
    private boolean checkCredentials(String username, String password) {
        String credentialsFile = "src/main/java/com/example/oop_project/credentials.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Line from file: '" + line + "'");
                String[] parts = line.split("\\|");

                // Ensure we handle lines with missing components
                if (parts.length >= 6) {
                    String fileUsername = parts[0].trim();
                    String filePassword = parts[1].trim();

                    System.out.println("Comparing Username: '" + fileUsername + "' with '" + username + "'");
                    System.out.println("Comparing Password: '" + filePassword + "' with '" + password + "'");

                    if (fileUsername.equals(username) && filePassword.equals(password)) {
                        System.out.println("Login successful for user: " + username);

                        // Handle missing lastTransactions field
                        String lastTransactions = parts.length == 7 ? parts[6] : "No transactions available";

                        loggedInUser = new User(
                                parts[0],
                                parts[1],
                                parts[2],
                                parts[3],
                                parts[4],
                                Double.parseDouble(parts[5]),
                                lastTransactions
                        );
                        return true;
                    }
                } else {
                    System.out.println("Skipping line due to unexpected format: " + line);
                }
            }
        } catch (IOException e) {
            errorLabel.setText("Error reading credentials file.");
        }
        return false;
    }


    // Open the Main Menu window after successful login
    private void openMainMenuWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oop_project/Mainmenu.fxml")); // Make sure the path is correct
            Parent root = loader.load();

            // Pass the logged-in user data to the MainController
            MainController mainController = loader.getController();
            mainController.setUser(loggedInUser);

            // Get the current stage and set the new scene
            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            Scene mainMenuScene = new Scene(root);
            currentStage.setScene(mainMenuScene);
            currentStage.sizeToScene(); // Adjust stage size based on the scene's content
            currentStage.show();

        } catch (IOException e) {
            errorLabel.setText("Error: Unable to load Main Menu.");
        }
    }

    // Handle the Sign-Up action (empty for now)
    @FXML
    public void handleSignUp(ActionEvent actionEvent) {
        try {
            // Load the sign-up FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oop_project/signup.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            Scene signUpScene = new Scene(root);
            currentStage.setScene(signUpScene);
            currentStage.sizeToScene();
            currentStage.show();

        } catch (IOException e) {
            errorLabel.setText("Error: Unable to load Sign-Up window.");
        }
    }


    // A simple User class to hold user data
    public record User(String username, String password, String firstName, String lastName, String dateOfBirth,
                       double currentBalance, String lastTransactions) {
    }
}
