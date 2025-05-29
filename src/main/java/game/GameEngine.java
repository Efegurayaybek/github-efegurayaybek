package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {
    private int canvasWidth;
    private int canvasHeight;

    // Kuş özellikleri (Kolay mod)
    private double birdX = 50;
    private double birdY = 300;
    private double birdWidth = 32;
    private double birdHeight = 28;
    private double birdVelocity = 0;
    private double gravity = 0.35;      // Düşük yerçekimi
    private double jumpPower = -7.5;    // Yumuşak zıplama
    private double maxVelocity = 8;     // Maksimum düşüş hızı

    // Boru özellikleri
    private List<Pipe> pipes;
    private double pipeGap = 180;       // Geniş geçit
    private double pipeWidth = 55;      // İnce boru
    private double pipeSpacing = 280;   // Borular arası mesafe

    // Oyun durumu
    private int score = 0;
    private double gameSpeed = 1.5;     // Yavaş başlangıç
    private boolean gameOver = false;

    public GameEngine(int width, int height) {
        this.canvasWidth = width;
        this.canvasHeight = height;
        this.pipes = new ArrayList<>();
    }

    public void initGame() {
        birdX = 50;
        birdY = 300;
        birdVelocity = 0;
        pipes.clear();
        score = 0;
        gameSpeed = 1.5;
        gameOver = false;

        // İlk boru
        createPipe();
    }

    public void update() {
        if (gameOver) return;

        updateBird();
        updatePipes();
    }

    private void updateBird() {
        birdVelocity += gravity;

        // Maksimum düşüş hızını sınırla
        if (birdVelocity > maxVelocity) {
            birdVelocity = maxVelocity;
        }

        birdY += birdVelocity;

        // Zemin ve tavan kontrolü - daha toleranslı
        if (birdY + birdHeight > canvasHeight - 5 || birdY < 5) {
            gameOver = true;
        }
    }

    private void updatePipes() {
        for (int i = pipes.size() - 1; i >= 0; i--) {
            Pipe pipe = pipes.get(i);
            pipe.x -= gameSpeed;

            // Skor kontrolü
            if (!pipe.passed && pipe.x + pipeWidth < birdX) {
                pipe.passed = true;
                score++;

                // Hız artışı - yavaş artış
                if (score % 8 == 0 && gameSpeed < 3) {
                    gameSpeed += 0.15;
                }
            }

            // Çarpışma kontrolü - daha toleranslı hitbox
            double birdLeft = birdX + 3;
            double birdRight = birdX + birdWidth - 3;
            double birdTop = birdY + 3;
            double birdBottom = birdY + birdHeight - 3;

            if (birdRight > pipe.x &&
                    birdLeft < pipe.x + pipeWidth &&
                    (birdTop < pipe.topHeight ||
                            birdBottom > pipe.bottomY)) {
                gameOver = true;
            }

            // Ekrandan çıkan boruları kaldır
            if (pipe.x + pipeWidth < 0) {
                pipes.remove(i);
            }
        }

        // Yeni boru oluştur
        if (pipes.isEmpty() || pipes.get(pipes.size() - 1).x < canvasWidth - pipeSpacing) {
            createPipe();
        }
    }

    private void createPipe() {
        double minHeight = 80;    // Yüksek minimum
        double maxHeight = canvasHeight - pipeGap - minHeight - 80;
        double topHeight = Math.random() * (maxHeight - minHeight) + minHeight;

        Pipe pipe = new Pipe();
        pipe.x = canvasWidth;
        pipe.topHeight = topHeight;
        pipe.bottomY = topHeight + pipeGap;
        pipe.bottomHeight = canvasHeight - (topHeight + pipeGap);
        pipe.passed = false;

        pipes.add(pipe);
    }

    public void jump() {
        if (!gameOver) {
            birdVelocity = jumpPower;
        }
    }

    public void render(GraphicsContext gc) {
        drawBackground(gc);
        drawPipes(gc);
        drawBird(gc);
    }

    private void drawBackground(GraphicsContext gc) {
        // Gökyüzü gradyanı
        LinearGradient skyGradient = new LinearGradient(
                0, 0, 0, canvasHeight, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.SKYBLUE),
                new Stop(0.7, Color.LIGHTGREEN),
                new Stop(1, Color.LIGHTGREEN)
        );
        gc.setFill(skyGradient);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        // Bulutlar
        gc.setFill(Color.rgb(255, 255, 255, 0.8));
        drawCloud(gc, 60, 70, 28);
        drawCloud(gc, 220, 100, 22);
        drawCloud(gc, 320, 80, 25);
        drawCloud(gc, 150, 140, 20);

        // Zemin
        gc.setFill(Color.SADDLEBROWN);
        gc.fillRect(0, canvasHeight - 15, canvasWidth, 15);

        // Çim efekti
        gc.setFill(Color.FORESTGREEN);
        for (int i = 0; i < canvasWidth; i += 20) {
            gc.fillRect(i, canvasHeight - 20, 15, 8);
        }
    }

    private void drawCloud(GraphicsContext gc, double x, double y, double size) {
        gc.fillOval(x, y, size, size);
        gc.fillOval(x + size * 0.8, y, size * 1.2, size * 1.2);
        gc.fillOval(x + size * 1.8, y, size * 0.9, size * 0.9);
    }

    private void drawBird(GraphicsContext gc) {
        // Kuş gövdesi
        gc.setFill(Color.GOLD);
        gc.fillOval(birdX, birdY, birdWidth, birdHeight);

        // Kuş gözü
        gc.setFill(Color.BLACK);
        gc.fillOval(birdX + 22, birdY + 9, 8, 8);

        // Göz parlaması
        gc.setFill(Color.WHITE);
        gc.fillOval(birdX + 24, birdY + 7, 4, 4);

        // Kuş gagası
        gc.setFill(Color.DARKORANGE);
        double[] beakX = {birdX + birdWidth, birdX + birdWidth + 10, birdX + birdWidth};
        double[] beakY = {birdY + 12, birdY + 14, birdY + 17};
        gc.fillPolygon(beakX, beakY, 3);

        // Kanat efekti
        gc.setFill(Color.ORANGE);
        gc.fillOval(birdX + 6, birdY + 16, 18, 10);
    }

    private void drawPipes(GraphicsContext gc) {
        for (Pipe pipe : pipes) {
            // Üst boru
            gc.setFill(Color.FORESTGREEN);
            gc.fillRect(pipe.x, 0, pipeWidth, pipe.topHeight);

            // Alt boru
            gc.fillRect(pipe.x, pipe.bottomY, pipeWidth, pipe.bottomHeight);

            // Boru kapakları
            gc.setFill(Color.DARKGREEN);
            gc.fillRect(pipe.x - 8, pipe.topHeight - 30, pipeWidth + 16, 30);
            gc.fillRect(pipe.x - 8, pipe.bottomY, pipeWidth + 16, 30);

            // Boru yansıma efekti
            gc.setFill(Color.rgb(255, 255, 255, 0.2));
            gc.fillRect(pipe.x + 5, 0, 8, pipe.topHeight);
            gc.fillRect(pipe.x + 5, pipe.bottomY, 8, pipe.bottomHeight);
        }
    }

    // Getter metodları
    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    // İç sınıf - Boru
    private static class Pipe {
        double x;
        double topHeight;
        double bottomY;
        double bottomHeight;
        boolean passed;
    }
}