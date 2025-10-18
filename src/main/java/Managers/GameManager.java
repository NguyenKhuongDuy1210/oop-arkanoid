package Managers;

import items.Ball;
import items.Brick;
import items.Paddle;

import java.util.ArrayList;
import java.util.Iterator;
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
        paddle = new Paddle(340, 550, 120, 30, 5);
        ball = new Ball(350, 530, 20, 20, 4, 1, -1);

        bricks = new ArrayList<>();
        createBricks();

        score = 0;
        lives = 3;
        isPaused = false;
        isGameOver = false;
    }

    // Tạo các viên gạch
    private void createBricks() {
        int rows = 3;
        int cols = 6;
        int brickWidth = 80;
        int brickHeight = 25;
        int gap = 10;

        int totalWidth = cols * (brickWidth + gap) - gap;
        int startX = (800 - totalWidth) / 2;
        int startY = 80;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = startX + c * (brickWidth + gap);
                int y = startY + r * (brickHeight + gap);
                bricks.add(new Brick(x, y, brickWidth, brickHeight, 1));
            }
        }
    }

    // Cập nhật game
    public void update() {
        if (isPaused || isGameOver) return;

        ball.update(paddle);
        paddle.update();


        // Va chạm gạch
        Iterator<Brick> it = bricks.iterator();
        while(it.hasNext()) {
            Brick brick = it.next();
            brick.update();
            if(ball.checkCollision(brick) && !brick.isDestroyed()) {
                brick.takeHit();
                score += 10;
            }
            if(brick.isDestroyed()) it.remove();
        }


        // Kiểm tra bóng ra ngoài
        if (ball.getdY() >= 600) {
            lives--;
            if (lives <= 0) {
                isGameOver = true;
            } else {
                resetRound();
            }
        }

        // Kiểm tra thắng game
        if (bricks.isEmpty()) {
            isGameOver = true; // Hoặc initGame() để restart
        }
    }

    private void resetRound() {
        ball = new Ball(350, 530, 20, 20, 4, 1, -1);
        paddle = new Paddle(340, 550, 120, 30, 5);
    }

    // Điều khiển paddle
    public void movePaddleLeft() {
        paddle.moveLeft();
    }

    public void movePaddleRight() {
        paddle.moveRight();
    }

    public void togglePause() {
        isPaused = !isPaused;
    }

    // Getters
    public Ball getBall() { return ball; }
    public Paddle getPaddle() { return paddle; }
    public List<Brick> getBricks() { return bricks; }
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public boolean isGameOver() { return isGameOver; }
}
