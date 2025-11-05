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
import Managers.MenuManager.MenuItem;
import javafx.scene.text.Text;

public class Renderer {

    private Image backgroundMenu, backgroundPlaying, ballImg, paddleImg, backgroundScreen, fireBallImg;
    private Menu menu;

    public Renderer() {

        backgroundPlaying = new Image("file:assets/background/Background_Play.png"); // Nền khi chơi game
        backgroundScreen = new Image("file:assets/background/background_screen.png"); // Nền chung cho tất cả các trạng thái
        backgroundMenu = new Image("file:assets/background/Background_Menu.png"); // Nền menu
        ballImg = new Image("file:assets/ball/ball_normal.png"); // Hình ảnh quả bóng
        fireBallImg = new Image("file:assets/ball/ball_fire.png"); // hình ảnh quả bóng lửa khi ăn power up
        paddleImg = new Image("file:assets/paddle/paddle.png"); // Hình ảnh thanh trượt
        menu = new Menu(); // Khởi tạo menu
    }

    public void render(GraphicsContext gc, GameManager gameManager) throws Exception { // Phương thức vẽ tất cả các trạng thái của game

        gc.clearRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT); // Xóa khung vẽ trước khi vẽ lại
        gc.drawImage(backgroundScreen, 0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT); // Vẽ nền chung
        
        switch (gameManager.getCurrentGameState()) {
            case Menu, Setting -> {
                gc.drawImage(backgroundMenu, GameConfig.SCREEN_X, GameConfig.SCREEN_Y, 
                GameConfig.SCREEN_PLAY_WIDTH, GameConfig.SCREEN_PLAY_HEIGHT); // Vẽ nền menu

                renderMenu(gc, menu); // Vẽ menu chính hoặc menu cài đặt
            }
            case Option -> {
                renderGame(gc, gameManager); // Vẽ game phía sau
                renderOptionOverlay(gc); // Vẽ nền mờ
                renderMenu(gc, menu); // Vẽ menu Option/Pause phía trên
            }
            case Playing -> {
                renderGame(gc, gameManager); // Vẽ game khi đang chơi
            }
            case GameOver -> {
                renderGame(gc, gameManager); // Vẽ game phía sau
                renderOptionOverlay(gc); // Vẽ nền mờ
                renderGameOver(gc, gameManager); // Vẽ màn hình Game Over
            }
        }
    }

    private void renderMenu(GraphicsContext gc, Menu menu) {
        String title = "ARKANOID";

        if (menu.getMenuItems() == menu.getPauseMenu()) { // Kiểm tra nếu là menu Pause
            title = "PAUSED";
        } else if (menu.getMenuItems() == menu.getSettingsMenu()) { // Kiểm tra nếu là menu Setting
            title = "OPTIONS";
        }

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(menu.getTitleFont());
        gc.setFill(Color.WHITE);
        gc.fillText(title, menu.getTitleX(), menu.getTitleY());

        gc.setFont(menu.getItemFont());
        for (MenuItem item : menu.getMenuItems()) { // Vẽ từng mục trong menu
            gc.setFill(Color.WHITE); // Màu chữ trắng
            gc.fillText(item.getText(), item.getX(), item.getY()); // Vẽ chữ của mục
        }

        // Vẽ selector
        if (!menu.getMenuItems().isEmpty() && menu.getSelectorIcon() != null) {
            MenuItem selectedItem = menu.getMenuItems().get(menu.getSelectedItemIndex()); // Lấy mục được chọn
            Image selectorIcon = menu.getSelectorIcon(); // Lấy hình ảnh selector
            double selectorY = menu.getSelectorCurrentY(); // Vị trí Y hiện tại của selector

            Text textNode = new Text(selectedItem.getText()); // Tạo Text để đo kích thước
            textNode.setFont(menu.getItemFont()); // Đặt font giống với font của menu
            double textWidth = textNode.getLayoutBounds().getWidth(); // Lấy chiều rộng của text

            double leftIconX = selectedItem.getX() - (textWidth / 2) - 25 - selectorIcon.getWidth(); // Vị trí X của icon bên trái
            double rightIconX = selectedItem.getX() + (textWidth / 2) + 25; // Vị trí X của icon bên phải
            double iconY = selectorY + (menu.getItemFont().getSize() / 2) - (selectorIcon.getHeight() / 2); // Vị trí Y của icon

            gc.drawImage(selectorIcon, leftIconX, iconY); // Vẽ icon bên trái

            // Vẽ icon bên phải với lật ngang
            gc.save();
            gc.translate(rightIconX + selectorIcon.getWidth(), iconY);
            gc.scale(-1, 1);
            gc.drawImage(selectorIcon, 0, 0);
            gc.restore();
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
            if (b.isFireBall()) {
                gc.drawImage(fireBallImg, b.getX(), b.getY(), b.getWidth(), b.getHeight());
            } else {
                gc.drawImage(ballImg, b.getX(), b.getY(), b.getWidth(), b.getHeight());
            }
        }

        for (PowerUp p : gameManager.getPowerUps()) {
            p.render(gc);
        }

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + gameManager.getScore(), GameConfig.SCREEN_X + 50, 20);
        gc.fillText("Lives: " + gameManager.getLives(), GameConfig.SCREEN_X + 550, 20);
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
        gc.fillRect(GameConfig.SCREEN_X, GameConfig.SCREEN_Y, GameConfig.SCREEN_PLAY_WIDTH, GameConfig.SCREEN_PLAY_HEIGHT); // Vẽ nền mờ

        renderMenu(gc, menu); // Vẽ nội dung menu Option/Pause
    }

    private void renderOptionOverlay(GraphicsContext gc) { // Vẽ nền mờ cho menu Option/Pause
        gc.setFill(Color.rgb(0, 0, 0, 0.7)); // nền mờ 70%
        gc.fillRect(GameConfig.SCREEN_X, GameConfig.SCREEN_Y, 
        GameConfig.SCREEN_PLAY_WIDTH, GameConfig.SCREEN_PLAY_HEIGHT); // Vẽ nền mờ
    }

    public Menu getMenu() {
        return menu;
    }
}
