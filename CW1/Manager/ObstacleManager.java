package CW1.Manager;

import CW1.MVCPatten.GameConfig;
import CW1.MVCPatten.GameModel;
import CW1.PathFinder.PathFinder;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ObstacleManager {
    private GameModel model;
    private PathFinder pathFinder;
    private Set<int[]> obstacles;  // 用 Set 来存储障碍物的位置，避免重复

    public ObstacleManager(GameModel model, PathFinder pathFinder) {
        this.model = model;
        this.pathFinder = pathFinder;  // 这里可能是 null
        this.obstacles = new HashSet<>();  // 初始化障碍物集合
    }

    public void placeObstacles() {
        Random rand = new Random();
        int placedObstacles = 0;

        while (placedObstacles < GameConfig.NUM_OBSTACLES) {
            int x = rand.nextInt(GameConfig.MAP_SIZE);
            int y = rand.nextInt(GameConfig.MAP_SIZE);

            if (model.getMap()[x][y] == GameConfig.EMPTY) {
                model.getMap()[x][y] = GameConfig.OBSTACLE;
                obstacles.add(new int[]{x, y});  // 存储障碍物位置

                if (!pathFinder.pathsAreClear()) {
                    model.getMap()[x][y] = GameConfig.EMPTY;  // 撤回障碍物
                    obstacles.remove(new int[]{x, y});  // 从集合中移除
                } else {
                    placedObstacles++;
                }
            }
        }
    }

    // 获取所有障碍物的位置
    public Set<int[]> getObstacles() {
        return obstacles;
    }
}



