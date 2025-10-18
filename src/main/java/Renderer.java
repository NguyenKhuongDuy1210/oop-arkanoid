package Managers;

import items.Ball;
import items.Brick;
import items.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

public class Renderer {

    private Image backgroundImg, ballImg, paddleImg, brickImg;
    private Image[] brickImgs = new Image[4];

    public Renderer() {
        backgroundImg = new Image("file:assets/background.png");
        ballImg       = new Image("file:assets/ball.png");
        paddleImg     = new Image("file:assets/paddle.png");
        brickImg      = new Image("file:assets/normal brick2.png");
        brickImgs[0] = new Image("file:assets/brick1.png");
        brickImgs[1] = new Image("file:assets/brick2.png");
        brickImgs[2] = new Image("file:assets/brick3.png");
        brickImgs[3] = new Image("file:assets/brick4.png");

    }

    public void render(GraphicsContext gc, GameManager gameManager) {
        gc.clearRect(0, 0, 800, 600);

        // Vẽ background
        gc.drawImage(backgroundImg, 0, 0, 800, 600);

        // Vẽ gạch
        for(Brick brick : gameManager.getBricks()) {
            brick.update();    // cập nhật frame nếu brick đang bị hit
            int idx = brick.getFrameIndex();
            gc.drawImage(brickImgs[idx], brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight());
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
