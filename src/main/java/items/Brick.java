package items;

import BaseObject.GameObject;
import java.awt.*;

import static com.sun.glass.ui.Cursor.setVisible;

public class Brick extends GameObject {
    private int hitPoints;
    private boolean Visible;
    //private Image ImageBrick;
    public Brick(float x, float y, int width, int height, int hitPoints) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        //this.ImageBrick=ImageBrick;
    }

    public void takeHit() {
        hitPoints--;
        if (hitPoints <= 0) {
            setVisible(false); // ẩn khi bị phá
        }
    }

    public boolean isDestroyed() {
        return hitPoints <= 0;
    }

    @Override
    public void update() {
        // Brick không di chuyển, nhưng có thể xử lý hiệu ứng vỡ trong tương lai
    }

    @Override
    public void render(Graphics g) {
        if (!isVisible()) return;

        // Màu thay đổi theo độ bền
        if (hitPoints == 3) g.setColor(Color.RED);
        else if (hitPoints == 2) g.setColor(Color.ORANGE);
        else g.setColor(Color.YELLOW);

        g.fillRect((int) x, (int) y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect((int) x, (int) y, width, height);
    }
    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isVisible() { return visible; }

}
