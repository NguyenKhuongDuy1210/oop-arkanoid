package items;

import BaseObject.GameObject;
import java.awt.*;

import static com.sun.glass.ui.Cursor.setVisible;

public class Brick extends GameObject {
    private int hitPoints;
    private int frameIndex = 0; // 0 = bình thường
    private int hitTimer = 0;   // đếm frame animation
    private boolean destroyed = false;
    public Brick(float x, float y, int width, int height, int hitPoints) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
    }

    public void takeHit() {
        if (hitTimer == 0) hitTimer = 1; // bắt đầu animation
    }

    @Override
    public void update() {
        if(hitTimer > 0) {
            hitTimer++;
            frameIndex++;
            if(frameIndex >= 3) destroyed = true; // frame cuối → xóa brick
        }
    }

    public int getFrameIndex() { return frameIndex; }
    public boolean isDestroyed() { return destroyed; }
    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isVisible() { return visible; }

}
