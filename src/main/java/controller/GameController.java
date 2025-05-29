package controller;

import game.GameEngine;
import model.User;
import model.UserManager;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    private Canvas gameCanvas;
    @FXML
    private Label playerLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label bestScoreLabel;
    @FXML
    private VBox gameOverPanel;
    @FXML
    private Label finalScoreLabel;
    @FXML
    private Label newRecordLabel;
    @FXML
    private VBox leaderboardBox;
    @FXML
    private Button playAgainButton;
    @FXML
    private Button exitButton;

    private UserManager userManager;
    private String currentUser;
    private GameEngine gameEngine;
    private AnimationTimer gameTimer;
    private GraphicsContext gc;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = gameCanvas.getGraphicsContext2D();
        gameEngine = new GameEngine(600, 600);

        // Make canvas focusable
        gameCanvas.setFocusTraversable(true);

        // DEBUG: Button control
        System.out.println("🔍 Initialize - Button controls:");
        System.out.println("playAgainButton is null? " + (playAgainButton == null));
        System.out.println("exitButton is null? " + (exitButton == null));
        System.out.println("gameOverPanel is null? " + (gameOverPanel == null));

        // Manual event handler setup (backup)
        setupButtonHandlers();

        // Start game
        startGame();
    }

    // Manual event handler setup
    private void setupButtonHandlers() {
        if (playAgainButton != null) {
            playAgainButton.setOnAction(this::handlePlayAgain);
            System.out.println("✅ Play Again button event handler set up");
        } else {
            System.out.println("❌ Play Again button is NULL!");
        }

        if (exitButton != null) {
            exitButton.setOnAction(this::handleExit);
            System.out.println("✅ Exit button event handler set up");
        } else {
            System.out.println("❌ Exit button is NULL!");
        }
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setCurrentUser(String username) {
        this.currentUser = username;
        playerLabel.setText(username);
        bestScoreLabel.setText(String.valueOf(userManager.getUserBestScore(username)));
    }

    private void startGame() {
        gameEngine.initGame();
        gameOverPanel.setVisible(false);

        // Game loop
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameEngine.update();
                gameEngine.render(gc);

                // Score update
                scoreLabel.setText(String.valueOf(gameEngine.getScore()));

                // Game over check
                if (gameEngine.isGameOver()) {
                    stop();
                    // Run on UI thread with Platform.runLater
                    Platform.runLater(() -> showGameOver());
                }
            }
        };
        gameTimer.start();

        // Controls
        gameCanvas.setOnKeyPressed(this::handleKeyPress);
        gameCanvas.setOnMouseClicked(event -> {
            gameEngine.jump();
            // Give focus to canvas
            gameCanvas.requestFocus();
        });
        gameCanvas.requestFocus();
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.SPACE) {
            gameEngine.jump();
        }
    }

    private void showGameOver() {
        int finalScore = gameEngine.getScore();
        finalScoreLabel.setText(String.valueOf(finalScore));

        // Record check
        boolean isNewRecord = userManager.updateUserScore(currentUser, finalScore);
        newRecordLabel.setVisible(isNewRecord && finalScore > 0);

        // Update best score
        bestScoreLabel.setText(String.valueOf(userManager.getUserBestScore(currentUser)));

        // Update leaderboard
        updateLeaderboard();

        // Show game over panel
        gameOverPanel.setVisible(true);
        gameOverPanel.setDisable(false);

        // DEBUG: Button status check
        System.out.println("🎮 Game Over Panel shown!");
        System.out.println("🔍 Button states:");
        if (playAgainButton != null) {
            System.out.println("playAgainButton - visible: " + playAgainButton.isVisible() + ", disabled: " + playAgainButton.isDisabled());
        }
        if (exitButton != null) {
            System.out.println("exitButton - visible: " + exitButton.isVisible() + ", disabled: " + exitButton.isDisabled());
        }
        System.out.println("gameOverPanel - visible: " + gameOverPanel.isVisible());

        // Force enable buttons
        enableGameOverButtons();

        // Give focus to game over panel
        Platform.runLater(() -> {
            gameOverPanel.requestFocus();
            if (playAgainButton != null) {
                playAgainButton.requestFocus();
            }
        });

        System.out.println("✅ Game Over setup completed");
    }

    // Enable game over buttons
    private void enableGameOverButtons() {
        if (playAgainButton != null) {
            playAgainButton.setDisable(false);
            playAgainButton.setVisible(true);
            playAgainButton.setManaged(true);
            System.out.println("🔄 Play Again button enabled");
        }

        if (exitButton != null) {
            exitButton.setDisable(false);
            exitButton.setVisible(true);
            exitButton.setManaged(true);
            System.out.println("🚪 Exit button enabled");
        }
    }

    private void updateLeaderboard() {
        leaderboardBox.getChildren().clear();

        List<User> leaderboard = userManager.getLeaderboard();

        if (leaderboard.isEmpty()) {
            Label noScores = new Label("No scores yet");
            noScores.getStyleClass().add("leaderboard-item");
            leaderboardBox.getChildren().add(noScores);
        } else {
            for (int i = 0; i < leaderboard.size(); i++) {
                User user = leaderboard.get(i);
                String medal = i == 0 ? "🥇" : i == 1 ? "🥈" : i == 2 ? "🥉" : (i + 1) + ".";

                Label userItem = new Label(medal + " " + user.getUsername() + " - " + user.getBestScore());
                userItem.getStyleClass().add("leaderboard-item");

                // Highlight current user
                if (user.getUsername().equals(currentUser)) {
                    userItem.getStyleClass().add("current-user");
                }

                leaderboardBox.getChildren().add(userItem);
            }
        }
    }

    @FXML
    private void handlePlayAgain(ActionEvent event) {
        System.out.println("🔄 PLAY AGAIN button pressed!"); // Debug message

        try {
            if (gameTimer != null) {
                gameTimer.stop();
            }

            // Hide game over panel
            gameOverPanel.setVisible(false);

            // Start new game
            startGame();

            System.out.println("✅ New game started!");

        } catch (Exception e) {
            System.err.println("❌ Play again error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.out.println("🚪 EXIT button pressed!"); // Debug message

        try {
            if (gameTimer != null) {
                gameTimer.stop();
            }

            userManager.logout();

            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) exitButton.getScene().getWindow();
            Scene scene = new Scene(root, 600, 700);

            try {
                scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            } catch (Exception e) {
                System.out.println("CSS file not found, using default style");
            }

            stage.setScene(scene);
            System.out.println("✅ Successfully returned to login screen!");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Login screen return error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Unexpected exit error: " + e.getMessage());
        }
    }
}