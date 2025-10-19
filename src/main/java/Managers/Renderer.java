package Managers;

import items.Ball;
import items.Brick;
import items.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

public class Renderer {

    private Image backgroundImg, ballImg, paddleImg, brickImg;

    public Renderer() {
        backgroundImg = new Image("file:assets/background.png");
        ballImg       = new Image("file:assets/ball.png");
        paddleImg     = new Image("file:assets/paddle.png");
        brickImg      = new Image("file:assets/brick.png");
    }

    public void render(GraphicsContext gc, GameManager gameManager) {
        gc.clearRect(0, 0, 600, 750);

        // Vẽ background
        gc.drawImage(backgroundImg, 0, 0, 600, 750);

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

        // Game Over
        if (gameManager.isGameOver()) {
            gc.fillText("GAME OVER!", 350, 300);
        }
    }
}
