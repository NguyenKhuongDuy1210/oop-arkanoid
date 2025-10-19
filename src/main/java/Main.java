import Managers.GameManager;
import Managers.Renderer;
import items.Ball;
import items.Paddle;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    private Renderer renderer;
    private GameManager gameManager;

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    @Override
    public void start(Stage stage) {

        Canvas canvas = new Canvas(600, 750);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Khởi tạo game và renderer
        gameManager = new GameManager();
        renderer = new Renderer();

        // Scene và xử lý bàn phím
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) leftPressed = true;
            if (e.getCode() == KeyCode.RIGHT) rightPressed = true;
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.LEFT) leftPressed = false;
            if (e.getCode() == KeyCode.RIGHT) rightPressed = false;
        });

        stage.setScene(scene);
        stage.setTitle("Arkanoid Game");
        stage.show();

        // AnimationTimer chính
        new AnimationTimer() {
            @Override
            public void handle(long now) {

                // Di chuyển paddle
                Paddle paddle = gameManager.getPaddle();
                if (leftPressed) paddle.moveLeft();
                if (rightPressed) paddle.moveRight();

                // Cập nhật game logic
                gameManager.update();

                // Vẽ game
                renderer.render(gc, gameManager);
            }
        }.start();
    }

    public static void main(String[] args) {
        launch();
    }
}
