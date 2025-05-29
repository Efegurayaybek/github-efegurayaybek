package controller;

import model.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Label messageLabel;

    private UserManager userManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userManager = UserManager.getInstance();

        // Clear message label initially
        messageLabel.setText("");

        // Add Enter key support for login
        usernameField.setOnKeyPressed(this::handleKeyPressed);
        passwordField.setOnKeyPressed(this::handleKeyPressed);

        // Focus on username field when screen loads
        usernameField.requestFocus();
    }

    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin(null);
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please enter both username and password!", "error");
            return;
        }

        if (userManager.login(username, password)) {
            showMessage("Login successful!", "success");

            try {
                // Load game screen
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
                Parent root = loader.load();

                // Get game controller and pass user information
                GameController gameController = loader.getController();
                gameController.setUserManager(userManager);
                gameController.setCurrentUser(username);

                // Switch to game screen
                Stage stage = (Stage) loginButton.getScene().getWindow();
                Scene scene = new Scene(root, 600, 700);

                // Add CSS if available
                try {
                    scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
                } catch (Exception e) {
                    System.out.println("CSS file not found, using default style");
                }

                stage.setScene(scene);
                stage.setTitle("FBG Game - " + username);

            } catch (IOException e) {
                e.printStackTrace();
                showMessage("Error loading game screen!", "error");
            }
        } else {
            showMessage("Invalid username or password!", "error");
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/register.fxml"));
            Stage stage = (Stage) registerButton.getScene().getWindow();
            Scene scene = new Scene(root, 600, 700);

            // Add CSS if available
            try {
                scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            } catch (Exception e) {
                System.out.println("CSS file not found, using default style");
            }

            stage.setScene(scene);
            stage.setTitle("FBG Game - Register");
        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Error loading register screen!", "error");
        }
    }

    private void showMessage(String message, String type) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().clear();
        messageLabel.getStyleClass().add("message-" + type);
    }
}