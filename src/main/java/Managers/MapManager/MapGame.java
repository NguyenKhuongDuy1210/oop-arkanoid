package Managers.MapManager;

import Managers.GameConfig.GameConfig;
import items.Brick;
import items.factory.FactoryBrick;
import items.factory.NormalBrick;
import items.factory.StrongBrick;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MapGame {
    private List<Brick> bricks = new ArrayList<Brick>();

    public void ReadFileMap(String path) throws Exception {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            int y = 0;
            while ((line = reader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    int x = i;
                    char digit = line.charAt(i);
                    switch (digit) {
                        case '1':
                            FactoryBrick Fbrick1 = new NormalBrick();
                            Brick brick1 = Fbrick1.createBrick(x * GameConfig.BRICK_WIDTH, y * GameConfig.BRICK_HEIGHT);
                            bricks.add(brick1);
                            break;
                        case '2':
                            FactoryBrick Fbrick2 = new StrongBrick();
                            Brick brick2 = Fbrick2.createBrick(x * GameConfig.BRICK_WIDTH, y * GameConfig.BRICK_HEIGHT);
                            bricks.add(brick2);
                            break;

                    }
                }
                y++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createMapBricks() throws Exception {
        this.ReadFileMap("Map/map1.txt");
    }

    public List<Brick> getMapBricks() {
        return this.bricks;
    }
}
