package CW1;

import java.util.*;

public class TreasureHuntGame {
    private static final int MAP_SIZE = 20; // 地图大小 20x20
    private static final int NUM_TREASURES = 3; // 宝藏数量
    private static final char EMPTY = '.'; // 空格
    private static final char OBSTACLE = '#'; // 障碍物
    private static final char TREASURE = 'T'; // 宝藏
    private static final char PLAYER = 'P'; // 玩家

    private char[][] map = new char[MAP_SIZE][MAP_SIZE]; // 游戏地图
    private int playerX, playerY; // 玩家位置
    private int score = 100; // 初始分数
    private List<int[]> treasures = new ArrayList<>(); // 宝藏位置

    public TreasureHuntGame() {
        initializeMap();
        placeTreasures();
        playerX = 0;
        playerY = 0;
    }

    // 初始化地图
    private void initializeMap() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                map[i][j] = EMPTY; // 初始化为空格
            }
        }
        placeObstacles(); // 放置障碍物
    }

    // 放置障碍物
    private void placeObstacles() {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            int x = rand.nextInt(MAP_SIZE);
            int y = rand.nextInt(MAP_SIZE);
            if (map[x][y] == EMPTY) {
                map[x][y] = OBSTACLE;
            }
        }
    }

    // 放置宝藏
    private void placeTreasures() {
        Random rand = new Random();
        for (int i = 0; i < NUM_TREASURES; i++) {
            int x, y;
            do {
                x = rand.nextInt(MAP_SIZE);
                y = rand.nextInt(MAP_SIZE);
            } while (map[x][y] != EMPTY); // 确保宝藏放在空白位置
            map[x][y] = TREASURE;
            treasures.add(new int[] {x, y});
        }
    }

    // 打印地图
    public void printMap() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                if (i == playerX && j == playerY) {
                    System.out.print(PLAYER + " "); // 打印玩家位置
                } else {
                    System.out.print(map[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    // 玩家移动
    public void movePlayer(char direction) {
        int newX = playerX;
        int newY = playerY;

        switch (direction) {
            case 'w': newX--; break; // 上
            case 's': newX++; break; // 下
            case 'a': newY--; break; // 左
            case 'd': newY++; break; // 右
        }

        // 检查边界和障碍物
        if (newX >= 0 && newX < MAP_SIZE && newY >= 0 && newY < MAP_SIZE && map[newX][newY] != OBSTACLE) {
            playerX = newX;
            playerY = newY;

            // 检查是否找到宝藏
            if (map[playerX][playerY] == TREASURE) {
                score += 10;
                map[playerX][playerY] = EMPTY; // 找到宝藏后移除
                treasures.removeIf(t -> t[0] == playerX && t[1] == playerY); // 从宝藏列表中删除
                System.out.println("宝藏被找到！得分增加！");
            }
        } else {
            System.out.println("无法移动，碰到障碍物或越界！");
            score -= 10;
        }
    }

    // 显示当前分数
    public void displayScore() {
        System.out.println("当前分数：" + score);
    }

    // 游戏是否结束
    public boolean isGameOver() {
        return treasures.isEmpty(); // 宝藏都被找到时结束游戏
    }

    public static void main(String[] args) {
        TreasureHuntGame game = new TreasureHuntGame();
        Scanner scanner = new Scanner(System.in);

        while (!game.isGameOver()) {
            game.printMap();
            game.displayScore();
            System.out.println("请输入移动方向（w=上, s=下, a=左, d=右）:");
            char direction = scanner.next().charAt(0);
            game.movePlayer(direction);
        }

        System.out.println("游戏结束！你找到了所有的宝藏！");
        game.displayScore();
    }
}

