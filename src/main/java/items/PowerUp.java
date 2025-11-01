package items;

import Managers.GameConfig.GameConfig;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class PowerUp {
    public enum Type { EXPAND_PADDLE,
        SHRINK_PADDLE,
        MULTI_BALL,
        LIVE,
        BALL_FIRE,
        SCORE_1,
        SCORE_2,
        SCORE_3
    }
    private static final Map<Type, Image> powerUpImages = new HashMap<>();

    static {
        powerUpImages.put(Type.MULTI_BALL, new Image("file:assets/power_up/ball_add.png"));
        powerUpImages.put(Type.BALL_FIRE, new Image("file:assets/power_up/ball_fire.png"));
        powerUpImages.put(Type.SHRINK_PADDLE, new Image("file:assets/power_up/paddle_small.png"));
        powerUpImages.put(Type.EXPAND_PADDLE, new Image("file:assets/power_up/paddle_large.png"));
        powerUpImages.put(Type.LIVE, new Image("file:assets/power_up/live.png"));
        powerUpImages.put(Type.SCORE_1, new Image("file:assets/power_up/score1.png"));
        powerUpImages.put(Type.SCORE_2, new Image("file:assets/power_up/score2.png"));
        powerUpImages.put(Type.SCORE_3, new Image("file:assets/power_up/score3.png"));
    }

    private Type type;
    private double x, y;
    private double speed = 3;
    private double width = 40, height = 16;
    private boolean active = true;

    public PowerUp(Type type, double x, double y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public void update() {
        y += speed;
        if (y > GameConfig.SCREEN_HEIGHT) active = false; // rơi khỏi màn
    }

    public boolean checkCollision(Paddle paddle) {
        return x < paddle.getX() + paddle.getWidth() &&
                x + width > paddle.getX() &&
                y < paddle.getY() + paddle.getHeight() &&
                y + height > paddle.getY();
    }

    public void render(GraphicsContext gc) {
        Image img = powerUpImages.get(type);
        if (img != null) {
            gc.drawImage(img, x, y, width, height);
        }
    }


    // getter / setter
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public Type getType() { return type; }
}
