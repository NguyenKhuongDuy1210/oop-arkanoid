package Managers;

import Managers.GameConfig.GameConfig;
import Managers.MapManager.MapGame;
import Managers.MenuManager.GameState;
import items.Ball;
import items.Brick;
import items.Paddle;
import items.PowerUp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameManager {

    private List<Ball> balls = new ArrayList<>();
    private Paddle paddle;
    private MapGame mapBrick;
    private int score;
    private int lives;
    private GameState currentGameState;
    private boolean playerWin;
    private boolean ballAttachedToPaddle = true;
    private List<PowerUp> powerUps = new ArrayList<>();

    public GameManager() throws Exception {
        initGame();
    }

    /** Thiết lập lại trò chơi về trạng thái ban đầu */
    public void initGame() throws Exception {
        paddle = new Paddle(280, 600, GameConfig.PADDLE_WIDTH, GameConfig.PADDLE_HEIGHT);
        float ballX = paddle.getX() + paddle.getWidth() / 2 - GameConfig.BALL_WIDTH / 2;
        float ballY = paddle.getY() - GameConfig.BALL_HEIGHT;
        Ball mainBall = new Ball(ballX, ballY, GameConfig.BALL_WIDTH, GameConfig.BALL_HEIGHT,
                GameConfig.BALL_SPEED, 0f, -1f);
        balls.clear();
        balls.add(mainBall);
        mapBrick = new MapGame();
        mapBrick.createMapBricks();
        score = 0;
        lives = 3;
        playerWin = false;
        currentGameState = GameState.Menu;
        ballAttachedToPaddle = true;
    }

    /** Cập nhật logic của game mỗi frame. */
    public void update() {
        if (currentGameState != GameState.Playing) return;
        paddle.update();

        // Cập nhật bóng
        if (ballAttachedToPaddle) {
            Ball b = balls.get(0);
            b.setX(paddle.getX() + paddle.getWidth() / 2 - b.getWidth() / 2);
            b.setY(paddle.getY() - b.getHeight());
        } else {
            for (Ball b : balls) {
                b.update(paddle);
            }
        }

        // --- Va chạm với gạch ---
        Iterator<Brick> it = mapBrick.getMapBricks().iterator();
        while (it.hasNext()) {
            Brick brick = it.next();
            brick.update();

            for (Ball b : balls) {
                if (b.checkCollision(brick) && !brick.isDestroyed()) {
                    if (brick.gethitPoints() > 0) {
                        brick.sethitPoints(brick.gethitPoints() - 1);
                        brick.setOnHit(true);
                    }

                    // Nếu máu còn lại sau khi trừ = 0 → viên gạch bị phá hủy
                    if (brick.gethitPoints() == 0) {
                        // Random tạo power-up
                        if (Math.random() < 0.2) { // 20% tỉ lệ
                            PowerUp.Type type = PowerUp.Type.values()[(int)(Math.random() * PowerUp.Type.values().length)];
                            powerUps.add(new PowerUp(type,
                                    brick.getX() + brick.getWidth()/2 - 16,
                                    brick.getY() + brick.getHeight()/2));
                        }
                    }
                }
            }
            if (brick.isDestroyed()) {
                score += 10;
                it.remove();
            }
        }

        Iterator<PowerUp> pIt = powerUps.iterator();
        while (pIt.hasNext()) {
            PowerUp p = pIt.next();
            p.update();

            if (p.checkCollision(paddle)) {
                activatePowerUp(p.getType());
                pIt.remove();
                continue;
            }
            if (!p.isActive()) pIt.remove();
        }

        Iterator<Ball> ballIt = balls.iterator();
        while (ballIt.hasNext()) {
            Ball b = ballIt.next();
            if (b.getY() >= 750) {
                ballIt.remove();
            }
        }

        if (balls.isEmpty()) {
            lives--;
            if (lives <= 0) {
                playerWin = false;
                currentGameState = GameState.GameOver;
            } else {
                resetRound();
            }
        }

        // --- Kiểm tra thắng game ---
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
        paddle.setdX(newX - paddle.getX());
        paddle.setX(newX);
    }

    private void resetRound() {
        float ballX = paddle.getX() + paddle.getWidth() / 2 - GameConfig.BALL_WIDTH / 2;
        float ballY = paddle.getY() - GameConfig.BALL_HEIGHT;
        Ball mainBall = new Ball(ballX, ballY, GameConfig.BALL_WIDTH, GameConfig.BALL_HEIGHT,
                GameConfig.BALL_SPEED, 0f, -1f);
        balls.clear();
        balls.add(mainBall);
        ballAttachedToPaddle = true;
        powerUps.clear();

    }

    /** Kích hoạt hiệu ứng Power-Up */
    private void activatePowerUp(PowerUp.Type type) {
        switch (type) {
            case EXPAND_PADDLE:
                paddle.setWidth(Math.min(paddle.getWidth() + 30, 180));
                break;

            case SHRINK_PADDLE:
                paddle.setWidth(Math.max(paddle.getWidth() - 30, 80));
                break;

            case MULTI_BALL:
                if (balls.size() < 3) {
                    List<Ball> newBalls = new ArrayList<>();
                    for (Ball b : balls) {
                        Ball left = new Ball(b.getX(), b.getY(), b.getWidth(), b.getHeight(),
                                b.getSpeed(), -Math.abs(b.getdX()), -Math.abs(b.getdY()));
                        Ball right = new Ball(b.getX(), b.getY(), b.getWidth(), b.getHeight(),
                                b.getSpeed(), Math.abs(b.getdX()), -Math.abs(b.getdY()));
                        newBalls.add(left);
                        newBalls.add(right);
                    }
                    balls.addAll(newBalls);
                }
                break;
        }
    }

    // --- Getter ---
    public GameState getCurrentGameState() { return currentGameState; }
    public void setCurrentGameState(GameState state) { this.currentGameState = state; }
    public List<Ball> getBalls() { return balls; }
    public Paddle getPaddle() { return paddle; }
    public List<Brick> getBricks() { return this.mapBrick.getMapBricks(); }
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public boolean getPlayerWin() { return playerWin; }
    public boolean isBallAttachedToPaddle() { return ballAttachedToPaddle; }
    public List<PowerUp> getPowerUps() { return powerUps; }
}
