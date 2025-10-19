package items;

import BaseObject.GameObject;
import java.awt.*;

import static com.sun.glass.ui.Cursor.setVisible;

public class Brick extends GameObject {
    private int frameIndex = 0;
    private long lastFrameTime = 0;
    private long frameDelay = 100_000_00 ; // 100ms

    private int hitPoints;
    private int[][] frame_clips = new int[10][4];
    private boolean onHit = false;   // đếm frame animation
    private boolean destroyed = false;
    public Brick(float x, float y, int width, int height, int hitPoints) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.frame_clips = createFrame_clips();
    }

    public int[][] createFrame_clips() {
        int width_frame = 60;
        int height_frame = 30;

        for (int i = 0; i < 10; i++) {
            frame_clips[i][0] = i * width_frame;
            frame_clips[i][1] = 0;
            frame_clips[i][2] = width_frame;
            frame_clips[i][3] = height_frame;
        }
        return frame_clips;
    }

    @Override
    public void update() {
        if (onHit && !destroyed) {
            long currentTime = System.nanoTime();
            if (currentTime - lastFrameTime >= frameDelay) {
                frameIndex++;
                lastFrameTime = currentTime;
                if (frameIndex >= frame_clips.length) {
                    destroyed = true;
                    visible = false;
                }
            }
        }
    }

    public boolean isDestroyed() { return destroyed; }
    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isVisible() { return visible; }
    public void setOnHit(boolean onHit) { this.onHit = onHit; }
    public boolean isOnHit() { return onHit; }
    public int getFrameIndex() {
        return frameIndex;
    }
    public int[] getCurrentClip() {
        return frame_clips[frameIndex];
    }

}
