package items;

import BaseObject.GameObject;
import Managers.GameConfig.GameConfig;
import items.effects.Animation;
import items.factory.MovingBrick;
import javafx.scene.image.Image;


public class Brick extends GameObject {
    // animation brick
    private Animation brick_animation;
    private int frameIndex = 0;
    private long lastFrameTime = 0;
    private long frameDelay = 100_000_00 ;

    private Image brickImg;
    private int hitPoints;
    private boolean onHit = false;   // đếm frame animation
    private boolean destroyed = false;

    public Brick(Image brickImg,float x, float y, int width, int height, int hitPoints) {
        super(x, y, width, height);
        this.brickImg = brickImg;
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
                // Khi animation chạy hết -> reset hoặc destroy

                if (frameIndex >= brick_animation.getFrame_clips().length) {
                    setDestroyed(true);
                }
                if (hitPoints > 0) {
                    setOnHit(false);
                }
            }
        }
    }

    public Image getBrickImg() {
        return brickImg;
    }
    public boolean isDestroyed() { return destroyed; }
    public void setDestroyed(boolean destroyed) { this.destroyed = destroyed; }
    public void setOnHit(boolean onHit) { this.onHit = onHit; }
    public boolean isOnHit() { return onHit; }
    public void sethitPoints(int hitPoints) { this.hitPoints = hitPoints; }
    public int gethitPoints() { return hitPoints; }
    public int getFrameIndex() {
        return frameIndex;
    }
    public int[] getCurrentClip() {
        return brick_animation.getFrame_clips()[getFrameIndex()];
    }

}
