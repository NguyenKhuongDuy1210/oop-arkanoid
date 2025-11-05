package items;

import BaseObject.GameObject;
import Managers.GameConfig.GameConfig;

public class Ball extends GameObject {
    private float speed;
    private float dX, dY;
    private boolean isFireBall = false;
    private long fireBallEndTime = 0;
    public Ball(float x, float y, int width, int height, float speed, float dX, float dY) {
        super(x, y, width, height);
        this.speed = speed;
        this.dX = dX;
        this.dY = dY;
        normalizeDirection();
    }

    private void normalizeDirection() {
        float len = (float) Math.sqrt(dX * dX + dY * dY);
        if (len != 0) {
            dX /= len;
            dY /= len;
        }
    }

    public void update(Paddle paddle) {
        x += dX * speed;
        y += dY * speed;

        // Va chạm tường
        if (x <= GameConfig.SCREEN_X) {
            x = GameConfig.SCREEN_X;
            dX = Math.abs(dX);
        }
        if (x + width >= GameConfig.SCREEN_X + GameConfig.SCREEN_PLAY_WIDTH) {
            x = GameConfig.SCREEN_X + GameConfig.SCREEN_PLAY_WIDTH - width;
            dX = -Math.abs(dX);
        }
        if (y <= 0) {
            y = 0;
            dY = Math.abs(dY);
        }

        // Va chạm paddle
        if (y + height >= paddle.getY() &&
                y + height <= paddle.getY() + paddle.getHeight() &&
                x + width >= paddle.getX() &&
                x <= paddle.getX() + paddle.getWidth()) {

            // Tính vị trí chạm trên paddle (0.0 = trái, 1.0 = phải)
            float hitPos = (x + width / 2 - paddle.getX()) / paddle.getWidth();

            // Góc phản xạ: lệch từ -60° đến +60° tùy vị trí chạm
            float angle = (float) Math.toRadians(-60 + hitPos * 120);

            dX = (float) Math.sin(angle);
            dY = (float) -Math.cos(angle);

            normalizeDirection();
            y = paddle.getY() - height - 1;
        }
    }

    public boolean checkCollision(Brick brick) {
        float margin = 1.5f;
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

            if (!isFireBall) {
                float overlapLeft = ballRight - brickLeft;
                float overlapRight = brickRight - ballLeft;
                float overlapTop = ballBottom - brickTop;
                float overlapBottom = brickBottom - ballTop;

                float minOverlapX = Math.min(overlapLeft, overlapRight);
                float minOverlapY = Math.min(overlapTop, overlapBottom);

                if (minOverlapX < minOverlapY) {
                    if (overlapLeft < overlapRight) x -= minOverlapX;
                    else x += minOverlapX;
                    dX = -dX;
                } else {
                    if (overlapTop < overlapBottom) y -= minOverlapY;
                    else y += minOverlapY;
                    dY = -dY;
                }
                normalizeDirection();
            }
            return true;
        }
        return false;
    }


    @Override
    public void update() {
        x += dX * speed;
        y += dY * speed;

        if (x <= 0) { x = 0; dX = Math.abs(dX); }
        if (x + width >= GameConfig.SCREEN_X + GameConfig.SCREEN_PLAY_WIDTH) {
            x = GameConfig.SCREEN_X + GameConfig.SCREEN_PLAY_WIDTH - width;
            dX = -Math.abs(dX);
        }
        if (y <= 0) { y = 0; dY = Math.abs(dY); }

        normalizeDirection();
    }
    public void activateFireBall(long durationMillis) {
        isFireBall = true;
        fireBallEndTime = System.currentTimeMillis() + durationMillis;
    }

    public void updateFireBallStatus() {
        if (isFireBall && System.currentTimeMillis() > fireBallEndTime) {
            isFireBall = false;
        }
    }

    public boolean isFireBall() {
        return isFireBall;
    }
    public float getdX() { return dX; }
    public void setdX(float dX) { this.dX = dX; normalizeDirection(); }

    public float getdY() { return dY; }
    public void setdY(float dY) { this.dY = dY; normalizeDirection(); }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }
    public void setFireBall(boolean fireBall) {
        this.isFireBall = fireBall;
    }

}
