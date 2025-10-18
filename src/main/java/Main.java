import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class Main extends Application {

    private Renderer renderer;
    private double ballX = 300, ballY = 300;
    private double paddleX = 250, paddleY = 500;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        renderer = new Renderer();

        new AnimationTimer() {
            public void handle(long now) {
                ballY += 3;
                renderer.render(gc, ballX, ballY, paddleX, paddleY);
            }
        }.start();

        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
