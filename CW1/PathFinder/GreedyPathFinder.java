package CW1.PathFinder;

import CW1.MVCPatten.GameModel;

import java.util.*;

public class GreedyPathFinder extends PathFinder {
    public GreedyPathFinder(GameModel model) {
        super(model);
    }

    @Override
    public List<int[]> findPathToNearestTreasure(int playerX, int playerY) {
        int[] nearestTreasure = findNearestTreasure(playerX, playerY, model.getTreasures());
        List<int[]> path = new ArrayList<>();
        path.add(new int[]{playerX, playerY});

        Set<String> visited = new HashSet<>();
        int[] lastMove = null; // Track last movement direction
        int maxSteps = 1000;

        while (maxSteps-- > 0) {
            int[] nextStep = getGreedyStep(playerX, playerY, nearestTreasure[0], nearestTreasure[1],
                    visited, lastMove);

            if (nextStep == null) break;

            // Update last move direction
            lastMove = new int[]{nextStep[0] - playerX, nextStep[1] - playerY};

            path.add(nextStep);
            playerX = nextStep[0];
            playerY = nextStep[1];
            visited.add(playerX + "," + playerY);

            if (playerX == nearestTreasure[0] && playerY == nearestTreasure[1]) {
                break;
            }

            nearestTreasure = findNearestTreasure(playerX, playerY, model.getTreasures());
        }

        return path;
    }

    private int[] getGreedyStep(int fromX, int fromY, int targetX, int targetY,
                                Set<String> visited, int[] lastMove) {
        int[] directions = {-1, 0, 1, 0, 0, -1, 0, 1}; // up, right, down, left
        int bestScore = Integer.MAX_VALUE;
        int[] bestStep = null;

        for (int i = 0; i < 4; i++) {
            int newX = fromX + directions[i * 2];
            int newY = fromY + directions[i * 2 + 1];

            if (!isValidMove(newX, newY)) continue;
            if (visited.contains(newX + "," + newY)) continue;

            // Calculate Manhattan distance to target
            int distance = Math.abs(newX - targetX) + Math.abs(newY - targetY);

            // Add small penalty if this would reverse the last move (prevent back-and-forth)
            if (lastMove != null &&
                    newX == fromX - lastMove[0] &&
                    newY == fromY - lastMove[1]) {
                distance += 1; // Small penalty to discourage reversing
            }

            if (distance < bestScore) {
                bestScore = distance;
                bestStep = new int[]{newX, newY};
            }
        }

        return bestStep;
    }
}