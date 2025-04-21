package CW1.PathFinder;

import CW1.MVCPatten.GameConfig;
import CW1.MVCPatten.GameModel;

import java.util.*;

public class AStarPathFinder extends PathFinder {
    public AStarPathFinder(GameModel model) {
        super(model);
    }

    @Override
    public List<int[]> findPathToNearestTreasure(int playerX, int playerY) {
        int[] nearestTreasure = findNearestTreasure(playerX, playerY, model.getTreasures());
        return findPathAStar(playerX, playerY, nearestTreasure[0], nearestTreasure[1]);
    }

    @Override
    public boolean pathsAreClear() {
        return false;
    }

    private List<int[]> findPathAStar(int startX, int startY, int targetX, int targetY) {
        PriorityQueue<AStarNode> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        Map<String, AStarNode> allNodes = new HashMap<>();

        AStarNode startNode = new AStarNode(startX, startY, 0, heuristic(startX, startY, targetX, targetY), null);
        openSet.add(startNode);
        allNodes.put(startX + "," + startY, startNode);

        int[] directions = {-1, 0, 1, 0, 0, -1, 0, 1};

        while (!openSet.isEmpty()) {
            AStarNode current = openSet.poll();

            if (current.x == targetX && current.y == targetY) {
                return reconstructPath(allNodes, current);
            }

            for (int i = 0; i < 4; i++) {
                int newX = current.x + directions[i*2];
                int newY = current.y + directions[i*2+1];

                if (!isValidMove(newX, newY)) continue;

                int newG = current.g + 1;
                String key = newX + "," + newY;

                if (!allNodes.containsKey(key) || newG < allNodes.get(key).g) {
                    int newH = heuristic(newX, newY, targetX, targetY);
                    AStarNode newNode = new AStarNode(newX, newY, newG, newH, current);
                    allNodes.put(key, newNode);
                    openSet.add(newNode);
                }
            }
        }
        return null;
    }

    private int heuristic(int x, int y, int targetX, int targetY) {
        int distance = Math.abs(x - targetX) + Math.abs(y - targetY);
        if (x <= 1 || x >= GameConfig.MAP_SIZE-2 || y <= 1 || y >= GameConfig.MAP_SIZE-2) {
            distance += 1; // 边界惩罚
        }
        return distance;
    }

    private List<int[]> reconstructPath(Map<String, AStarNode> allNodes, AStarNode endNode) {
        List<int[]> path = new ArrayList<>();
        AStarNode current = endNode;
        while (current != null) {
            path.add(new int[]{current.x, current.y});
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }

    private static class AStarNode {
        int x, y, g, f;
        AStarNode parent;
        AStarNode(int x, int y, int g, int h, AStarNode parent) {
            this.x = x;
            this.y = y;
            this.g = g;
            this.f = g + h;
            this.parent = parent;
        }
    }
}