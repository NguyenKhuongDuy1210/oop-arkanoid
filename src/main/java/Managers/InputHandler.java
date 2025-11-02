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

        // --- XỬ LÝ CHUỘT ---
        scene.setOnMouseMoved(e -> {
            if (gameManager.getCurrentGameState() == GameState.Menu ||
                gameManager.getCurrentGameState() == GameState.Option ||
                gameManager.getCurrentGameState() == GameState.Setting) // Đang ở bên trong 1 trong 3 cái.
            {
                menu.handleMouseMove(e.getX(), e.getY()); // Cập nhật vị trí chuột trong menu
            } 
            else if (gameManager.getCurrentGameState() == GameState.Playing) {
                gameManager.updatePaddlePosition(e.getX()); // Cập nhật vị trí thanh trượt
            }
        });

        scene.setOnMousePressed(e -> {
            if (gameManager.getCurrentGameState() == GameState.Menu ||
                gameManager.getCurrentGameState() == GameState.Option ||
                gameManager.getCurrentGameState() == GameState.Setting) // Đang ở bên trong 1 trong 3 cái.
            {
                handleMenuAction(); // Xử lý khi chọn mục trong menu
            } 
            else if (gameManager.getCurrentGameState() == GameState.Playing) {
                if (e.isPrimaryButtonDown()) { // Chuột trái bắn bóng
                    gameManager.releaseBall();
                }
            }
        });

        // --- XỬ LÝ BÀN PHÍM ---
        scene.setOnKeyPressed(e -> {
            // Xử lý chung cho Menu/Option (UP, DOWN, ENTER)
            if (gameManager.getCurrentGameState() == GameState.Menu ||
                gameManager.getCurrentGameState() == GameState.Option ||
                gameManager.getCurrentGameState() == GameState.Setting) // Đang ở bên trong 1 trong 3 cái.
            {
                switch (e.getCode()) {
                    case UP:
                        menu.navigateUp();
                        break;
                    case DOWN:
                        menu.navigateDown();
                        break;
                    case ENTER:
                        handleMenuAction();
                        break;
                }
            }

            // Xử lý riêng cho Playing (SPACE)
            if (gameManager.getCurrentGameState() == GameState.Playing && e.getCode() == KeyCode.SPACE) {
                gameManager.releaseBall();
            }

            // Xử lý ESCAPE (Menu/Option)
            if (e.getCode() == KeyCode.ESCAPE) {
                if (gameManager.getCurrentGameState() == GameState.Playing) {
                    gameManager.setCurrentGameState(GameState.Option); // Pause Game
                    menu.switchToPauseMenu(); // Chuyển danh sách menu
                } 
                else if (gameManager.getCurrentGameState() == GameState.Option) {
                    gameManager.setCurrentGameState(GameState.Playing); // Resume Game
                }  
                else if (gameManager.getCurrentGameState() == GameState.Setting) {
                    gameManager.setCurrentGameState(GameState.Menu); // Thoát khỏi Settings về Main Menu
                    menu.switchToMainMenu();
                }
            }

            // Xử lý ENTER ở GameOver
            if (gameManager.getCurrentGameState() == GameState.GameOver && e.getCode() == KeyCode.ENTER) {
                handleMenuAction(); // Sẽ gọi logic quay về Menu
            }
        });
    }

    private void handleMenuAction() {
        try {
            String selecString = menu.getSelectedItem().getText();
            
            if (gameManager.getCurrentGameState() == GameState.Menu) {
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
                

            }
            else if (gameManager.getCurrentGameState() == GameState.Setting) {
                
                gameManager.handleMenuSelection(selecString);

                if (selecString.equals("BACK TO MENU")) {
                    menu.switchToMainMenu(); // Quay lại menu chính
                }
                
            }
            else if (gameManager.getCurrentGameState() == GameState.GameOver) {
                gameManager.initGame(); // Reset game
                gameManager.setCurrentGameState(GameState.Menu); // Đặt state về Menu
                menu.switchToMainMenu(); // Quay lại hiển thị danh sách mục Main Menu
            }
        } catch (Exception e) {
            System.err.println("Lỗi xử lý menu: " + e.getMessage());
        }
    }
}
