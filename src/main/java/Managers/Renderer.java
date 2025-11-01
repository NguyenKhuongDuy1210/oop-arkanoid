package Managers;

import Managers.GameConfig.GameConfig;
import Managers.MapManager.MapGame;
import Managers.MenuManager.Menu;
import items.Ball;
import items.Brick;
import items.Paddle;
import items.PowerUp;
import items.factory.MovingBrick;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;  

public class Renderer {

    private Image backgroundMenu, backgroundPlaying, ballImg, paddleImg;
    private Menu menu;

    public Renderer() {
        backgroundPlaying = new Image("file:assets/background/background.png");
        backgroundMenu= new Image("file:assets/background/background1.png");
        ballImg       = new Image("file:assets/ball/normal_ball.png");
        paddleImg     = new Image("file:assets/paddle/paddle.png");
        menu          = new Menu();
    }

    public void render(GraphicsContext gc, GameManager gameManager) throws Exception {
        gc.clearRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

        switch (gameManager.getCurrentGameState()) {
            case Menu -> menu.render(gc, backgroundMenu);
            case Playing -> renderGame(gc, gameManager);
            case GameOver -> {
                renderGame(gc, gameManager);
                renderGameOver(gc, gameManager);
            }
        }
    }

    private void renderGame(GraphicsContext gc, GameManager gameManager) {

        gc.drawImage(backgroundPlaying, 0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        for (Brick brick : gameManager.getBricks()) {
            brick.update();
            if (brick.gethitPoints() >= 0) {
                int[] clip = brick.getCurrentClip();
                gc.drawImage(brick.getBrickImg(), clip[0], clip[1], clip[2], clip[3],
                        brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight());
            }
        }

        Paddle paddle = gameManager.getPaddle();
        gc.drawImage(paddleImg, paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());
        for (Ball b : gameManager.getBalls()) {
            gc.drawImage(ballImg, b.getX(), b.getY(), b.getWidth(), b.getHeight());
        }

        for (PowerUp p : gameManager.getPowerUps()) {
            p.render(gc);
        }

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + gameManager.getScore(), 50, 20);
        gc.fillText("Lives: " + gameManager.getLives(), 550, 20);
    }

    private void renderGameOver(GraphicsContext gc, GameManager gameManager) {
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        if (gameManager.getPlayerWin()) {
            gc.setFill(Color.LIMEGREEN);
            gc.setFont(new Font("Arial", 70));
            gc.fillText("YOU WIN!", 300, 300);
        } else {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Arial", 70));
            gc.fillText("GAME OVER", 300, 300);
        }

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 30));
        gc.fillText("Final Score: " + gameManager.getScore(), 300, 360);

        gc.setFont(new Font("Arial", 20));
        gc.fillText("Press Enter to Return to Menu", 300, 420);
    }

    public Menu getMenu() {
        return menu;
    }
}
