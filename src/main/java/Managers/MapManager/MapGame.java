package Managers.MapManager;

import Managers.GameConfig.GameConfig;
import items.Brick;
import items.factory.FactoryBrick;
import items.factory.MovingBrick;
import items.factory.NormalBrick;
import items.factory.StrongBrick;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MapGame {

    private List<Brick> bricks = new ArrayList<Brick>(); // Danh sách các viên gạch trong bản đồ
    private int currentLevel = 1; // Map hiện tại của trò chơi

    public void ReadFileMap(String path) throws Exception {
        bricks.clear(); // Xóa danh sách gạch hiện tại trước khi đọc bản đồ mới

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path)); // Mở file bản đồ để đọc
            String line;
            int y = 0;
            while ((line = reader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    int x = i;
                    char digit = line.charAt(i);
                    switch (digit) {
                        case '1':
                            FactoryBrick Fbrick1 = new NormalBrick();
                            Brick brick1 = Fbrick1.createBrick(GameConfig.SCREEN_X + x * GameConfig.BRICK_WIDTH,
                                    GameConfig.SCREEN_Y + y * GameConfig.BRICK_HEIGHT);
                            bricks.add(brick1);
                            break;
                        case '2':
                            FactoryBrick Fbrick2 = new StrongBrick();
                            Brick brick2 = Fbrick2.createBrick(GameConfig.SCREEN_X + x * GameConfig.BRICK_WIDTH,
                                    GameConfig.SCREEN_Y + y * GameConfig.BRICK_HEIGHT);
                            bricks.add(brick2);
                            break;
                        case '3':
                            FactoryBrick Fbrick3 = new MovingBrick();
                            Brick brick3 = Fbrick3.createBrick(GameConfig.SCREEN_X + x * GameConfig.BRICK_WIDTH,
                                    GameConfig.SCREEN_Y + y * GameConfig.BRICK_HEIGHT);
                            bricks.add(brick3);
                            break;
                        default:
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
    String path = "Map/map" + currentLevel + ".txt";
    File file = new File(path);
    if (!file.exists() || file.length() == 0) {
        System.err.println("Map " + currentLevel + " chưa tồn tại hoặc rỗng!");
        return;
    }
    this.ReadFileMap(path);
}

    public List<Brick> getMapBricks() {
        return this.bricks;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int level) {
        if (level >= 1 && level <= 7) {
            currentLevel = level;
        }
    }
}
