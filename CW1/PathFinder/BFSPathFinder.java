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

    @Override
    public boolean pathsAreClear() {
        // 获取玩家位置
        int playerX = model.getPlayerX();
        int playerY = model.getPlayerY();

        // 获取所有宝藏的位置
        List<int[]> treasures = model.getTreasures();

        // 遍历所有宝藏，检查每个宝藏是否都有一条有效路径
        for (int[] treasure : treasures) {
            int targetX = treasure[0];
            int targetY = treasure[1];

            // 尝试找到从玩家到宝藏的路径
            List<int[]> path = findPathBFS(playerX, playerY, targetX, targetY);

            // 如果路径不存在，返回 false
            if (path == null || path.isEmpty()) {
                return false;
            }
        }

        // 如果所有宝藏都有有效路径，返回 true
        return true;
    }


}