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

public class RegisterController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button registerButton;
    @FXML private Button backButton;
    @FXML private Label messageLabel;

    private UserManager userManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userManager = UserManager.getInstance();

        // Clear message label initially
        messageLabel.setText("");

        // Add Enter key support for registration
        usernameField.setOnKeyPressed(this::handleKeyPressed);
        passwordField.setOnKeyPressed(this::handleKeyPressed);
        confirmPasswordField.setOnKeyPressed(this::handleKeyPressed);

        // Focus on username field when screen loads
        usernameField.requestFocus();
    }

    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleRegister(null);
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validation
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showMessage("Please fill in all fields!", "error");
            return;
        }

        if (username.length() < 3) {
            showMessage("Username must be at least 3 characters long!", "error");
            return;
        }

        if (password.length() < 3) {
            showMessage("Password must be at least 3 characters long!", "error");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showMessage("Passwords do not match!", "error");
            return;
        }

        // Check for special characters in username
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            showMessage("Username can only contain letters, numbers, and underscores!", "error");
            return;
        }

        // Try to register
        String registerResult = userManager.register(username, password, confirmPassword);
        if (registerResult == null) {
            // Registration successful (null means success)
            showMessage("Registration successful! Redirecting to login...", "success");

            // Wait a moment and then redirect to login
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(this::goToLogin);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } else {
            // Registration failed, show error message
            showMessage(translateErrorMessage(registerResult), "error");
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        goToLogin();
    }

    private void goToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root, 600, 700);

            // Add CSS if available
            try {
                scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            } catch (Exception e) {
                System.out.println("CSS file not found, using default style");
            }

            stage.setScene(scene);
            stage.setTitle("FBG Game - Login");
        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Error loading login screen!", "error");
        }
    }

    private void showMessage(String message, String type) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().clear();
        messageLabel.getStyleClass().add("message-" + type);
    }

    // Translate Turkish error messages to English
    private String translateErrorMessage(String turkishMessage) {
        switch (turkishMessage) {
            case "Kullanıcı adı en az 3 karakter olmalı!":
                return "Username must be at least 3 characters long!";
            case "Şifre en az 4 karakter olmalı!":
                return "Password must be at least 4 characters long!";
            case "Şifreler eşleşmiyor!":
                return "Passwords do not match!";
            case "Bu kullanıcı adı zaten mevcut!":
                return "This username already exists!";
            case "Tüm alanları doldurun!":
                return "Please fill in all fields!";
            default:
                return turkishMessage; // Return original if no translation found
        }
    }
}