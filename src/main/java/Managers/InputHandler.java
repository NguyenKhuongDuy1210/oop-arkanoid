// In Managers/InputHandler.java
package Managers;

import Managers.MenuManager.GameState;
import Managers.MenuManager.Menu;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * Lớp chuyên xử lý tất cả các đầu vào từ người dùng (bàn phím, chuột).
 * Lớp này giúp tách biệt logic xử lý input ra khỏi lớp Main, làm cho code sạch sẽ hơn.
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
     * @param scene Scene của trò chơi.
     */
    public void attach(Scene scene) {
        // --- XỬ LÝ SỰ KIỆN BÀN PHÍM ---
        scene.setOnKeyPressed(e -> handleKeyPress(e.getCode()));
        scene.setOnKeyReleased(e -> handleKeyRelease(e.getCode()));

        // --- XỬ LÝ SỰ KIỆN CHUỘT ---
        scene.setOnMouseMoved(e -> {
            if (gameManager.getCurrentGameState() == GameState.Menu) {
                menu.handleMouseMove(e.getX(), e.getY());
            }
        });

        scene.setOnMouseClicked(e -> {
            if (gameManager.getCurrentGameState() == GameState.Menu) {
                handleMenuSelection();
            }
        });
    }

    /**
     * Xử lý khi một phím được nhấn.
     * @param code Mã của phím được nhấn.
     */
    private void handleKeyPress(KeyCode code) {
        switch (gameManager.getCurrentGameState()) {
            case Menu:
                if (code == KeyCode.UP) menu.navigateUp();
                if (code == KeyCode.DOWN) menu.navigateDown();
                if (code == KeyCode.ENTER) handleMenuSelection();
                break;
            case Playing:
                if (code == KeyCode.LEFT) gameManager.setPaddleMovingLeft(true);
                if (code == KeyCode.RIGHT) gameManager.setPaddleMovingRight(true);
                // Ví dụ: thêm nút Pause
                // if (code == KeyCode.P) gameManager.togglePause();
                break;
            case GameOver:
                if (code == KeyCode.ENTER) gameManager.initGame();
                break;
        }
    }

    /**
     * Xử lý khi một phím được nhả ra.
     * @param code Mã của phím được nhả.
     */
    private void handleKeyRelease(KeyCode code) {
        if (gameManager.getCurrentGameState() == GameState.Playing) {
            if (code == KeyCode.LEFT) gameManager.setPaddleMovingLeft(false);
            if (code == KeyCode.RIGHT) gameManager.setPaddleMovingRight(false);
        }
    }

    /**
     * Xử lý hành động khi một mục trong menu được chọn (bằng Enter hoặc chuột).
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