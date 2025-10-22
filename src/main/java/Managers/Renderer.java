package Managers;

import items.Ball;
import items.Brick;
import items.Paddle;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.awt.*;
import java.util.List;

public class Renderer {

    private Image backgroundImg, ballImg, paddleImg, brickImg;
    private Menu menu;

    public Renderer() {
        backgroundImg = new Image("file:assets/background.png");
        ballImg       = new Image("file:assets/ball.png");
        paddleImg     = new Image("file:assets/paddle.png");
        brickImg      = new Image("file:assets/brick.png");
        menu          = new Menu();
    }

    public void render(GraphicsContext gc, GameManager gameManager) {
        gc.clearRect(0, 0, 600, 750);

        // Vẽ background
        gc.drawImage(backgroundImg, 0, 0, 600, 750);

        switch(gameManager.getCurrent_GameState()) {
            case Menu:
                menu.render(gc);
                break;
            case Playing:
                renderGame(gc, gameManager);
                break;
            case GameOver:
                renderGame(gc, gameManager);
                renderGameOver(gc, gameManager);
                break;
        }
    }

    private void renderGame(GraphicsContext gc, GameManager gameManager) {
        // Vẽ gạch
        for (Brick brick : gameManager.getBricks()) {
            brick.update();

            if (brick.isOnHit()) {
                int[] clip = brick.getCurrentClip();
                gc.drawImage(
                        brickImg,
                        clip[0], clip[1], clip[2], clip[3], // source rectangle
                        brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight() // destination
                );
            } else {
                gc.drawImage(
                        brickImg,
                        0, 0, brick.getWidth(), brick.getHeight(), // mặc định frame đầu
                        brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight()
                );
            }
        }

        // Vẽ paddle
        Paddle paddle = gameManager.getPaddle();
        gc.drawImage(paddleImg, paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());

        // Vẽ ball
        Ball ball = gameManager.getBall();
        gc.drawImage(ballImg, ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight());

        // Vẽ UI
        gc.fillText("Score: " + gameManager.getScore(), 20, 20);
        gc.fillText("Lives: " + gameManager.getLives(), 700, 20);
    }

    private void renderGameOver(GraphicsContext gc, GameManager gameManager) {

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        // Kiểm tra xem người chơi thắng hay thua
        if (gameManager.getPlayerWin()) {
            gc.setFill(Color.LIMEGREEN);
            gc.setFont(new Font("Arial", 70));
            gc.fillText("YOU WIN!", 300, 300);
        } else {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Arial", 70));
            gc.fillText("GAME OVER", 300, 300);
        }

        // Hiển thị điểm số cuối cùng
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 30));
        gc.fillText("Final Score: " + gameManager.getScore(), 300, 360);

        // Hướng dẫn người chơi
        gc.setFont(new Font("Arial", 20));
        gc.fillText("Press Enter to Return to Menu", 300, 420);
    }

    public Menu getMenu() {
        return menu;
    }
}
