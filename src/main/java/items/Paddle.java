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
        if(x<0)
            x=0;
        if(x> GameConfig.SCREEN_WIDTH - width)
            x = GameConfig.SCREEN_WIDTH - width;
    }
    public void applyPowerUp(String powerUp) {
        this.currentPowerUp = powerUp;
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

}
