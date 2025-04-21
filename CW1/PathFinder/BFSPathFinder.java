package CW1.PathFinder;

import CW1.MVCPatten.GameConfig;
import CW1.MVCPatten.GameModel;

import java.util.*;

public class BFSPathFinder extends PathFinder {
    // Constructor to initialize the model and call the parent constructor
    public BFSPathFinder(GameModel model) {
        super(model);
    }

    // Method to find the path to the nearest treasure using BFS
    @Override
    public List<int[]> findPathToNearestTreasure(int playerX, int playerY) {
        // Find the nearest treasure based on the player's position
        int[] nearestTreasure = findNearestTreasure(playerX, playerY, model.getTreasures());
        // Call the BFS method to find the path to the nearest treasure
        return findPathBFS(playerX, playerY, nearestTreasure[0], nearestTreasure[1]);
    }

    // BFS (Breadth-First Search) algorithm to find the shortest path to the target
    private List<int[]> findPathBFS(int startX, int startY, int targetX, int targetY) {
        boolean[][] visited = new boolean[GameConfig.MAP_SIZE][GameConfig.MAP_SIZE];  // Track visited nodes
        Queue<int[]> queue = new LinkedList<>();  // Queue for BFS
        Map<String, int[]> parentMap = new HashMap<>();  // Map to track the parent of each node (for path reconstruction)

        // Start BFS from the player's position
        queue.offer(new int[]{startX, startY});
        visited[startX][startY] = true;

        // Directions array for moving up, right, down, and left
        int[] directions = {-1, 0, 1, 0, 0, -1, 0, 1};

        // While there are nodes to explore
        while (!queue.isEmpty()) {
            int[] current = queue.poll();  // Dequeue the next node
            int x = current[0], y = current[1];

            // If the target is found, reconstruct and return the path
            if (x == targetX && y == targetY) {
                return reconstructPath(parentMap, startX, startY, targetX, targetY);
            }

            // Explore the neighboring nodes
            for (int i = 0; i < 4; i++) {
                int newX = x + directions[i * 2];  // Calculate the new X coordinate
                int newY = y + directions[i * 2 + 1];  // Calculate the new Y coordinate

                // Skip invalid moves or already visited nodes
                if (isValidMove(newX, newY) && !visited[newX][newY]) {
                    visited[newX][newY] = true;  // Mark the new node as visited
                    queue.offer(new int[]{newX, newY});  // Enqueue the new node
                    parentMap.put(newX + "," + newY, current);  // Set the parent of the new node for path reconstruction
                }
            }
        }
        return null;  // Return null if no path is found
    }

    // Method to reconstruct the path from the end node to the start node
    private List<int[]> reconstructPath(Map<String, int[]> parentMap, int startX, int startY, int targetX, int targetY) {
        List<int[]> path = new ArrayList<>();
        int[] current = new int[]{targetX, targetY};
        path.add(current);

        // Reconstruct the path by following the parent nodes from the target to the start
        while (current[0] != startX || current[1] != startY) {
            current = parentMap.get(current[0] + "," + current[1]);  // Get the parent node
            path.add(current);  // Add the parent node to the path
        }

        Collections.reverse(path);  // Reverse the path to get it from start to end
        return path;  // Return the reconstructed path
    }

    // Method to check if there is a valid path to each treasure
    @Override
    public boolean pathsAreClear() {
        int playerX = model.getPlayerX();  // Get the player's X coordinate
        int playerY = model.getPlayerY();  // Get the player's Y coordinate

        List<int[]> treasures = model.getTreasures();  // Get the list of all treasures

        // Iterate through all treasures and check if each has a valid path
        for (int[] treasure : treasures) {
            int targetX = treasure[0];  // Get the treasure's X coordinate
            int targetY = treasure[1];  // Get the treasure's Y coordinate

            // Try to find a path from the player to the treasure
            List<int[]> path = findPathBFS(playerX, playerY, targetX, targetY);

            // If a path does not exist for any treasure, return false
            if (path == null || path.isEmpty()) {
                return false;
            }
        }

        // If all treasures have valid paths, return true
        return true;
    }
}
