package items;

import BaseObject.MovableObject;

public class Paddle extends MovableObject {
    private float speed;
    private String currentPowerUp;

    public Paddle(float x, float y, int width, int height, float speed) {
        super(x, y, width, height, 0, 0);
        this.speed = speed;
    }

    @Override
    public void move() {
        x += dx;
        y += dy;
    }

    public void moveLeft() {
        dx = -speed;
        move();
        dx = 0;
    }

    public void moveRight() {
        dx = speed;
        move();
        dx = 0;
    }

    public void applyPowerUp(String powerUp) {
        this.currentPowerUp = powerUp;
    }

    @Override
    public void update() {
        // có thể xử lý logic paddle ở đây
    }

    @Override
    public void render(java.awt.Graphics g) {
        g.fillRect((int)x, (int)y, width, height);
    }
}
