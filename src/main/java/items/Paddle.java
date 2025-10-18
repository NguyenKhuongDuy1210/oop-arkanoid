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
        if(x<0)
            x=0;
        if(x>800-width)
            x=800-width;
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

    }

}
