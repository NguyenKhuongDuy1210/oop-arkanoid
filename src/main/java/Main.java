// In Main.java

import Managers.GameConfig.GameConfig;
import Managers.GameManager;
import Managers.InputHandler;
import Managers.Renderer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Canvas canvas = new Canvas(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);

        GameManager gameManager = new GameManager();
        Renderer renderer = new Renderer();

        gameManager.setMenu(renderer.getMenu()); // Đặt menu cho gameManager
        // InputHandler cần biết về gameManager, menu và stage để hoạt động
        InputHandler inputHandler = new InputHandler(gameManager, renderer.getMenu(), stage);
        inputHandler.attach(scene); // Gắn trình xử lý input vào scene

        stage.setScene(scene);
        stage.setTitle("Arkanoid");
        stage.setResizable(false);
        stage.show();

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // 1. Cập nhật trạng thái game
                gameManager.update();
                renderer.getMenu().update(); // Cập nhật hiệu ứng cho menu

                // 2. Vẽ mọi thứ lên màn hình
                try {
                    renderer.render(gc, gameManager);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        gameLoop.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
