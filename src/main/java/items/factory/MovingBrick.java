package items.factory;

import Managers.GameConfig.GameConfig;
import items.Brick;
import javafx.scene.image.Image;

public class MovingBrick extends Brick implements FactoryBrick {
    private int dx = 1;
    //private
    public MovingBrick(Image brickImg, float x, float y, int width, int height, int hitPoints) {
        super(brickImg, x, y, width, height, hitPoints);
    }

    public MovingBrick() {
        super(new Image("file:assets/bricks/brick3.png"), 0, 0, GameConfig.BRICK_WIDTH, GameConfig.BRICK_HEIGHT, 3);
    }

    @Override
    public void update() {
        super.update();
        setX(getX() + dx);
        if (getX() <= GameConfig.SCREEN_X || getX() + getWidth() >= GameConfig.SCREEN_X + GameConfig.SCREEN_PLAY_WIDTH + 1) {
            dx *= -1;
        }
    }

    @Override
    public Brick createBrick(int x, int y) {
        Image brickImg = new Image("file:assets/bricks/brick3.png");
        return new MovingBrick(brickImg, x, y, GameConfig.BRICK_WIDTH, GameConfig.BRICK_HEIGHT, 3);
    }

    public void setDx(int dx)
    {
        this.dx=dx;
    }
}
