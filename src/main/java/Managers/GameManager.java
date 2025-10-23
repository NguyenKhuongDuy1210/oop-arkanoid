// In Managers/GameManager.java
package Managers;

import Managers.GameConfig.GameConfig;
import Managers.MenuManager.GameState;
import items.Ball;
import items.Brick;
import items.Paddle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * "Bộ não" của trò chơi. Quản lý tất cả các đối tượng, trạng thái
 * (điểm, mạng), và logic chính khi game đang ở trạng thái Playing.
 */
public class GameManager {

    // ... (Các thuộc tính ball, paddle, bricks, score, lives giữ nguyên) ...
    private Ball ball;
    private Paddle paddle;
    private List<Brick> bricks;
    private int score;
    private int lives;
    private GameState currentGameState; // Đổi tên theo quy ước camelCase
    private boolean playerWin;

    // Trạng thái di chuyển của paddle, được quản lý bởi InputHandler
    private boolean paddleMovingLeft = false;
    private boolean paddleMovingRight = false;

    public GameManager() {
        initGame();
    }

    /**
     * Thiết lập lại trò chơi về trạng thái ban đầu (nhưng vẫn giữ ở màn hình menu).
     */
    public void initGame() {
        paddle = new Paddle(280, 600, GameConfig.PADDLE_WIDTH, GameConfig.PADDLE_HEIGHT, GameConfig.PADDLE_SPEED);
        ball = new Ball(350, 530, GameConfig.BALL_WIDTH, GameConfig.BALL_HEIGHT, GameConfig.BALL_SPEED, 1, -1);
        bricks = new ArrayList<>();
        createBricks();
        score = 0;
        lives = 2;
        playerWin = false;
        paddleMovingLeft = false;
        paddleMovingRight = false;
        currentGameState = GameState.Menu;
    }

    /**
     * Cập nhật logic của game mỗi frame.
     */
    public void update() {
        // Chỉ cập nhật logic khi đang chơi
        if (currentGameState != GameState.Playing) return;

        // Cập nhật vị trí paddle dựa trên trạng thái di chuyển
        if (paddleMovingLeft) paddle.moveLeft();
        if (paddleMovingRight) paddle.moveRight();

        ball.update(paddle);
        paddle.update();

        // Va chạm gạch
        Iterator<Brick> it = bricks.iterator();
        while (it.hasNext()) {
            Brick brick = it.next();
            brick.update();
            if (ball.checkCollision(brick) && !brick.isDestroyed()) {
                brick.setOnHit(true);
                score += 10;
            }
            if (brick.isDestroyed()) it.remove();
        }

        // Kiểm tra bóng ra ngoài
        if (ball.getY() >= 750) {
            lives--;
            if (lives <= 0) {
                playerWin = false;
                currentGameState = GameState.GameOver;
            } else {
                resetRound();
            }
        }

        // Kiểm tra thắng game
        if (bricks.isEmpty()) {
            playerWin = true;
            currentGameState = GameState.GameOver;
        }
    }
    
    // ... (createBricks, resetRound giữ nguyên) ...
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

    private void resetRound() {
        paddle = new Paddle(280, 600, 120, 20, 5);
        ball = new Ball(350, 530, 15, 15, 2, 1, -1);
    }

    // --- Getters và Setters ---

    public void setPaddleMovingLeft(boolean moving) {
        this.paddleMovingLeft = moving;
    }

    public void setPaddleMovingRight(boolean moving) {
        this.paddleMovingRight = moving;
    }

    public GameState getCurrentGameState() { return currentGameState; }
    public void setCurrentGameState(GameState state) { this.currentGameState = state; }
    
    // ... (các getter khác giữ nguyên) ...
    public Ball getBall() { return ball; }
    public Paddle getPaddle() { return paddle; }
    public List<Brick> getBricks() { return bricks; }
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public boolean getPlayerWin() { return playerWin; }
}