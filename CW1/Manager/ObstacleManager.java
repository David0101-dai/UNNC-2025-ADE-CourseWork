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
                model.getMap()[x][y] = GameConfig.OBSTACLE;
                if (!pathFinder.pathsAreClear()) {
                    model.getMap()[x][y] = GameConfig.EMPTY;
                } else {
                    placedObstacles++;
                }
            }
        }
    }
}

