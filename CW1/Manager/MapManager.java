package CW1.Manager;

import CW1.MVCPatten.GameConfig;
import CW1.MVCPatten.GameModel;

public class MapManager {
    private char[][] map = new char[GameConfig.MAP_SIZE][GameConfig.MAP_SIZE];
    private boolean[][] obstacleTouched = new boolean[GameConfig.MAP_SIZE][GameConfig.MAP_SIZE];
    private GameModel model;
    public MapManager(GameModel model) {
        this.model = model;
    }

    public void initializeMap() {
        for (int i = 0; i < GameConfig.MAP_SIZE; i++) {
            for (int j = 0; j < GameConfig.MAP_SIZE; j++) {
                map[i][j] = GameConfig.EMPTY;
                obstacleTouched[i][j] = false;
            }
        }

        // 将所有障碍物初始为白色
        for (int[] obstacle : model.getObstacleManager().getObstacles()) {
            int x = obstacle[0];
            int y = obstacle[1];
            map[x][y] = GameConfig.OBSTACLE;  // 初始化为障碍物
        }
    }

    public void resetMap() {
        for (int i = 0; i < GameConfig.MAP_SIZE; i++) {
            for (int j = 0; j < GameConfig.MAP_SIZE; j++) {
                if (map[i][j] != GameConfig.PLAYER && map[i][j] != GameConfig.TREASURE) {
                    map[i][j] = GameConfig.EMPTY;
                }
            }
        }
        // 初始化障碍物为白色
        for (int[] obstacle : model.getObstacleManager().getObstacles()) {
            int x = obstacle[0];
            int y = obstacle[1];
            map[x][y] = GameConfig.OBSTACLE;
        }
    }

    public char[][] getMap() {
        return map;
    }

    public boolean[][] getObstacleTouched() {
        return obstacleTouched;
    }
}

