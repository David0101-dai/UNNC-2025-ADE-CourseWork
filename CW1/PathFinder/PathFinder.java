package CW1.PathFinder;

import CW1.MVCPatten.GameConfig;
import CW1.MVCPatten.GameModel;

import java.util.List;

public abstract class PathFinder {
    protected GameModel model;

    public PathFinder(GameModel model) {
        this.model = model;
    }

    // 抽象方法：找到从当前位置到最近宝藏的路径
    public abstract List<int[]> findPathToNearestTreasure(int playerX, int playerY);

    // 公共方法：获取方向建议
    public String getDirectionHint(int playerX, int playerY) {
        List<int[]> treasures = model.getTreasures();
        if (treasures.isEmpty()) return "No treasures left.";

        List<int[]> path = findPathToNearestTreasure(playerX, playerY);
        if (path == null || path.size() <= 1) {
            return "No valid path to treasure.";
        }

        int[] nextStep = path.get(1);
        return getDirectionMessage(playerX, playerY, nextStep[0], nextStep[1]);
    }

    // 公共工具方法：计算方向消息
    protected String getDirectionMessage(int fromX, int fromY, int toX, int toY) {
        if (toX < fromX) return "Go up.";
        if (toX > fromX) return "Go down.";
        if (toY < fromY) return "Go left.";
        if (toY > fromY) return "Go right.";
        return "You are at the treasure!";
    }

    // 检查移动是否有效
    protected boolean isValidMove(int x, int y) {
        return x >= 0 && x < GameConfig.MAP_SIZE &&
                y >= 0 && y < GameConfig.MAP_SIZE &&
                model.getMap()[x][y] != GameConfig.OBSTACLE;
    }

    // 找到最近的宝藏
    protected int[] findNearestTreasure(int playerX, int playerY, List<int[]> treasures) {
        int minDistance = Integer.MAX_VALUE;
        int[] nearest = null;
        for (int[] treasure : treasures) {
            int dist = Math.abs(treasure[0] - playerX) + Math.abs(treasure[1] - playerY);
            if (dist < minDistance) {
                minDistance = dist;
                nearest = treasure;
            }
        }
        return nearest;
    }
}