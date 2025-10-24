// In Managers/InputHandler.java
package Managers;

import Managers.MenuManager.GameState;
import Managers.MenuManager.Menu;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * Lớp xử lý đầu vào từ chuột cho game.
 */
public class InputHandler {

    private GameManager gameManager;
    private Menu menu;
    private Stage stage; // Thêm stage để có thể đóng cửa sổ từ menu

    public InputHandler(GameManager gameManager, Menu menu, Stage stage) {
        this.gameManager = gameManager;
        this.menu = menu;
        this.stage = stage;
    }

    /**
     * Gắn các bộ lắng nghe sự kiện vào Scene chính của game.
     */
    public void attach(Scene scene) {

        // --- XỬ LÝ SỰ KIỆN CHUỘT ---
        scene.setOnMouseMoved(e -> {
            if (gameManager.getCurrentGameState() == GameState.Menu) {
                menu.handleMouseMove(e.getX(), e.getY());
            } else if (gameManager.getCurrentGameState() == GameState.Playing) {
                gameManager.updatePaddlePosition(e.getX());
            }
        });
        scene.setOnKeyPressed(e -> {
            if (gameManager.getCurrentGameState() == GameState.GameOver) {
                if (e.getCode() == KeyCode.ENTER) {
                    try {
                        gameManager.initGame();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    gameManager.setCurrentGameState(GameState.Menu); // quay về menu
                }
            }
        });

        scene.setOnMousePressed(e -> {
            if (gameManager.getCurrentGameState() == GameState.Menu) {
                handleMenuSelection();
            } else if (gameManager.getCurrentGameState() == GameState.Playing) {
                if (e.isPrimaryButtonDown()) { // Chuột trái bắn bóng
                    gameManager.releaseBall();
                }
            }
        });
    }

    /**
     * Xử lý khi một mục trong menu được chọn (bằng chuột).
     */
    private void handleMenuSelection() {
        int selectedIndex = menu.getSelectedItemIndex();
        switch (selectedIndex) {
            case 0: // Bắt đầu
                gameManager.setCurrentGameState(GameState.Playing);
                break;
            case 1: // Tùy chọn (chưa có chức năng)
                System.out.println("Tùy chọn được chọn!");
                break;
            case 2: // Thoát
                stage.close();
                break;
        }
    }
}
