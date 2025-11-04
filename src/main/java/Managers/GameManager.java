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

import static java.lang.Math.*;

public class GameManager {

    private List<Ball> balls = new ArrayList<>();
    private List<PowerUp> powerUps = new ArrayList<>();

    private Paddle paddle;
    private MapGame mapBrick;
    private int score;
    private int lives;
    private GameState currentGameState;
    private boolean playerWin;
    private boolean ballAttachedToPaddle = true;
    private int currentLevel = 1;
    private boolean levelCompleted = false;

    public GameManager() throws Exception {
        initGame();
    }

    /**
     * Thiết lập lại trò chơi về trạng thái ban đầu
     */
    public void initGame() throws Exception {
        paddle = new Paddle(GameConfig.SCREEN_X + 280, 700, GameConfig.PADDLE_WIDTH, GameConfig.PADDLE_HEIGHT);
        float ballX = paddle.getX() + paddle.getWidth() / 2 - GameConfig.BALL_WIDTH / 2;
        float ballY = paddle.getY() - GameConfig.BALL_HEIGHT;
        Ball mainBall = new Ball(ballX, ballY, GameConfig.BALL_WIDTH, GameConfig.BALL_HEIGHT,
                GameConfig.BALL_SPEED, 0f, -1f);
        balls.clear();
        balls.add(mainBall);

        mapBrick = new MapGame(); // Khởi tạo đối tượng MapGame
        mapBrick.setCurrentLevel(currentLevel); // Thiết lập level hiện tại
        mapBrick.createMapBricks(); // Tạo các viên gạch cho level hiện tại

        score = 0;
        lives = 3;
        playerWin = false;
        currentGameState = GameState.Menu;
        ballAttachedToPaddle = true;
        powerUps.clear();
        levelCompleted = false;
    }

    public void startLevel(int level) throws Exception {
        currentLevel = level;
        levelCompleted = false;
        mapBrick.setCurrentLevel(level);
        mapBrick.createMapBricks(); // load map
        if (mapBrick.getMapBricks().isEmpty()) {
            System.err.println("Không load được map, game sẽ không bắt đầu!");
            return;
        }

        balls.clear();
        float ballX = paddle.getX() + paddle.getWidth() / 2 - GameConfig.BALL_WIDTH / 2;
        float ballY = paddle.getY() - GameConfig.BALL_HEIGHT;
        balls.add(new Ball(ballX, ballY, GameConfig.BALL_WIDTH, GameConfig.BALL_HEIGHT,
                GameConfig.BALL_SPEED, 0f, -1f));
        ballAttachedToPaddle = true;
        currentGameState = GameState.Playing;
        playerWin = false;
    }

    /**
     * Cập nhật logic của game mỗi frame.
     */
    public void update() {
        if (currentGameState != GameState.Playing) {
            return;
        }
        paddle.update();

        // Cập nhật bóng
        if (ballAttachedToPaddle) {
            Ball b = balls.getFirst();
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
                }
                b.updateFireBallStatus();
            }
            if (brick.isDestroyed()) {
                score += 10;
                if (Math.random() < 0.5) {
                    PowerUp.Type type = PowerUp.Type.values()[(int) (Math.random() * PowerUp.Type.values().length)];
                    powerUps.add(new PowerUp(type,
                            brick.getX() + brick.getWidth() / 2 - 16,
                            brick.getY() + brick.getHeight() / 2));
                }
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
            if (!p.isActive()) {
                pIt.remove();
            }
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
            balls.clear();
            if (lives <= 0) {
                playerWin = false;
                currentGameState = GameState.GameOver;
            } else {
                resetRound();
            }
        }

        // --- Kiểm tra thắng game ---
        if (mapBrick.getMapBricks().isEmpty() && !levelCompleted) {
            try {
                handleLevelComplete(); // xử lý qua level
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleLevelComplete() throws Exception {
        levelCompleted = true;
        System.out.println("Hoàn thành level " + currentLevel);

        if (currentLevel < 7) {
            currentLevel++;
            powerUps.clear();
            startLevel(currentLevel);
        } else {
            System.out.println("Bạn đã thắng toàn bộ 10 màn!");
            playerWin = true;
            currentGameState = GameState.GameOver;
        }
    }

    public void handleMenuSelection(String itemString) throws Exception {
        if (currentGameState == GameState.Menu) {
            switch (itemString) {
                case "START" -> {
                    initGame();
                    currentGameState = GameState.Playing;
                    resetRound();
                }
                case "OPTIONS" ->
                    currentGameState = GameState.Option; // Chuyển đến menu tạm dừng
                case "EXIT" -> {
                }
            }
        } else if (currentGameState == GameState.Option) { // Pause Menu
            switch (itemString) {
                case "RESUME" ->
                    currentGameState = GameState.Playing;
                case "RESTART" -> {
                    initGame(); // Reset    
                    currentGameState = GameState.Playing;
                    resetRound();
                }
                case "BACK TO MENU" ->
                    initGame(); // Reset toàn bộ game
            }
        } else if (currentGameState == GameState.Setting) { // Setting Menu
            switch (itemString) {
                case "SOUND" -> {
                    System.out.println("Đang code...");
                }
                case "LEVELS" -> {

                }
                case "BACK TO MENU" ->
                    currentGameState = GameState.Menu; // Reset toàn bộ game
            }
        }
    }

    public void releaseBall() {
        if (ballAttachedToPaddle) {
            ballAttachedToPaddle = false;
        }
    }

    /**
     * Cập nhật vị trí paddle theo chuột
     */
    public void updatePaddlePosition(double mouseX) {
        float newX = (float) (mouseX - paddle.getWidth() / 2);
        if (newX < GameConfig.SCREEN_X) {
            newX = GameConfig.SCREEN_X;
        }
        if (newX > GameConfig.SCREEN_X + GameConfig.SCREEN_PLAY_WIDTH - paddle.getWidth()) {
            newX = GameConfig.SCREEN_X + GameConfig.SCREEN_PLAY_WIDTH - paddle.getWidth();
        }
        paddle.setdX(newX - paddle.getX());
        paddle.setX(newX);
    }

    private void resetRound() {
        balls.clear();
        powerUps.clear();
        float ballX = paddle.getX() + paddle.getWidth() / 2 - GameConfig.BALL_WIDTH / 2;
        float ballY = paddle.getY() - GameConfig.BALL_HEIGHT;
        balls.add(new Ball(ballX, ballY, GameConfig.BALL_WIDTH, GameConfig.BALL_HEIGHT,
                GameConfig.BALL_SPEED, 0f, -1f));
        ballAttachedToPaddle = true;
    }

    /**
     * Kích hoạt hiệu ứng Power-Up
     */
    private void activatePowerUp(PowerUp.Type type) {
        switch (type) {
            case EXPAND_PADDLE:
                paddle.setWidth(min(paddle.getWidth() + 30, 180));
                break;

            case SHRINK_PADDLE:
                paddle.setWidth(max(paddle.getWidth() - 30, 80));
                break;

            case MULTI_BALL:
                if (!balls.isEmpty() && balls.size() < 8) {
                    List<Ball> newBalls = new ArrayList<>();
                    for (Ball baseBall : balls) {
                        float baseX = baseBall.getX();
                        float baseY = baseBall.getY();
                        float speed = baseBall.getSpeed();
                        float dirX = baseBall.getdX();
                        float dirY = baseBall.getdY();
                        //double angle = Math.atan2(dirY, dirX);
                        Ball left = new Ball(baseX, baseY, baseBall.getWidth(), baseBall.getHeight(),
                                speed, (float) Math.cos(150), (float) Math.sin(150));

                        Ball right = new Ball(baseX, baseY, baseBall.getWidth(), baseBall.getHeight(),
                                speed, (float) Math.cos(30), (float) Math.sin(30));
                        newBalls.add(left);
                        newBalls.add(right);
                    }
                    balls.addAll(newBalls);
                }

                break;
            case LIVE:
                lives = min(lives + 1, 3);
                break;

            case SCORE_1:
                score += 10;
                break;

            case SCORE_2:
                score += 20;
                break;

            case SCORE_3:
                score += 50;
                break;

            case BALL_FIRE:
                for (Ball b : balls) {
                    b.activateFireBall(1000);
                }
                break;

        }
    }

    // --- Getter and Setter ---
    public GameState getCurrentGameState() {
        return currentGameState;
    }

    public void setCurrentGameState(GameState state) {
        this.currentGameState = state;
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public List<Brick> getBricks() {
        return this.mapBrick.getMapBricks();
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public boolean getPlayerWin() {
        return playerWin;
    }

    public boolean isBallAttachedToPaddle() {
        return ballAttachedToPaddle;
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public void setCurrentLevel(int level) {
        currentLevel = level;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}
