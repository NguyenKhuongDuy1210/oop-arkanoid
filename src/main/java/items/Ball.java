package items;

import BaseObject.GameObject;
import Managers.GameConfig.GameConfig;

public class Ball extends GameObject {
    private float speed;
    private float dX, dY;

    public Ball(float x, float y, int width, int height, float speed, float dX, float dY) {
        super(x, y, width, height);
        this.speed = speed;
        this.dX = dX;
        this.dY = dY;
    }

    public void update(Paddle paddle) {
        x += dX * speed;
        y += dY * speed;

        // Va chạm tường
        if (x <= 0) { x = 0; dX = Math.abs(dX); }
        if (x + width >= GameConfig.SCREEN_WIDTH) { x = GameConfig.SCREEN_WIDTH - width; dX = -Math.abs(dX); }
        if (y <= 0) { y = 0; dY = Math.abs(dY); }

        // Va chạm paddle
        if (y + height >= paddle.getY() &&
                y + height <= paddle.getY() + paddle.getHeight() &&
                x + width >= paddle.getX() &&
                x <= paddle.getX() + paddle.getWidth()) {

            dY = -Math.abs(dY);
            double hitPos = (x + width/2 - paddle.getX()) / paddle.getWidth();
            dX = (float)(hitPos - 0.5f);
            y = paddle.getY() - height - 1;
        }
    }

    public boolean checkCollision(Brick brick) {
        float margin = 2.0f;
        float ballLeft = x + margin;
        float ballRight = x + width - margin;
        float ballTop = y + margin;
        float ballBottom = y + height - margin;

        float brickLeft = brick.getX();
        float brickRight = brick.getX() + brick.getWidth();
        float brickTop = brick.getY();
        float brickBottom = brick.getY() + brick.getHeight();

        if (ballRight > brickLeft && ballLeft < brickRight &&
                ballBottom > brickTop && ballTop < brickBottom) {

            // Tính hướng va chạm dựa trên khoảng overlap
            float overlapLeft = ballRight - brickLeft;
            float overlapRight = brickRight - ballLeft;
            float overlapTop = ballBottom - brickTop;
            float overlapBottom = brickBottom - ballTop;

            boolean ballFromLeft = overlapLeft < overlapRight;
            boolean ballFromTop = overlapTop < overlapBottom;

            float minOverlapX = ballFromLeft ? overlapLeft : overlapRight;
            float minOverlapY = ballFromTop ? overlapTop : overlapBottom;

            if (minOverlapX < minOverlapY) dX = -dX;
            else dY = -dY;
            brick.sethitPoints(brick.gethitPoints() - 1);
            brick.setOnHit(true);
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        x += dX * speed;
        y += dY * speed;

        if (x <= 0) { x = 0; dX = Math.abs(dX); }
        if (x + width >= GameConfig.SCREEN_WIDTH) { x = GameConfig.SCREEN_WIDTH - width; dX = -Math.abs(dX); }
        if (y <= 0) { y = 0; dY = Math.abs(dY); }
    }

    public float getdX()
    {
        return dX;
    }
    public void setdX(float dX)
    {
        this.dX=dX;
    }
    public float getdY()
    {
        return dY;
    }
    public void setdY(float dY)
    {
        this.dY=dY;
    }
    public float getSpeed()
    {
        return speed;
    }
}
