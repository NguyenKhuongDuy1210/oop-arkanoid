package items;

import BaseObject.GameObject;
import BaseObject.MovableObject;
import Managers.GameConfig.GameConfig;

import java.awt.*;

import static com.sun.glass.ui.Cursor.setVisible;

public class Ball extends MovableObject {
    private float speed;
    private float dX, dY;

    public Ball(float x, float y, int width, int height, float speed, float dX, float dY) {
        // dx, dy là vận tốc thực tế (hướng * tốc độ)
        super(x, y, width, height, dX * speed, dY * speed);
        this.speed = speed;
        this.dX = dX;
        this.dY = dY;
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

    @Override
    public void move() {
        x += dx;
        y += dy;
    }

    public void bounceX() {
        dX = -dX;
        dx = dX * speed;
    }

    public void bounceY() {
        dY = -dY;
        dy = dY * speed;
    }

    public void update(Paddle paddle) {
        // di chuyển bóng
        x += dX * speed;
        y += dY * speed;

        // va chạm tường
        if(x <= 0) { x = 0; dX = -dX; }
        if(x + width >= GameConfig.SCREEN_WIDTH) { x = GameConfig.SCREEN_WIDTH - width; dX = -dX; }
        if(y <= 0) { y = 0; dY = -dY; }

        // va chạm paddle kiểu Breakout
        if(y + height >= paddle.getY() &&
                y + height <= paddle.getY() + paddle.getHeight() &&
                x + width >= paddle.getX() &&
                x <= paddle.getX() + paddle.getWidth()) {

            dY = -Math.abs(dY); // chỉ đảo hướng, giữ nguyên speed
            double hitPos = (x + width/2 - paddle.getX()) / paddle.getWidth(); // trung tâm bóng
            dX = (float)(hitPos - 0.5f); // chỉ đổi hướng, không đổi tốc độ
            y = paddle.getY() - height - 1; // tránh bị dính
        }


    }

    public boolean checkCollision(Brick brick) {
        Rectangle ballRect = new Rectangle((int)x,(int)y,width,height);
        Rectangle brickRect = new Rectangle((int)brick.getX(),(int)brick.getY(),brick.getWidth(),brick.getHeight());

        if(ballRect.intersects(brickRect)) {
            float overlapLeft = (x+width) - brick.getX();
            float overlapRight = (brick.getX()+brick.getWidth()) - x;
            float overlapTop = (y+height) - brick.getY();
            float overlapBottom = (brick.getY()+brick.getHeight()) - y;

            boolean ballFromLeft = overlapLeft < overlapRight;
            boolean ballFromTop = overlapTop < overlapBottom;

            float minOverlapX = ballFromLeft ? overlapLeft : overlapRight;
            float minOverlapY = ballFromTop ? overlapTop : overlapBottom;

            if(minOverlapX < minOverlapY) dX = -dX;
            else dY = -dY;

            brick.setOnHit(true);
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        move();

        // Va chạm tường ngang
        if (x <= 0) {
            x = 0;
            bounceX();
        }
        if (x + width >= GameConfig.SCREEN_WIDTH) {
            x = GameConfig.SCREEN_WIDTH - width;
            bounceX();
        }

        // Va chạm tường trên
        if (y <= 0) {
            y = 0;
            bounceY();
        }
    }

}
