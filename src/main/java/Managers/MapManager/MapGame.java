package Managers.MapManager;

import Managers.GameConfig.GameConfig;
import items.Brick;
import items.factory.FactoryBrick;
import items.factory.NormalBrick;

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
                System.out.println("Dòng đọc được: " + line);

                for (int i = 0; i < line.length(); i++) {
                    int x = i;
                    char digit = line.charAt(i);
                    if (digit == '1') {
                        FactoryBrick Fbrick = new NormalBrick();
                        Brick brick = Fbrick.createBrick(x * GameConfig.BRICK_WIDTH, y * GameConfig.BRICK_HEIGHT);
                        bricks.add(brick);
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
