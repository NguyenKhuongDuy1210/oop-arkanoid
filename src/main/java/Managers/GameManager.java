package Managers;

import Managers.GameConfig.GameConfig;
import Managers.MapManager.MapGame;
import Managers.MenuManager.GameState;
import items.Ball;
import items.Brick;
import items.Paddle;

import java.util.Iterator;
import java.util.List;

/**
 * "Bộ não" của trò chơi. Quản lý tất cả các đối tượng, trạng thái
 * (điểm, mạng), và logic chính khi game đang ở trạng thái Playing.
 */
public class GameManager {

    private Ball ball;
    private Paddle paddle;
    private MapGame mapBrick;
    private int score;
    private int lives;
    private GameState currentGameState;
    private boolean playerWin;
    private boolean ballAttachedToPaddle = true; // bóng dính paddle chờ bắn

    public GameManager() throws Exception {
        initGame();
    }

    /**
     * Thiết lập lại trò chơi về trạng thái ban đầu
     */
    public void initGame() throws Exception {
        paddle = new Paddle(280, 600, GameConfig.PADDLE_WIDTH, GameConfig.PADDLE_HEIGHT);
        float ballX = paddle.getX() + paddle.getWidth()/2 - GameConfig.BALL_WIDTH/2;
        float ballY = paddle.getY() - GameConfig.BALL_HEIGHT;
        ball = new Ball(ballX, ballY, GameConfig.BALL_WIDTH, GameConfig.BALL_HEIGHT, GameConfig.BALL_SPEED, 0f, -1f);
        mapBrick = new MapGame();
        mapBrick.createMapBricks();
        score = 0;
        lives = 3;
        playerWin = false;
        currentGameState = GameState.Menu;
        ballAttachedToPaddle = true;
    }

    /**
     * Cập nhật logic của game mỗi frame.
     */
    public void update() {
        if (currentGameState != GameState.Playing) return;
        paddle.update();
        // Cập nhật bóng
        if (ballAttachedToPaddle) {
            ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2);
            ball.setY(paddle.getY() - ball.getHeight());
        } else {
            ball.update(paddle);
        }

        // Va chạm với gạch
        Iterator<Brick> it = mapBrick.getMapBricks().iterator();
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
        if (mapBrick.getMapBricks().isEmpty()) {
            playerWin = true;
            currentGameState = GameState.GameOver;
        }
    }

    public void releaseBall() {
        if (ballAttachedToPaddle) {
            ballAttachedToPaddle = false;
        }
    }

    /** Cập nhật vị trí paddle theo chuột */
    public void updatePaddlePosition(double mouseX) {
        float newX = (float) (mouseX - paddle.getWidth() / 2);
        if (newX < 0) newX = 0;
        if (newX > GameConfig.SCREEN_WIDTH - paddle.getWidth())
            newX = GameConfig.SCREEN_WIDTH - paddle.getWidth();
        paddle.setX(newX);
    }

    private void resetRound() {
        ball = new Ball(350, 530, GameConfig.BALL_WIDTH, GameConfig.BALL_HEIGHT, GameConfig.BALL_SPEED, 0f, -1f);
        ballAttachedToPaddle = true;
    }


    public GameState getCurrentGameState() { return currentGameState; }
    public void setCurrentGameState(GameState state) { this.currentGameState = state; }

    public Ball getBall() { return ball; }
    public Paddle getPaddle() { return paddle; }
    public List<Brick> getBricks() { return this.mapBrick.getMapBricks(); }
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public boolean getPlayerWin() { return playerWin; }
    public boolean isBallAttachedToPaddle() { return ballAttachedToPaddle; }
}
