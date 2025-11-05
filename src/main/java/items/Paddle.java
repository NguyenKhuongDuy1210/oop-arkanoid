package items;

import BaseObject.GameObject;
import Managers.GameConfig.GameConfig;

public class Paddle extends GameObject {
    private String currentPowerUp;
    private float dX;
    public Paddle(float x, float y, int width, int height) {
        super(x, y, width, height);
    }

    public void move() {
        if(x < 0)
            x = 0;
        if(x > GameConfig.SCREEN_X + GameConfig.SCREEN_PLAY_WIDTH - width)
            x = GameConfig.SCREEN_X + GameConfig.SCREEN_PLAY_WIDTH - width;
    }

    public float getdX() {
        return dX;
    }
    public void setdX(float dX)
    {
        this.dX=dX;
    }
    @Override
    public void update() {

    }
    @Override
    public void setWidth(int newWidth) {
        float centerX = x + width / 2f;
        width = newWidth;
        x = centerX - width / 2f;
    }

}
