package com.example.oop_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Parent;

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

        if ("mehedi".equals(username) && "1234".equals(password)) {
            openMainMenuWindow();
        } else {
            errorLabel.setText("Invalid username or password!");
        }
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

    private static Stage getStage(Stage currentStage, Parent root) {
        double currentWidth = currentStage.getWidth();
        double currentHeight = currentStage.getHeight();

        Scene scene = new Scene(root);

        Stage mainMenuStage = new Stage();
        mainMenuStage.setScene(scene);
        mainMenuStage.setTitle("Bank Main Menu");

        // Apply the width, height, and position of the current stage to the new stage
        mainMenuStage.setWidth(currentWidth);    // Set the width of the new stage
        mainMenuStage.setHeight(currentHeight);  // Set the height of the new stage
        mainMenuStage.setX(currentStage.getX()); // Set the X position of the new stage
        mainMenuStage.setY(currentStage.getY()); // Set the Y position of the new stage
        return mainMenuStage;
    }
}
