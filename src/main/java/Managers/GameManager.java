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

import Managers.MenuManager.Menu;

public class GameManager {

    private List<Ball> balls = new ArrayList<>();
    private List<PowerUp> powerUps = new ArrayList<>();
    private int[][] listHighScore = new int[8][3];
    private Paddle paddle;
    private MapGame mapBrick;
    private int score;
    private int lives;
    private GameState currentGameState;
    private boolean playerWin;
    private boolean ballAttachedToPaddle = true;
    private int currentLevel = 1;
    private boolean levelCompleted = false;
    private Menu menu;
    private int levelCompleteSelection = 0;

    public GameManager() throws Exception {
        SoundManager.loadSounds();
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
        SoundManager.play("start_game");
    }

    public void startLevel(int level) throws Exception {

        currentLevel = level;
        // score = 0; // giữ nguyên điểm số khi qua level
        paddle.setWidth(GameConfig.PADDLE_WIDTH);
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
            Ball b = balls.get(0);
            b.setX(paddle.getX() + paddle.getWidth() / 2 - b.getWidth() / 2);
            b.setY(paddle.getY() - b.getHeight());
        } else {
            for (Ball b : balls) {
                if (b.checkColisionPaddle(paddle)) {
                    SoundManager.play("hit_brick");
                    continue;
                }
                b.update();
            }
        }
        Iterator<Brick> it = mapBrick.getMapBricks().iterator();
        while (it.hasNext()) {
            Brick brick = it.next();
            brick.update();
            boolean hitPlayed = false;
            for (Ball b : balls) {
                if (b.checkCollision(brick) && !brick.isDestroyed()) {
                    if (brick.gethitPoints() > 0) {
                        if (!hitPlayed) {
                            SoundManager.play("hit_brick");
                            hitPlayed = true;
                        }
                        brick.sethitPoints(brick.gethitPoints() - 1);
                        brick.setOnHit(true);
                    }
                }
                //if()
                b.updateFireBallStatus();
            }
            if (brick.isDestroyed()) {
                score += 10;
                if (Math.random() < 0.5 && powerUps.size() <= 4) {
                    PowerUp.Type type = PowerUp.Type.values()[(int) (Math.random() * PowerUp.Type.values().length)];
                    powerUps.add(new PowerUp(type,
                            brick.getX() + brick.getWidth() / 2 - 16,
                            brick.getY() + brick.getHeight() / 2));
                }
                Iterator<PowerUp> pIt = powerUps.iterator();
                while (pIt.hasNext()) {
                    PowerUp p = pIt.next();
                    if (!p.isActive()) {
                        pIt.remove();
                    }
                }
                it.remove();
            }
        }
        boolean sound = false;
        Iterator<PowerUp> pIt = powerUps.iterator();
        while (pIt.hasNext()) {
            PowerUp p = pIt.next();
            p.update();

            if (p.checkCollision(paddle)) {
                activatePowerUp(p.getType());
                pIt.remove();
                if (!sound) {
                    sound = true;
                    SoundManager.play("bonus");
                }
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
                SoundManager.play("game-over");
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

    public void updateHighScore() {
        if (score >= listHighScore[currentLevel][0]) {
            listHighScore[currentLevel][0] = score;
        } else if (score >= listHighScore[currentLevel][1]) {
            listHighScore[currentLevel][1] = score;
        } else if (score >= listHighScore[currentLevel][2]) {
            listHighScore[currentLevel][2] = score;
        }
    }

    private void handleLevelComplete() throws Exception {
        levelCompleted = true;
        SoundManager.play("level-up");
        System.out.println("Hoàn thành level " + currentLevel);

        if (currentLevel < 7) {

            currentGameState = GameState.LevelComplete;

            if (menu != null) {
                menu.switchToLevelCompleteMenu();
            }

        } else {
            System.out.println("Bạn đã thắng toàn bộ 7 màn!");
            playerWin = true;
            currentGameState = GameState.GameOver;
        }
    }

    public void proceedToNextLevel() throws Exception { // Chuyển đến level tiếp theo
        if (currentLevel < 7) {
            currentLevel++;
            powerUps.clear();
            startLevel(currentLevel);
        }
    }

    public void handleMenuSelection(String itemString) throws Exception {
        if (currentGameState == GameState.Menu) {
            switch (itemString) {
                case "START" -> {
                    currentGameState = GameState.Playing; // Chuyển trạng thái game sang Playing
                }
                case "OPTIONS" ->
                    currentGameState = GameState.Option; // Chuyển đến menu tạm dừng
                case "EXIT" -> { // xử lý trong InputHandler
                }
            }
        } else if (currentGameState == GameState.Option) { // Pause Menu
            switch (itemString) {
                case "RESUME" ->
                    currentGameState = GameState.Playing; // Quay lại chơi game
                case "RESTART" -> {
                    initGame(); // Reset    
                    currentGameState = GameState.Playing; // Bắt đầu chơi lại
                    resetRound();
                }
                case "BACK TO MENU" ->
                    initGame(); // Reset toàn bộ game
            }
        } else if (currentGameState == GameState.Setting) { // Setting Menu
            switch (itemString) {
                case "SOUND" -> {
                    currentGameState = GameState.SoundSetting; // Chuyển đến menu cài đặt âm thanh  
                    menu.switchToSoundSettingsMenu(); // chuyển menu hiển thị
                }
                case "LEVELS" -> {

                }
                case "BACK TO MENU" ->
                    currentGameState = GameState.Menu; // Reset toàn bộ game
            }
        } else if (currentGameState == GameState.SoundSetting) { // Setting Sound Menu
            if (itemString.startsWith("MUSIC:")) { // bật/tắt nhạc nền
                SoundManager.toggleMusic();
            } else if (itemString.startsWith("SFX:")) { // bật/tắt hiệu ứng âm thanh
                SoundManager.toggleSfx();
            } else if (itemString.equals("BACK")) {
                currentGameState = GameState.Setting; // Quay lại menu cài đặt
                if (menu != null) {
                    menu.switchToSettingMenu(); // chuyển menu hiển thị
                }
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
                        double angle = Math.atan2(dirY, dirX);
                        if (dirY > 0) {
                            angle = -angle;
                        }
                        double leftAngle = angle - Math.toRadians(30);
                        double rightAngle = angle + Math.toRadians(30);
                        leftAngle = clampAngle(leftAngle);
                        rightAngle = clampAngle(rightAngle);
                        Ball left = new Ball(baseX, baseY, baseBall.getWidth(), baseBall.getHeight(),
                                speed, (float) Math.cos(leftAngle), (float) Math.sin(leftAngle));
                        Ball right = new Ball(baseX, baseY, baseBall.getWidth(), baseBall.getHeight(),
                                speed, (float) Math.cos(rightAngle), (float) Math.sin(rightAngle));
                        left.setFireBall(baseBall.isFireBall());
                        right.setFireBall(baseBall.isFireBall());
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

    private double clampAngle(double angle) {
        angle = (angle + Math.PI) % (2 * Math.PI) - Math.PI;

        double minVertical = Math.toRadians(15);
        double maxVertical = Math.toRadians(165);
        if (Math.abs(angle) < minVertical) {
            angle = Math.copySign(minVertical, angle);
        }
        if (Math.abs(angle) > maxVertical) {
            angle = Math.copySign(maxVertical, angle);
        }
        return angle;
    }

    public void changeMusicVolume(boolean increase) { // thay đổi âm lượng nhạc nền
        double currentVolume = SoundManager.getMusicVolume(); // Lấy âm lượng hiện tại

        double newVolume = increase ? currentVolume + 0.1 : currentVolume - 0.1; // Tăng hoặc giảm âm lượng

        SoundManager.setMusicVolume(newVolume); // Cập nhật âm lượng nhạc nền
    }

    public void changeSfxVolume(boolean increase) { // thay đổi âm lượng hiệu ứng âm thanh
        double currentVolume = SoundManager.getSfxVolume(); // Lấy âm lượng hiện tại

        double newVolume = increase ? currentVolume + 0.1 : currentVolume - 0.1;

        SoundManager.setSfxVolume(newVolume); // Cập nhật âm lượng hiệu ứng âm thanh
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

    public int[] getListHighScore() {
        return listHighScore[currentLevel];
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public int getLevelCompleteSelection() {
        return levelCompleteSelection;
    }
}
