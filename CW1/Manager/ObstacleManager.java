package CW1.Manager;

import CW1.MVCPatten.GameConfig;
import CW1.MVCPatten.GameModel;
import CW1.PathFinder.PathFinder;

import java.util.Random;

public class ObstacleManager {
    private GameModel model;
    private PathFinder pathFinder;

    public ObstacleManager(GameModel model, PathFinder pathFinder) {
        this.model = model;
        this.pathFinder = pathFinder;  // 这里可能是 null
    }

    public void placeObstacles() {
        Random rand = new Random();
        int placedObstacles = 0;

        while (placedObstacles < GameConfig.NUM_OBSTACLES) {
            int x = rand.nextInt(GameConfig.MAP_SIZE);
            int y = rand.nextInt(GameConfig.MAP_SIZE);

            if (model.getMap()[x][y] == GameConfig.EMPTY) {
                // 先放置障碍物，然后检查路径
                model.getMap()[x][y] = GameConfig.OBSTACLE;

                // 检查路径是否可用
                if (!pathFinder.pathsAreClear()) {
                    model.getMap()[x][y] = GameConfig.EMPTY;  // 如果路径不可用，撤回该障碍物
                } else {
                    placedObstacles++;  // 如果路径可用，继续放置障碍物
                }
            }
        }
    }
}


