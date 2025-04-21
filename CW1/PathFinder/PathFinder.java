package CW1.PathFinder;

import CW1.MVCPatten.GameConfig;
import CW1.MVCPatten.GameModel;

import java.util.List;

public abstract class PathFinder {
    protected GameModel model;  // Reference to the GameModel, which contains the game state

    // Constructor to initialize the model
    public PathFinder(GameModel model) {
        this.model = model;
    }

    // Abstract method to find the path from the player's current position to the nearest treasure
    public abstract List<int[]> findPathToNearestTreasure(int playerX, int playerY);

    // Abstract method to check if the paths are clear (i.e., there are no obstacles blocking the way)
    public abstract boolean pathsAreClear();

    // Public method to provide a direction hint for the player
    public String getDirectionHint(int playerX, int playerY) {
        List<int[]> treasures = model.getTreasures();  // Get the list of treasures on the map
        if (treasures.isEmpty()) return "No treasures left.";  // Return a message if there are no treasures left

        List<int[]> path = findPathToNearestTreasure(playerX, playerY);  // Find the path to the nearest treasure
        if (path == null || path.size() <= 1) {
            return "No valid path to treasure.";  // If there's no valid path, return a message
        }

        int[] nextStep = path.get(1);  // Get the next step to move towards the nearest treasure
        return getDirectionMessage(playerX, playerY, nextStep[0], nextStep[1]);  // Generate and return the direction message
    }

    // Helper method to generate a direction message based on the player's position and the target position
    protected String getDirectionMessage(int fromX, int fromY, int toX, int toY) {
        if (toX < fromX) return "Go up.";  // If the target is above, instruct the player to go up
        if (toX > fromX) return "Go down.";  // If the target is below, instruct the player to go down
        if (toY < fromY) return "Go left.";  // If the target is to the left, instruct the player to go left
        if (toY > fromY) return "Go right.";  // If the target is to the right, instruct the player to go right
        return "You are at the treasure!";  // If the player has reached the target, return a message saying they are at the treasure
    }

    // Method to check if a move is valid (i.e., within bounds and not an obstacle)
    protected boolean isValidMove(int x, int y) {
        return x >= 0 && x < GameConfig.MAP_SIZE &&  // Check if x is within the map bounds
                y >= 0 && y < GameConfig.MAP_SIZE &&  // Check if y is within the map bounds
                model.getMap()[x][y] != GameConfig.OBSTACLE;  // Check if the position is not blocked by an obstacle
    }

    // Method to find the nearest treasure based on the player's position
    protected int[] findNearestTreasure(int playerX, int playerY, List<int[]> treasures) {
        int minDistance = Integer.MAX_VALUE;  // Initialize the minimum distance as a large value
        int[] nearest = null;  // Variable to store the coordinates of the nearest treasure

        // Loop through all treasures and find the closest one
        for (int[] treasure : treasures) {
            // Calculate the Manhattan distance from the player to the current treasure
            int dist = Math.abs(treasure[0] - playerX) + Math.abs(treasure[1] - playerY);
            if (dist < minDistance) {
                minDistance = dist;  // Update the minimum distance if a closer treasure is found
                nearest = treasure;  // Set the current treasure as the nearest one
            }
        }
        return nearest;  // Return the coordinates of the nearest treasure
    }
}
