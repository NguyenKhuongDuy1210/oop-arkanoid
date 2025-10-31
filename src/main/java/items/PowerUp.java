package items;

import Managers.GameConfig.GameConfig;

public class PowerUp {
    public enum Type { EXPAND_PADDLE,
        SHRINK_PADDLE,
        EXTRA_LIFE,
        MULTI_BALL
    }

    private Type type;
    private double x, y;
    private double speed = 3;
    private double width = 32, height = 32;
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

    public void render(javafx.scene.canvas.GraphicsContext gc) {
        gc.setFill(javafx.scene.paint.Color.GOLD);
        gc.fillOval(x, y, width, height);
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
