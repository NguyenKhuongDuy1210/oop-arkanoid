import items.Brick;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;

public class Renderer {

    private List<Brick> bricks;
    private Image backgroundImg, ballImg, paddleImg, brickImg;

    public Renderer() {
        backgroundImg = new Image("file:assets/background.png");
        ballImg       = new Image("file:assets/ball.png");
        paddleImg     = new Image("file:assets/paddle.png");
        brickImg      = new Image("file:assets/normal brick2.png");
        bricks = new ArrayList<>();
    }

    public void render(GraphicsContext gc, double ballX, double ballY,
                       double paddleX, double paddleY) {

        gc.drawImage(backgroundImg, 0, 0, 800, 600);

        int brickWidth = 80;
        int brickHeight = 25;
        int gap = 10;
        int rows = 3;
        int cols = 6;

        // TÍNH CANH GIỮA CHUẨN
        int totalWidth = cols * (brickWidth + gap) - gap;
        int startX = (800 - totalWidth) / 2;   // Màn hình 800 width
        int startY = 80;  // Cách top 80px (thoáng hơn)

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = startX + col * (brickWidth + gap);
                int y = startY + row * (brickHeight + gap);
                gc.drawImage(brickImg,x, y, brickWidth, brickHeight);
            }
        }
        //brickImg, 100, 100, 80, 30);

        gc.drawImage(paddleImg, paddleX, paddleY, 120, 30);

        gc.drawImage(ballImg, ballX, ballY, 20, 20);
    }
}
