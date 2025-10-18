package items;

import BaseObject.GameObject;
import BaseObject.MovableObject;
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

    public void checkCollision(GameObject other) {
        Rectangle ballRect = new Rectangle((int)x, (int)y, width, height);
        Rectangle otherRect = new Rectangle((int)other.getX(), (int)other.getY(), other.getWidth(), other.getHeight());

        if (ballRect.intersects(otherRect)) {

            // Tính hướng va chạm
            float overlapLeft   = (x + width) - other.getX();
            float overlapRight  = (other.getX() + other.getWidth()) - x;
            float overlapTop    = (y + height) - other.getY();
            float overlapBottom = (other.getY() + other.getHeight()) - y;

            boolean ballFromLeft   = overlapLeft < overlapRight;
            boolean ballFromTop    = overlapTop < overlapBottom;

            float minOverlapX = ballFromLeft ? overlapLeft : overlapRight;
            float minOverlapY = ballFromTop  ? overlapTop  : overlapBottom;


            if (minOverlapX < minOverlapY) {
                bounceX();
            }
            else {
                bounceY();
            }

            if (other instanceof Brick brick) {
                brick.takeHit();
            }
        }
    }


    @Override
    public void update() {
        move();

        // ⚡ Va chạm biên màn hình (ví dụ: 800x600)
        if (x <= 0 || x + width >= 800) bounceX();
        if (y <= 0) bounceY();

        // ⚠ Nếu rơi ra ngoài màn hình, ẩn bóng
        if (y > 600) setVisible(false);
    }

    @Override
    public void render(Graphics g) {
        if (!isVisible()) return;

        g.setColor(Color.WHITE);
        g.fillOval((int)x, (int)y, width, height);
        g.setColor(Color.GRAY);
        g.drawOval((int)x, (int)y, width, height);
    }
}
