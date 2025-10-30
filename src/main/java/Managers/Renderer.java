package Managers;

import Managers.GameConfig.GameConfig;
import Managers.MenuManager.Menu;
import items.Ball;
import items.Brick;
import items.Paddle;
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

    // --- BALL TRAIL EFFECT ---
    private static class Trail {
        double cx, cy;    // tâm
        double size;
        double life = 1.0;
        Trail(double cx, double cy, double size) {
            this.cx = cx; this.cy = cy; this.size = size;
        }
    }

    private final List<Trail> trails = new ArrayList<>();
    private final int MAX_TRAIL = 10;
    private final double LIFE_DECREASE = 0.05;
    private final double SIZE_SHRINK = 0.92;

    public Renderer() {
        backgroundPlaying = new Image("file:assets/background.png");
        backgroundMenu= new Image("file:assets/background1.png");
        ballImg       = new Image("file:assets/ball.png");
        paddleImg     = new Image("file:assets/paddle.png");
        menu          = new Menu();
    }

    public void render(GraphicsContext gc, GameManager gameManager) {
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
            else {
                gc.drawImage(brick.getBrickImg(), 0, 0, brick.getWidth(), brick.getHeight(),
                        brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight());
            }
        }

        Paddle paddle = gameManager.getPaddle();
        gc.drawImage(paddleImg, paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());

        Ball ball = gameManager.getBall();

        // ------ TRAIL ADD ------
        if (trails.size() >= MAX_TRAIL) trails.remove(0);
        double cx = ball.getX() + ball.getWidth() / 2;
        double cy = ball.getY() + ball.getHeight() / 2;
        trails.add(new Trail(cx, cy, ball.getWidth()));

        // ------ DRAW TRAIL BEFORE MAIN BALL ------
        Iterator<Trail> it = trails.iterator();
        while (it.hasNext()) {
            Trail t = it.next();
            t.life -= LIFE_DECREASE;
            t.size *= SIZE_SHRINK;
            if (t.life <= 0 || t.size <= 0) { it.remove(); continue; }

            gc.setGlobalAlpha(t.life * 0.6);
            double drawX = t.cx - t.size / 2;
            double drawY = t.cy - t.size / 2;
            gc.drawImage(ballImg, drawX, drawY, t.size, t.size);
        }
        gc.setGlobalAlpha(1.0);

        gc.drawImage(ballImg, ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight());

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
