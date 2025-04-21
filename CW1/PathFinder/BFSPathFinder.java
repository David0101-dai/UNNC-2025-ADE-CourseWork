package CW1.PathFinder;

import CW1.MVCPatten.GameConfig;
import CW1.MVCPatten.GameModel;

import java.util.*;

public class BFSPathFinder extends PathFinder {
    public BFSPathFinder(GameModel model) {
        super(model);
    }

    @Override
    public List<int[]> findPathToNearestTreasure(int playerX, int playerY) {
        int[] nearestTreasure = findNearestTreasure(playerX, playerY, model.getTreasures());
        return findPathBFS(playerX, playerY, nearestTreasure[0], nearestTreasure[1]);
    }

    private List<int[]> findPathBFS(int startX, int startY, int targetX, int targetY) {
        boolean[][] visited = new boolean[GameConfig.MAP_SIZE][GameConfig.MAP_SIZE];
        Queue<int[]> queue = new LinkedList<>();
        Map<String, int[]> parentMap = new HashMap<>();

        queue.offer(new int[]{startX, startY});
        visited[startX][startY] = true;

        int[] directions = {-1, 0, 1, 0, 0, -1, 0, 1};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0], y = current[1];

            if (x == targetX && y == targetY) {
                return reconstructPath(parentMap, startX, startY, targetX, targetY);
            }

            for (int i = 0; i < 4; i++) {
                int newX = x + directions[i*2];
                int newY = y + directions[i*2+1];

                if (isValidMove(newX, newY) && !visited[newX][newY]) {
                    visited[newX][newY] = true;
                    queue.offer(new int[]{newX, newY});
                    parentMap.put(newX + "," + newY, current);
                }
            }
        }
        return null;
    }

    private List<int[]> reconstructPath(Map<String, int[]> parentMap, int startX, int startY, int targetX, int targetY) {
        List<int[]> path = new ArrayList<>();
        int[] current = new int[]{targetX, targetY};
        path.add(current);

        while (current[0] != startX || current[1] != startY) {
            current = parentMap.get(current[0] + "," + current[1]);
            path.add(current);
        }

        Collections.reverse(path);
        return path;
    }

    // 实现 pathsAreClear 方法
    @Override
    public boolean pathsAreClear() {
        // 假设玩家位置是 model.getPlayerX(), model.getPlayerY()
        int playerX = model.getPlayerX();
        int playerY = model.getPlayerY();

        // 获取最近的宝藏
        int[] nearestTreasure = findNearestTreasure(playerX, playerY, model.getTreasures());
        if (nearestTreasure == null) {
            return false;  // 如果没有找到宝藏，返回 false
        }

        // 尝试找到一条从玩家到最近宝藏的有效路径
        List<int[]> path = findPathBFS(playerX, playerY, nearestTreasure[0], nearestTreasure[1]);
        return path != null && !path.isEmpty();
    }

}