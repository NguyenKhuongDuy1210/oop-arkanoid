package Managers;

import Managers.GameConfig.GameConfig;
import Managers.MenuManager.GameState;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Renderer {

    private Image backgroundMenu, backgroundPlaying, ballImg, paddleImg, backgroundScreen, fireBallImg;
    private Menu menu;

    public Renderer() throws FileNotFoundException {

        backgroundPlaying = new Image("file:assets/background/Background_Play.png"); // Nền khi chơi game
        backgroundScreen = new Image("file:assets/background/Background_Screen.png"); // Nền chung cho tất cả các trạng thái
        backgroundMenu = new Image("file:assets/background/Background_Menu.png"); // Nền menu
        ballImg = new Image("file:assets/ball/ball_normal.png"); // Hình ảnh quả bóng
        fireBallImg = new Image("file:assets/ball/ball_fire.png"); // hình ảnh quả bóng lửa khi ăn power up
        paddleImg = new Image("file:assets/paddle/paddle.png"); // Hình ảnh thanh trượt
        menu = new Menu(); // Khởi tạo menu
    }

    public void render(GraphicsContext gc, GameManager gameManager) throws Exception { // Phương thức vẽ tất cả các trạng thái của game

        gc.clearRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT); // Xóa khung vẽ trước khi vẽ lại
        gc.drawImage(backgroundScreen, 0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT); // Vẽ nền chung
        GameState state = gameManager.getCurrentGameState(); // Lấy trạng thái hiện tại của game

        switch (state) {
            case Menu, Setting, SoundSetting -> {
                gc.drawImage(backgroundMenu, GameConfig.SCREEN_X, GameConfig.SCREEN_Y,
                        GameConfig.SCREEN_PLAY_WIDTH, GameConfig.SCREEN_PLAY_HEIGHT);
                if (state == GameState.SoundSetting) {
                    menu.updateSoundSettingsMenuItemsText();
                }
                renderMenu(gc, menu);
            }

            case LevelComplete -> {
                gc.drawImage(backgroundMenu, GameConfig.SCREEN_X, GameConfig.SCREEN_Y,
                        GameConfig.SCREEN_PLAY_WIDTH, GameConfig.SCREEN_PLAY_HEIGHT);

                renderLevelComplete(gc, gameManager); // 👈 gọi hàm vẽ màn LevelComplete
            }
            case Option -> {
                renderGame(gc, gameManager);
                renderOptionOverlay(gc);
                renderMenu(gc, menu);
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

    private void renderLevelComplete(GraphicsContext gc, GameManager gameManager) throws FileNotFoundException {
        // vẽ hình và text
        Image top1Img = new Image(new FileInputStream("assets/background/top1.png"));
        Image top2Img = new Image(new FileInputStream("assets/background/top2.png"));
        Image top3Img = new Image(new FileInputStream("assets/background/top3.png"));
        int[] topScore = gameManager.getListHighScore();

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Algerian", 60));
        gc.drawImage(top1Img, GameConfig.SCREEN_X + 225, 430, 30, 30);
        gc.drawImage(top2Img, GameConfig.SCREEN_X + 225, 500, 30, 30);
        gc.drawImage(top3Img, GameConfig.SCREEN_X + 225, 570, 30, 30);

        gc.setFont(new Font("Algerian", 55));
        gc.fillText("YOUR SCORE", GameConfig.SCREEN_X + 290, 230);
        gc.fillText("TOP SCORE", GameConfig.SCREEN_X + 290, 370);

        gc.setFont(new Font("Arial", 50));
        gc.fillText("" + gameManager.getScore(), GameConfig.SCREEN_X + 290, 295);

        gc.setFont(new Font("Arial", 40));
        gc.fillText("" + topScore[0], GameConfig.SCREEN_X + 325, 445);
        gc.fillText("" + topScore[1], GameConfig.SCREEN_X + 325, 515);
        gc.fillText("" + topScore[2], GameConfig.SCREEN_X + 325, 585);

        // Gọi vẽ menu "NEXT LEVEL" và "BACK TO MENU"
        renderMenu(gc, menu);
    }

    private void renderMenu(GraphicsContext gc, Menu menu) {
        String title = "ARKANOID";

        if (menu.getMenuItems() == menu.getPauseMenu()) { // Kiểm tra nếu là menu Pause
            title = "PAUSED";
        } else if (menu.getMenuItems() == menu.getSettingsMenu()) { // Kiểm tra nếu là menu Setting
            title = "OPTIONS";
        } else if (menu.getMenuItems() == menu.getSoundSettingsMenu()) { // Kiểm tra nếu là menu Sound Settings
            title = "SOUND";
        } else if (menu.getMenuItems() == menu.getLevelCompleteMenu()) {
            title = "LEVEL WIN!";
        }

        // Vẽ tiêu đề menu
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
        gc.setFont(new Font("Algerian", 70));
        gc.fillText("LEVEL " + gameManager.getCurrentLevel(), 200, 350);
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

        gc.setFont(Font.font("Algerian", FontWeight.BOLD, 30));
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + gameManager.getScore(), 1200, 330);
        gc.fillText("Lives: " + gameManager.getLives(), 1200, 380);
    }

    private void renderGameOver(GraphicsContext gc, GameManager gameManager) throws FileNotFoundException {
        Image game_overImg, top1Img, top2Img, top3Img;
        game_overImg = new Image(new FileInputStream("assets/background/game_over.png"));
        top1Img = new Image(new FileInputStream("assets/background/top1.png"));
        top2Img = new Image(new FileInputStream("assets/background/top2.png"));
        top3Img = new Image(new FileInputStream("assets/background/top3.png"));
        int[] topScore = gameManager.getListHighScore();
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        if (gameManager.getPlayerWin()) {
            gc.setFill(Color.WHITE);
            gc.setFont(new Font("Algerian", 70));
            gc.fillText("YOU WIN!", GameConfig.SCREEN_X + 300, 130);
            gc.drawImage(top1Img, GameConfig.SCREEN_X + 225, 450, 30, 30);
            gc.drawImage(top2Img, GameConfig.SCREEN_X + 225, 520, 30, 30);
            gc.drawImage(top3Img, GameConfig.SCREEN_X + 225, 590, 30, 30);
            gc.setFont(new Font("Algerian", 65));
            gc.fillText("YOUR SCORE", GameConfig.SCREEN_X + 290, 230);
            gc.fillText("TOP SCORE", GameConfig.SCREEN_X + 290, 390);
            gc.setFont(new Font("Arial", 60));
            gc.fillText("" + gameManager.getScore(), GameConfig.SCREEN_X + 290, 315);
            gc.setFont(new Font("Arial", 45));

            gc.fillText("" + topScore[0], GameConfig.SCREEN_X + 325, 465);
            gc.fillText("" + topScore[1], GameConfig.SCREEN_X + 325, 535);
            gc.fillText("" + topScore[2], GameConfig.SCREEN_X + 325, 605);
        }

        gc.setFill(Color.WHITE);
        if (gameManager.getCurrentLevel() < 7) {
            gc.drawImage(game_overImg, GameConfig.SCREEN_X, GameConfig.SCREEN_Y);
            gc.drawImage(top1Img, GameConfig.SCREEN_X + 225, 450, 30, 30);
            gc.drawImage(top2Img, GameConfig.SCREEN_X + 225, 520, 30, 30);
            gc.drawImage(top3Img, GameConfig.SCREEN_X + 225, 590, 30, 30);
            gc.setFont(new Font("Arial", 60));
            gc.fillText("" + gameManager.getScore(), GameConfig.SCREEN_X + 290, 330);
            gc.setFont(new Font("Arial", 45));

            gc.fillText("" + topScore[0], GameConfig.SCREEN_X + 325, 465);
            gc.fillText("" + topScore[1], GameConfig.SCREEN_X + 325, 535);
            gc.fillText("" + topScore[2], GameConfig.SCREEN_X + 325, 605);

        }
        gc.setFont(new Font("Arial", 20));
        gc.fillText("Press Enter to Return to Menu", GameConfig.SCREEN_X + 300, 700);
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
