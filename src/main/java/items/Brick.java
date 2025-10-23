package items;

import BaseObject.GameObject;
import Managers.GameConfig.GameConfig;
import items.effects.Animation;

import java.awt.*;

import static com.sun.glass.ui.Cursor.setVisible;

public class Brick extends GameObject {
    // animation brick
    private Animation brick_animation;
    private int frameIndex = 0;
    private long lastFrameTime = 0;
    private long frameDelay = 100_000_00 ;

    private int hitPoints;
    private boolean onHit = false;   // đếm frame animation
    private boolean destroyed = false;

    public Brick(float x, float y, int width, int height, int hitPoints) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.brick_animation = new Animation();
        brick_animation.createFrame_clips(10, GameConfig.BRICK_WIDTH, GameConfig.BRICK_HEIGHT);
    }

    @Override
    public void update() {
        if (onHit && !destroyed) {
            long currentTime = System.nanoTime();
            if (currentTime - lastFrameTime >= frameDelay) {
                frameIndex++;
                lastFrameTime = currentTime;
                if (frameIndex >= brick_animation.getFrame_clips().length) {
                    destroyed = true;
                    visible = false;
                }
            }
        }
    }

    public boolean isDestroyed() { return destroyed; }
    public void setOnHit(boolean onHit) { this.onHit = onHit; }
    public boolean isOnHit() { return onHit; }
    public int getFrameIndex() {
        return frameIndex;
    }
    public int[] getCurrentClip() {
        return brick_animation.getFrame_clips()[frameIndex];
    }

}
