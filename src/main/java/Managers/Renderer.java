package Managers;

import Managers.GameConfig.GameConfig;
import Managers.MenuManager.Menu;
import items.Ball;
import items.Brick;
import items.Paddle;
import items.PowerUp;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class Renderer {

    private Image backgroundMenu, backgroundPlaying, ballImg, paddleImg, backgroundScreen;
    private Menu menu;

    public Renderer() {

        backgroundPlaying = new Image("file:assets/background/Background_Play.png");
        backgroundScreen = new Image("file:assets/background/background_screen2.png");
        backgroundMenu= new Image("file:assets/background/Background_Menu.png");
        ballImg       = new Image("file:assets/ball/normal_ball.png");
        paddleImg     = new Image("file:assets/paddle/paddle.png");
        menu          = new Menu();
    }

    public void render(GraphicsContext gc, GameManager gameManager) throws Exception {
        gc.clearRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT); // Xóa khung vẽ trước khi vẽ lại
        gc.drawImage(backgroundScreen, 0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        switch (gameManager.getCurrentGameState()) {
            case Menu, Setting -> {
                gc.drawImage(backgroundMenu, GameConfig.SCREEN_X, GameConfig.SCREEN_Y, GameConfig.SCREEN_PLAY_WIDTH, GameConfig.SCREEN_PLAY_HEIGHT); // Vẽ nền menu
                menu.render(gc); // Vẽ nội dung hiện trên menu
            }
            case Playing -> renderGame(gc, gameManager);
            case GameOver -> {
                renderGame(gc, gameManager);
                renderGameOver(gc, gameManager);
            }
            case Option -> {
                renderGame(gc, gameManager); // Vẽ game bên dưới (sử dụng nền Playing)
                renderOptionMenu(gc); // Vẽ menu Option/Pause lên trên
            }
        }
    }

    private void renderGame(GraphicsContext gc, GameManager gameManager) {
        gc.drawImage(backgroundPlaying, GameConfig.SCREEN_X, GameConfig.SCREEN_Y, GameConfig.SCREEN_PLAY_WIDTH, GameConfig.SCREEN_PLAY_HEIGHT);
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
        gc.fillText("Score: " + gameManager.getScore(),GameConfig.SCREEN_X + 50, 20);
        gc.fillText("Lives: " + gameManager.getLives(),GameConfig.SCREEN_X + 550, 20);
    }

    private void renderGameOver(GraphicsContext gc, GameManager gameManager) {
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        if (gameManager.getPlayerWin()) {
            gc.setFill(Color.LIMEGREEN);
            gc.setFont(new Font("Arial", 70));
            gc.fillText("YOU WIN!", GameConfig.SCREEN_X + 300, 300);
        } else {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Arial", 70));
            gc.fillText("GAME OVER", GameConfig.SCREEN_X + 300, 300);
        }

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 30));
        gc.fillText("Final Score: " + gameManager.getScore(), GameConfig.SCREEN_X + 300, 360);

        gc.setFont(new Font("Arial", 20));
        gc.fillText("Press Enter to Return to Menu", GameConfig.SCREEN_X + 300, 420);
    }

    private void renderOptionMenu(GraphicsContext gc) {
        gc.setFill(Color.rgb(0, 0, 0, 0.7)); // Màu nền mờ 70%
        gc.fillRect(GameConfig.SCREEN_X , GameConfig.SCREEN_Y, GameConfig.SCREEN_PLAY_WIDTH, GameConfig.SCREEN_PLAY_HEIGHT); // Vẽ nền mờ

        menu.render(gc); // Vẽ nội dung menu Option/Pause
    }

    public Menu getMenu() {
        return menu;
    }
}
