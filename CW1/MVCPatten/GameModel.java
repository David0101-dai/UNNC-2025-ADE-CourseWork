package CW1.MVCPatten;

import CW1.Manager.MapManager;
import CW1.Manager.ObstacleManager;
import CW1.Manager.Player;
import CW1.Manager.TreasureManager;
import CW1.PathFinder.BFSPathFinder;
import CW1.PathFinder.PathFinder;

import java.util.List;

public class GameModel {
    private GameController controller;
    private MapManager mapManager;
    private Player player;
    private TreasureManager treasureManager;
    private ObstacleManager obstacleManager;
    private PathFinder pathFinder;

    private int score = 100;

    public GameModel(GameController controller) {
        this.controller = controller;
        this.pathFinder = new BFSPathFinder(this);

        mapManager = new MapManager(this);
        player = new Player(this);
        treasureManager = new TreasureManager(this);
        obstacleManager = new ObstacleManager(this, pathFinder);

        initializeGame();
    }

    private void initializeGame() {
        mapManager.initializeMap();
        player.placePlayer();
        treasureManager.placeTreasures();
        obstacleManager.placeObstacles();
        ensurePaths();
    }

    public List<int[]> getTreasures() {
        return treasureManager.getTreasures();
    }

    private void ensurePaths() {
        int retries = 0;
        while (!pathFinder.pathsAreClear() && retries < 100) {
            resetMap();
            player.placePlayer();
            treasureManager.placeTreasures();
            obstacleManager.placeObstacles();
            retries++;
        }

        if (retries == 100) {
            System.out.println("Unable to ensure paths after 100 retries.");
        }
    }

    private void resetMap() {
        mapManager.resetMap();
    }

    public void movePlayer(char direction) {
        player.movePlayer(direction);
        controller.update();  // 让 GameController 负责路径更新和界面刷新
    }

    public int getScore() {
        return score;
    }

    public char[][] getMap() {
        return mapManager.getMap();
    }

    public boolean isGameOver() {
        return treasureManager.getTreasures().isEmpty();
    }

    public GameController getController() {
        return controller;
    }

    public void decreaseScore(int points) {
        score -= points;
        if (score < 0) score = 0;
    }

    // 获取玩家的 X 坐标
    public int getPlayerX() {
        return player.getX();  // 假设 Player 类有 getX 方法
    }

    // 获取玩家的 Y 坐标
    public int getPlayerY() {
        return player.getY();  // 假设 Player 类有 getY 方法
    }

    // 新增：获取 obstacleTouched 数组
    public boolean[][] getObstacleTouched() {
        return mapManager.getObstacleTouched();  // 返回 MapManager 中的 obstacleTouched 数组
    }

    public void playerCollectsTreasure(int x, int y) {
        treasureManager.removeTreasure(x, y);  // 移除宝藏
        this.getMap()[x][y] = GameConfig.EMPTY;  // 更新地图
    }
    public void addScore(int points) {
        score += points;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public TreasureManager getTreasureManager() {
        return treasureManager;
    }

    public ObstacleManager getObstacleManager() {
        return obstacleManager;
    }

}


