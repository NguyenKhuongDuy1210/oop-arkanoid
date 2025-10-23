package Managers;

import Managers.GameConfig.GameConfig;
import Managers.MenuManager.GameState;
import items.Ball;
import items.Brick;
import items.Paddle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// GameState

public class GameManager {

    private Ball ball;
    private Paddle paddle;
    private List<Brick> bricks;
    private int score;
    private int lives;
    private boolean isPaused;
    private GameState current_GameState;
    private boolean playerWin;

    // Constructor
    public GameManager() {
        initGame();
    }

    // Khởi tạo game
    public void initGame() {
        paddle = new Paddle(280, 600, GameConfig.PADDLE_WIDTH, GameConfig.PADDLE_HEIGHT, GameConfig.PADDLE_SPEED);
        ball = new Ball(350, 530, GameConfig.BALL_WIDTH, GameConfig.BALL_HEIGHT, GameConfig.BALL_SPEED, 1, -1);

        bricks = new ArrayList<>();
        createBricks();

        score = 0;
        lives = 1;
        isPaused = false;
        playerWin = false;

        current_GameState = GameState.Menu;
    }

    // Tạo các viên gạch
    private void createBricks() {
        int rows = 3;
        int cols = 8;

        int totalWidth = cols * (GameConfig.BRICK_WIDTH + GameConfig.BRICK_GAP) - GameConfig.BRICK_GAP;
        int startX = (600 - totalWidth) / 2;
        int startY = 80;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = startX + c * (GameConfig.BRICK_WIDTH + GameConfig.BRICK_GAP);
                int y = startY + r * (GameConfig.BRICK_HEIGHT + GameConfig.BRICK_GAP);
                bricks.add(new Brick(x, y, GameConfig.BRICK_WIDTH, GameConfig.BRICK_HEIGHT, 1));
            }
        }
    }

    // Cập nhật game
    public void update() {
        if (current_GameState != GameState.Playing || isPaused) return;

        ball.update(paddle);
        paddle.update();


        // Va chạm gạch
        Iterator<Brick> it = bricks.iterator();
        while(it.hasNext()) {
            Brick brick = it.next();
            brick.update();
            if(ball.checkCollision(brick) && !brick.isDestroyed()) {
                brick.setOnHit(true);
                score += 10;
            }
            if(brick.isDestroyed()) it.remove();
        }


        // Kiểm tra bóng ra ngoài
        if (ball.getY() >= 750) {
            lives--;
            if (lives <= 0) {
                playerWin = false;
                current_GameState = GameState.GameOver;
            } else {
                resetRound();
            }
        }

        // Kiểm tra thắng game
        if (bricks.isEmpty()) {
            playerWin = true;
            current_GameState = GameState.GameOver;
        }
    }

    private void resetRound() {
        paddle = new Paddle(280, 600, 120, 20, 5);
        ball = new Ball(350, 530, 15, 15, 2, 1, -1);
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
    public GameState getCurrent_GameState() { return current_GameState; }
    public void setCurrent_GameState(GameState state) { this.current_GameState = state; }
    public boolean getPlayerWin() { return playerWin; }
}
