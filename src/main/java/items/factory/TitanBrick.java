package items.factory;

import Managers.GameConfig.GameConfig;
import items.Brick;
import javafx.scene.image.Image;

public class TitanBrick implements FactoryBrick {
    @Override
    public Brick createBrick(int x, int y) {
        Image brickImg = new Image("file:assets/bricks/brick5.png");
        return new Brick(brickImg, x, y, GameConfig.BRICK_WIDTH, GameConfig.BRICK_HEIGHT, 5);
    }
}
