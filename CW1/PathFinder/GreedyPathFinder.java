package CW1.PathFinder;

import CW1.MVCPatten.GameConfig;
import CW1.MVCPatten.GameModel;

import java.util.*;

public class GreedyPathFinder extends PathFinder {
    public GreedyPathFinder(GameModel model) {
        super(model);
    }

    @Override
    public List<int[]> findPathToNearestTreasure(int playerX, int playerY) {
        int[] nearestTreasure = findNearestTreasure(playerX, playerY, model.getTreasures());
        return findPathGreedy(playerX, playerY, nearestTreasure[0], nearestTreasure[1]);
    }

    @Override
    public boolean pathsAreClear() {
        return false;
    }

    private List<int[]> findPathGreedy(int startX, int startY, int targetX, int targetY) {
        PriorityQueue<GreedyNode> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.h));
        Set<String> visited = new HashSet<>();

        GreedyNode startNode = new GreedyNode(startX, startY, heuristic(startX, startY, targetX, targetY), null);
        openSet.add(startNode);

        int[] directions = {-1, 0, 1, 0, 0, -1, 0, 1};  // Up, right, down, left

        while (!openSet.isEmpty()) {
            GreedyNode current = openSet.poll();

            if (current.x == targetX && current.y == targetY) {
                return reconstructPath(current);
            }

            for (int i = 0; i < 4; i++) {
                int newX = current.x + directions[i * 2];
                int newY = current.y + directions[i * 2 + 1];

                if (!isValidMove(newX, newY) || visited.contains(newX + "," + newY)) continue;

                int newH = heuristic(newX, newY, targetX, targetY);
                GreedyNode newNode = new GreedyNode(newX, newY, newH, current);
                openSet.add(newNode);
                visited.add(newX + "," + newY);
            }
        }
        return null;
    }

    private int heuristic(int x, int y, int targetX, int targetY) {
        // Manhattan distance as the heuristic
        return Math.abs(x - targetX) + Math.abs(y - targetY);
    }

    private List<int[]> reconstructPath(GreedyNode endNode) {
        List<int[]> path = new ArrayList<>();
        GreedyNode current = endNode;
        while (current != null) {
            path.add(new int[]{current.x, current.y});
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }

    private static class GreedyNode {
        int x, y, h;
        GreedyNode parent;

        GreedyNode(int x, int y, int h, GreedyNode parent) {
            this.x = x;
            this.y = y;
            this.h = h;  // Only heuristic (h) is considered
            this.parent = parent;
        }
    }
}
