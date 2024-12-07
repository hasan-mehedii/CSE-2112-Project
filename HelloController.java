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
import javafx.scene.input.KeyCode;

import java.io.IOException;

public class HelloController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (checkCredentials(username, password)) {
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
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    String fileUsername = parts[0].trim();
                    String filePassword = parts[1].trim();
                    //System.out.println("Checking: " + fileUsername + " | " + filePassword);
                    if (fileUsername.equals(username) && filePassword.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
            errorLabel.setText("Error reading credentials file.");
        }
        return false;
    }


    private void openMainMenuWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oop_project/Mainmenu.fxml"));
            Parent root = loader.load();

            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            Stage mainMenuStage = getStage(currentStage, root);

            mainMenuStage.sizeToScene();
            currentStage.close();
            mainMenuStage.show();

        } catch (IOException e) {
            //e.printStackTrace();
            errorLabel.setText("Error: Unable to load Main Menu.");
        }
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oop_project/signup.fxml"));
            Parent root = loader.load();

            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            Stage signUpStage = getStage(root, currentStage);

            currentStage.close();
            signUpStage.show();

        } catch (IOException e) {
            errorLabel.setText("Error: Unable to load Sign Up.");
            //e.printStackTrace();
        }
    }

    private static Stage getStage(Parent root, Stage currentStage) {
        Stage signUpStage = new Stage();
        Scene scene = new Scene(root);
        signUpStage.setScene(scene);
        signUpStage.setTitle("Sign Up");

        // Apply the dimensions and position of the current stage to the new stage
        signUpStage.setWidth(currentStage.getWidth());
        signUpStage.setHeight(currentStage.getHeight());
        signUpStage.setX(currentStage.getX());
        signUpStage.setY(currentStage.getY());

        // Optionally, resize the scene to fit the content
        signUpStage.sizeToScene();
        return signUpStage;
    }


    private static Stage getStage(Stage currentStage, Parent root) {
        double currentWidth = currentStage.getWidth();
        double currentHeight = currentStage.getHeight();
        double currentX = currentStage.getX();
        double currentY = currentStage.getY();

        Scene scene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setTitle("Main Menu");

        newStage.setWidth(currentWidth);
        newStage.setHeight(currentHeight);
        newStage.setX(currentX);
        newStage.setY(currentY);

        return newStage;
    }

}
