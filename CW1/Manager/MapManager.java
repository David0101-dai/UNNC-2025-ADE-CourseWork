package CW1.Manager;

import CW1.MVCPatten.GameConfig;

public class MapManager {
    private char[][] map = new char[GameConfig.MAP_SIZE][GameConfig.MAP_SIZE];
    private boolean[][] obstacleTouched = new boolean[GameConfig.MAP_SIZE][GameConfig.MAP_SIZE];

    public void initializeMap() {
        for (int i = 0; i < GameConfig.MAP_SIZE; i++) {
            for (int j = 0; j < GameConfig.MAP_SIZE; j++) {
                map[i][j] = GameConfig.EMPTY;
                obstacleTouched[i][j] = false;
            }
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
    }

    public char[][] getMap() {
        return map;
    }

    // 新增：获取 obstacleTouched 数组
    public boolean[][] getObstacleTouched() {
        return obstacleTouched;
    }
}
