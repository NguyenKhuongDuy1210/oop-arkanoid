package Managers;

import items.Ball;
import items.Brick;
import items.Paddle;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private Ball ball;
    private Paddle paddle;
    private List<Brick> bricks;
    private int score;
    private int lives;
    private boolean isPaused;
    private boolean isGameOver;

    // Constructor
    public GameManager() {
        initGame();
    }

    // Khởi tạo game
    public void initGame() {
        // tạo paddle và ball
        paddle = new Paddle(300, 550, 100, 15,4 );
        ball = new Ball(340, 530, 15, 15, 5, 1, -1);

        // danh sách gạch
        bricks = new ArrayList<>();
        createBricks();

        score = 0;
        lives = 3;
        isPaused = false;
        isGameOver = false;
    }

    // Tạo các viên gạch
    private void createBricks() {
        int rows = 5;
        int cols = 10;
        int brickWidth = 60;
        int brickHeight = 20;
        int startX = 40;
        int startY = 50;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                bricks.add(new Brick(startX + c * brickWidth, startY + r * brickHeight, brickWidth, brickHeight,1));
            }
        }
    }

    // Cập nhật logic game
    public void update() {
        if (isPaused || isGameOver) return;

        ball.update();
        paddle.update();

         //Va chạm paddle
        ball.checkCollision(paddle); // xử lý va chạm thông minh

        // Va chạm gạch
        for (Brick brick : bricks) {
            ball.checkCollision(brick);
        }

        // Kiểm tra bóng ra khỏi màn
        if (ball.getdY() > 600) { // ví dụ chiều cao khung hình = 600
            lives--;
            if (lives <= 0) {
                isGameOver = true;
            } else {
                resetRound();
            }
        }
    }

    // Vẽ toàn bộ đối tượng
    public void render(Graphics g) {
        ball.render(g);
        paddle.render(g);

        for (Brick brick : bricks) {
            if (brick.isVisible()) brick.render(g);
        }

        g.drawString("Score: " + score, 20, 20);
        g.drawString("Lives: " + lives, 520, 20);

        if (isGameOver) {
            g.drawString("GAME OVER!", 300, 300);
        }
    }

    // Reset bóng và thanh khi mất mạng
    private void resetRound() {
        ball = new Ball(340, 530, 15, 15, 5, 1, -1);
        paddle = new Paddle(300, 550, 100, 15,-1);
    }

    // Điều khiển paddle
    public void movePaddleLeft() {
        paddle.moveLeft();
    }

    public void movePaddleRight() {
        paddle.moveRight();
    }

    // Dừng / tiếp tục game
    public void togglePause() {
        isPaused = !isPaused;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
}
