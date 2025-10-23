import Managers.GameManager;
import Managers.MenuManager.GameState;
import Managers.Renderer;
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
            switch (gameManager.getCurrent_GameState()) {
                case Menu:
                    if (e.getCode() == KeyCode.UP) {
                        renderer.getMenu().navigateUp();
                    } else if (e.getCode() == KeyCode.DOWN) {
                        renderer.getMenu().navigateDown();
                    } else if (e.getCode() == KeyCode.ENTER) {
                        int selected = renderer.getMenu().getSelectedItemIndex();
                        if (selected == 0) { // Start Game
                            gameManager.setCurrent_GameState(GameState.Playing);
                        } else if (selected == 1) { // Exit
                            stage.close();
                        }
                    }
                    break;
                case Playing:
                    if (e.getCode() == KeyCode.LEFT) leftPressed = true;
                    if (e.getCode() == KeyCode.RIGHT) rightPressed = true;
                    // Bạn có thể thêm nút Pause (ví dụ: P) ở đây
                    // if (e.getCode() == KeyCode.P) gameManager.togglePause();
                    break;
                case GameOver:
                    if (e.getCode() == KeyCode.ENTER) {
                        gameManager.initGame(); // Quay về trạng thái ban đầu (Menu)
                    }
                    break;
            }
        });

        scene.setOnKeyReleased(e -> {
            if (gameManager.getCurrent_GameState() == GameState.Playing) {
                if (e.getCode() == KeyCode.LEFT) leftPressed = false;
                if (e.getCode() == KeyCode.RIGHT) rightPressed = false;
            }
        });

        stage.setScene(scene);
        stage.setTitle("Arkanoid Game");
        stage.show();

        // AnimationTimer chính
        new AnimationTimer() {
            @Override
            public void handle(long now) {

//                Paddle paddle = gameManager.getPaddle();
//                if (leftPressed) paddle.moveLeft();
//                if (rightPressed) paddle.moveRight();

                // Di chuyển paddle
                if (gameManager.getCurrent_GameState() == GameState.Playing) {
                    if (leftPressed) gameManager.movePaddleLeft();
                    if (rightPressed) gameManager.movePaddleRight();
                }

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
