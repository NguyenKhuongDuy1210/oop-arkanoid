// In Managers/InputHandler.java
package Managers;

import Managers.GameConfig.GameConfig;
import Managers.MenuManager.GameState;
import Managers.MenuManager.Menu;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import Managers.MenuManager.MenuItem;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Lớp xử lý đầu vào từ chuột cho game.
 */
public class InputHandler {

    private GameManager gameManager;
    private Menu menu;
    private Stage stage; // Cửa sổ chính của game, dùng để đóng ứng dụng khi chọn "EXIT"
    private boolean leftPressed = false; // trạng thái phím trái
    private boolean rightPressed = false; // trạng thái phím phải
    private final double PADDLE_SPEED = 5.0; // tốc độ di chuyển của paddle

    public InputHandler(GameManager gameManager, Menu menu, Stage stage) {
        this.gameManager = gameManager;
        this.menu = menu;
        this.stage = stage;
    }

    /**
     * Gắn các bộ lắng nghe sự kiện vào Scene chính của game.
     */
    public void attach(Scene scene) {

        // XỬ LÝ CHUỘT 
        scene.setOnMouseMoved(e -> {
            if (gameManager.getCurrentGameState() == GameState.Menu
                    || gameManager.getCurrentGameState() == GameState.Option
                    || gameManager.getCurrentGameState() == GameState.Setting
                    || gameManager.getCurrentGameState() == GameState.SoundSetting
                    || gameManager.getCurrentGameState() == GameState.LevelComplete) { // Đang ở bên trong 1 trong 3 cái.
                handleMouseMove(e.getX(), e.getY()); // Cập nhật vị trí chuột trong menu
            } else if (gameManager.getCurrentGameState() == GameState.Playing) { // Chỉ khi đang chơi
                gameManager.updatePaddlePosition(e.getX()); // Cập nhật vị trí thanh trượt
            }
        });

        scene.setOnMousePressed(e -> {

            if (gameManager.getCurrentGameState() == GameState.LevelComplete) {
                handleMenuAction(); // Xử lý click chuột giống như nhấn Enter
            }

            if (gameManager.getCurrentGameState() == GameState.Menu
                    || gameManager.getCurrentGameState() == GameState.Option
                    || gameManager.getCurrentGameState() == GameState.Setting
                    || gameManager.getCurrentGameState() == GameState.SoundSetting
                    || gameManager.getCurrentGameState() == GameState.LevelComplete) // Đang ở bên trong 1 trong 3 cái.
            {
                handleMenuAction(); // Xử lý khi chọn mục trong menu
            } else if (gameManager.getCurrentGameState() == GameState.Playing) { // Chỉ khi đang chơi
                if (e.isPrimaryButtonDown()) { // Chuột trái bắn bóng
                    gameManager.releaseBall();
                }
            }
        });

        // XỬ LÝ BÀN PHÍM
        scene.setOnKeyPressed(e -> {

            if (gameManager.getCurrentGameState() == GameState.Menu
                    || gameManager.getCurrentGameState() == GameState.Option
                    || gameManager.getCurrentGameState() == GameState.Setting
                    || gameManager.getCurrentGameState() == GameState.SoundSetting
                    || gameManager.getCurrentGameState() == GameState.LevelComplete)  { // Đang ở bên trong 1 trong 3 cái.
                switch (e.getCode()) {
                    case UP: // Di chuyển lên trên
                        menu.navigateUp();
                        break;
                    case DOWN: // Di chuyển xuống dưới
                        menu.navigateDown();
                        break;
                    case ENTER: // Chọn mục trong menu
                        handleMenuAction();
                        break;
                    case LEFT: // Di chuyển trái (chỉ trong Sound Setting) 
                        if (gameManager.getCurrentGameState() == GameState.SoundSetting) {
                            handleVolumeChange(false); // false = giảm
                        }
                        break;
                    case RIGHT: // Di chuyển phải (chỉ trong Sound Setting)
                        if (gameManager.getCurrentGameState() == GameState.SoundSetting) {
                            handleVolumeChange(true); // true = tăng
                        }
                        break;
                }
            }

            // Xử lý ESCAPE (Menu/Option)
            if (e.getCode() == KeyCode.ESCAPE) {
                if (gameManager.getCurrentGameState() == GameState.Playing) {
                    gameManager.setCurrentGameState(GameState.Option); // Pause Game
                    menu.switchToPauseMenu(); // Chuyển danh sách menu
                } else if (gameManager.getCurrentGameState() == GameState.Option) {
                    gameManager.setCurrentGameState(GameState.Playing); // Resume Game
                } else if (gameManager.getCurrentGameState() == GameState.SoundSetting) { // từ Sound Setting về Setting
                    gameManager.setCurrentGameState(GameState.Setting);
                    menu.switchToSettingMenu();
                } else if (gameManager.getCurrentGameState() == GameState.Setting) {
                    gameManager.setCurrentGameState(GameState.Menu); // Thoát khỏi Settings về Main Menu
                    menu.switchToMainMenu();
                }
            }

            // Xử lý ENTER ở GameOver
            if (gameManager.getCurrentGameState() == GameState.GameOver && e.getCode() == KeyCode.ENTER) {
                handleMenuAction(); // Sẽ gọi logic quay về Menu
            }
        }
        );
    }

    private void handleVolumeChange(boolean increase) { // thay đổi âm lượng trong Sound Setting
        if (menu.getSelectedItem() == null) {
            return;
        }

        String selectedText = menu.getSelectedItem().getText(); // Lấy text của mục được chọn

        if (selectedText.startsWith("MUSIC VOL")) {
            gameManager.changeMusicVolume(increase);
        } else if (selectedText.startsWith("SFX VOL")) {
            gameManager.changeSfxVolume(increase);
        }
    }

    public void updatePaddleKeyboard() {
        if (gameManager.getCurrentGameState() != GameState.Playing) {
            return;
        }

        if (leftPressed) {
            double newX = gameManager.getPaddle().getX() - PADDLE_SPEED;
            gameManager.updatePaddlePosition(newX + gameManager.getPaddle().getWidth() / 2);
        }
        if (rightPressed) {
            double newX = gameManager.getPaddle().getX() + PADDLE_SPEED;
            gameManager.updatePaddlePosition(newX + gameManager.getPaddle().getWidth() / 2);
        }
    }

    public void handleMouseMove(double mouseX, double mouseY) {
        if (menu == null || menu.getMenuItems().isEmpty()) {
            return;
        }

        for (int i = 0; i < menu.getMenuItems().size(); i++) {
            MenuItem item = menu.getMenuItems().get(i);

            Text textNode = new Text(item.getText());
            textNode.setFont(menu.getItemFont());
            double textWidth = textNode.getLayoutBounds().getWidth();
            double textHeight = textNode.getLayoutBounds().getHeight() * 0.7;

            double xMin = item.getX() - textWidth / 2.0;
            double xMax = item.getX() + textWidth / 2.0;
            double yMin = item.getY() - textHeight / 2.0;
            double yMax = item.getY() + textHeight / 2.0;

            if (mouseX >= xMin && mouseX <= xMax && mouseY >= yMin && mouseY <= yMax) {
                if (menu.getSelectedItemIndex() != i) {
                    menu.setSelectedItemIndex(i);
                    menu.updateTargetY();
                }
                return;
            }
        }
    }

    private void handleMenuAction() { // Xử lý hành động khi chọn mục trong menu
        try {
            String selecString = menu.getSelectedItem().getText(); // Lấy text của mục được chọn

            if (gameManager.getCurrentGameState() == GameState.LevelComplete) {
                switch (selecString) {
                    case "NEXT LEVEL":
                        gameManager.proceedToNextLevel();
                        break;
                    case "BACK TO MENU":
                        gameManager.initGame();
                        menu.switchToMainMenu();
                        break;
                }
            }

            if (gameManager.getCurrentGameState() == GameState.LevelComplete) {
                switch (selecString) {
                    case "NEXT LEVEL":
                        gameManager.proceedToNextLevel(); // Chuyển sang level tiếp theo
                        break;
                    case "BACK TO MENU":
                        gameManager.initGame(); // Reset game
                        menu.switchToMainMenu();
                        break;
                }
            } else if (gameManager.getCurrentGameState() == GameState.SoundSetting) {

                gameManager.handleMenuSelection(selecString); // Xử lý lựa chọn trong Sound Setting

            } else if (gameManager.getCurrentGameState() == GameState.Menu) {
                switch (selecString) {
                    case "START":
                        gameManager.handleMenuSelection(selecString);  // Chuyển trạng thái game sang Playing
                        break;
                    case "OPTIONS":
                        gameManager.setCurrentGameState(GameState.Setting); // Chuyển trạng thái game sang Option
                        menu.switchToSettingMenu(); // Chuyển sang menu tùy chọn
                        break;
                    case "EXIT":
                        stage.close(); // Đóng cửa sổ game
                        break;
                }

            } else if (gameManager.getCurrentGameState() == GameState.Option) {

                gameManager.handleMenuSelection(selecString);

                if (selecString.equals("BACK TO MENU")) {
                    menu.switchToMainMenu(); // Quay lại menu chính
                }

            } else if (gameManager.getCurrentGameState() == GameState.Setting) {

                if (selecString.equals("BACK")) {
                    menu.switchToSettingMenu(); // chuyển menu hiển thị
                    return;
                }

                if (selecString.equals("LEVELS")) {
                    menu.switchToLevelsMenu(); // Chuyển sang menu Level
                    return;
                }

                if (selecString.startsWith("LEVEL ")) {
                    int level = Integer.parseInt(selecString.substring(6));
                    gameManager.startLevel(level);  // Thay cho initGame()
                    return;
                }

                gameManager.handleMenuSelection(selecString);

                if (selecString.equals("BACK TO MENU")) {
                    menu.switchToMainMenu(); // Quay lại menu chính
                }

            } else if (gameManager.getCurrentGameState() == GameState.GameOver) {
                gameManager.initGame(); // Reset game
                gameManager.setCurrentGameState(GameState.Menu); // Đặt state về Menu
                menu.switchToMainMenu(); // Quay lại hiển thị danh sách mục Main Menu
            }
        } catch (Exception e) {
            System.err.println("Lỗi xử lý menu: " + e.getMessage());
        }
    }
}
