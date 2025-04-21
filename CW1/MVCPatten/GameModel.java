package CW1.MVCPatten;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class GameModel {
    private char[][] map = new char[GameConfig.MAP_SIZE][GameConfig.MAP_SIZE];
    private boolean[][] obstacleTouched = new boolean[GameConfig.MAP_SIZE][GameConfig.MAP_SIZE];  // 用于跟踪障碍物是否被触碰
    public int playerX, playerY;
    private int score = 100;
    private List<int[]> treasures = new ArrayList<>();
    private GameController controller;

    public GameModel(GameController controller) {
        this.controller = controller;
        initializeMap();
        placePlayer();
        placeTreasures();
        placeObstacles();
        ensurePaths();  // 确保存在通向宝藏的路径
    }

    public boolean[][] getObstacleTouched() {
        return obstacleTouched;
    }

    public List<int[]> getTreasures() {
        return treasures;
    }

    private void initializeMap() {
        for (int i = 0; i < GameConfig.MAP_SIZE; i++) {
            for (int j = 0; j < GameConfig.MAP_SIZE; j++) {
                map[i][j] = GameConfig.EMPTY;  // 设置所有位置为空
                obstacleTouched[i][j] = false;  // 初始障碍物没有被触碰
            }
        }
    }

    private void placePlayer() {
        Random rand = new Random();
        int x, y;

        // 确保生成的位置有效，不与障碍物或宝藏重叠
        while (true) {
            x = rand.nextInt(GameConfig.MAP_SIZE);  // 随机选择一个位置
            y = rand.nextInt(GameConfig.MAP_SIZE);

            // 检查该位置是否为障碍物或宝藏
            if (map[x][y] == GameConfig.EMPTY) {  // 确保位置为空白格
                break;  // 如果是空白格，跳出循环
            }
        }

        // 设置玩家位置
        playerX = x;
        playerY = y;
        map[playerX][playerY] = GameConfig.PLAYER;
    }



    private void placeTreasures() {
        Random rand = new Random();
        for (int i = 0; i < GameConfig.NUM_TREASURES; i++) {
            int x = -1, y = -1;
            boolean isValid = false;

            while (!isValid) {
                x = rand.nextInt(GameConfig.MAP_SIZE);
                y = rand.nextInt(GameConfig.MAP_SIZE);

                if (Math.abs(playerX - x) < 5 || Math.abs(playerY - y) < 5) {
                    continue;  // 如果宝藏与玩家距离小于5格，重新生成
                }

                isValid = true;
                for (int[] treasure : treasures) {
                    if (Math.abs(treasure[0] - x) < 5 && Math.abs(treasure[1] - y) < 5) {
                        isValid = false;
                        break;
                    }
                }
            }

            map[x][y] = GameConfig.TREASURE;
            treasures.add(new int[]{x, y});
        }
    }

    private void placeObstacles() {
        Random rand = new Random();
        int placedObstacles = 0;

        while (placedObstacles < GameConfig.NUM_OBSTACLES) {
            int x = rand.nextInt(GameConfig.MAP_SIZE);
            int y = rand.nextInt(GameConfig.MAP_SIZE);

            if (map[x][y] == GameConfig.EMPTY) {
                map[x][y] = GameConfig.OBSTACLE;  // 放置白色障碍物
                obstacleTouched[x][y] = false;  // 标记为未触碰

                if (!pathsAreClear()) {
                    map[x][y] = GameConfig.EMPTY;  // 如果路径不可行，撤销该障碍物
                } else {
                    placedObstacles++;
                }
            }
        }
    }

    private boolean pathsAreClear() {
        boolean[][] visited = new boolean[GameConfig.MAP_SIZE][GameConfig.MAP_SIZE];
        bfs(playerX, playerY, visited);

        for (int i = 0; i < GameConfig.MAP_SIZE; i++) {
            for (int j = 0; j < GameConfig.MAP_SIZE; j++) {
                if (map[i][j] != GameConfig.OBSTACLE && !visited[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void bfs(int startX, int startY, boolean[][] visited) {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;

        int[] directions = {-1, 0, 1, 0, 0, -1, 0, 1};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            for (int i = 0; i < 4; i++) {
                int newX = x + directions[i * 2];
                int newY = y + directions[i * 2 + 1];

                if (newX >= 0 && newX < GameConfig.MAP_SIZE && newY >= 0 && newY < GameConfig.MAP_SIZE &&
                        !visited[newX][newY] && map[newX][newY] != GameConfig.OBSTACLE) {
                    visited[newX][newY] = true;
                    queue.add(new int[]{newX, newY});
                }
            }
        }
    }

    private void ensurePaths() {
        int retries = 0;

        while (!pathsAreClear() && retries < 100) {
            resetMap();
            placePlayer();
            placeTreasures();
            placeObstacles();
            retries++;
        }

        if (retries == 100) {
            System.out.println("Unable to ensure paths after 100 retries.");
        }
    }

    private void resetMap() {
        for (int i = 0; i < GameConfig.MAP_SIZE; i++) {
            for (int j = 0; j < GameConfig.MAP_SIZE; j++) {
                if (map[i][j] != GameConfig.PLAYER && map[i][j] != GameConfig.TREASURE) {
                    map[i][j] = GameConfig.EMPTY;
                }
            }
        }
    }

    public void movePlayer(char direction) {
        int newX = playerX;
        int newY = playerY;

        switch (direction) {
            case 'w': newX--; break;
            case 's': newX++; break;
            case 'a': newY--; break;
            case 'd': newY++; break;
        }

        // 检查是否越界
        if (newX < 0 || newX >= GameConfig.MAP_SIZE || newY < 0 || newY >= GameConfig.MAP_SIZE) {
            controller.getView().setMessage("This is the map boundary. Please go back.");
            score -= 1;  // 每次越界减少1分
        } else if (map[newX][newY] == GameConfig.OBSTACLE) {
            if (!obstacleTouched[newX][newY]) {
                obstacleTouched[newX][newY] = true;
                map[newX][newY] = GameConfig.OBSTACLE;
            }
            controller.getView().setMessage("You bumped into an obstacle!");
            score -= 10;
        } else {
            // 移动到新的位置
            final int targetX = newX;
            final int targetY = newY;
            treasures.removeIf(t -> t[0] == targetX && t[1] == targetY);

            if (map[newX][newY] == GameConfig.TREASURE) {
                score += 10;
                map[newX][newY] = GameConfig.EMPTY;
            }

            map[playerX][playerY] = GameConfig.EMPTY;
            playerX = newX;
            playerY = newY;
            map[playerX][playerY] = GameConfig.PLAYER;
        }

        score -= 1;  // 每次移动都扣除1分
        controller.getView().repaint();
    }

    public int getScore() {
        return score;
    }

    public char[][] getMap() {
        return map;
    }

    public boolean isGameOver() {
        return treasures.isEmpty();
    }

    public void decreaseScore(int points) {
        score -= points;
        if (score < 0) score = 0;  // Ensure the score doesn't go below 0
    }

}
