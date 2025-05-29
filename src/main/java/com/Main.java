package com.fbg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));

            Scene scene = new Scene(root, 600, 700);
            // CSS dosyası varsa ekle
            try {
                scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            } catch (Exception e) {
                System.out.println("CSS dosyası bulunamadı, varsayılan stiller kullanılacak");
            }

            primaryStage.setTitle("🐦 FBG - Flappy Bird Game 🐦");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            System.out.println("✅ Oyun başarıyla başlatıldı!");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("FXML dosyası yüklenemedi: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("🚀 FBG başlatılıyor...");
        launch(args);
    }
}