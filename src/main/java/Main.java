// In Main.java
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
    public void start(Stage stage) {

        Canvas canvas = new Canvas(600, 750);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);

        GameManager gameManager = new GameManager();
        Renderer renderer = new Renderer();
        // InputHandler cần biết về gameManager, menu và stage để hoạ t động
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
                renderer.render(gc, gameManager);
            }
        };
        gameLoop.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}