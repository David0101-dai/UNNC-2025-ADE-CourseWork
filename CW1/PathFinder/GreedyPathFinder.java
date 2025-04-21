package CW1.PathFinder;

import CW1.MVCPatten.GameConfig;
import CW1.MVCPatten.GameModel;

import java.util.*;

public class GreedyPathFinder extends PathFinder {
    // Constructor: Inherit from the PathFinder class and initialize the model
    public GreedyPathFinder(GameModel model) {super(model);}
    // Method to find the path to the nearest treasure using a greedy approach
    @Override
    public List<int[]> findPathToNearestTreasure(int playerX, int playerY) {
        // Find the nearest treasure based on the player's current position
        int[] nearestTreasure = findNearestTreasure(playerX, playerY, model.getTreasures());
        // Call the method to find the path using a greedy strategy
        return findPathGreedy(playerX, playerY, nearestTreasure[0], nearestTreasure[1]);
    }

    // Method to check if paths are clear (not implemented in this greedy pathfinder)
    @Override
    public boolean pathsAreClear() {
        return false;
    }

    // Method to find the greedy path from the starting point to the target point
    private List<int[]> findPathGreedy(int startX, int startY, int targetX, int targetY) {
        // A priority queue to store nodes, ordered by their heuristic value (distance to target)
        PriorityQueue<GreedyNode> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.h));
        Set<String> visited = new HashSet<>();  // A set to track visited nodes to avoid revisiting them

        // Start node with the initial position and its heuristic value (distance to target)
        GreedyNode startNode = new GreedyNode(startX, startY, heuristic(startX, startY, targetX, targetY), null);
        openSet.add(startNode);  // Add the start node to the open set

        // Directions array for moving up, right, down, left
        int[] directions = {-1, 0, 1, 0, 0, -1, 0, 1};

        // While there are nodes to explore
        while (!openSet.isEmpty()) {
            GreedyNode current = openSet.poll();  // Get the node with the lowest heuristic (h)

            // If the current node is the target, reconstruct and return the path
            if (current.x == targetX && current.y == targetY) {
                return reconstructPath(current);
            }

            // Explore the neighboring nodes
            for (int i = 0; i < 4; i++) {
                int newX = current.x + directions[i * 2];  // Calculate the new X coordinate
                int newY = current.y + directions[i * 2 + 1];  // Calculate the new Y coordinate

                // Skip invalid moves or already visited nodes
                if (!isValidMove(newX, newY) || visited.contains(newX + "," + newY)) continue;

                // Calculate the heuristic (distance to the target) for the new node
                int newH = heuristic(newX, newY, targetX, targetY);
                // Create a new node with the calculated heuristic and add it to the open set
                GreedyNode newNode = new GreedyNode(newX, newY, newH, current);
                openSet.add(newNode);
                visited.add(newX + "," + newY);  // Mark the new node as visited
            }
        }
        return null;  // Return null if no path is found
    }

    // Heuristic function: calculates the Manhattan distance between two points
    private int heuristic(int x, int y, int targetX, int targetY) {
        return Math.abs(x - targetX) + Math.abs(y - targetY);  // Manhattan distance
    }

    // Reconstruct the path from the end node to the start node using parent pointers
    private List<int[]> reconstructPath(GreedyNode endNode) {
        List<int[]> path = new ArrayList<>();
        GreedyNode current = endNode;

        // Traverse from the end node to the start node via parent nodes
        while (current != null) {
            path.add(new int[]{current.x, current.y});  // Add the current node to the path
            current = current.parent;  // Move to the parent node
        }

        Collections.reverse(path);  // Reverse the path to get it from start to end
        return path;
    }

    // GreedyNode class represents a node in the pathfinding process
    private static class GreedyNode {
        int x, y, h;  // Coordinates of the node and its heuristic value
        GreedyNode parent;  // Reference to the parent node

        // Constructor to initialize a GreedyNode with coordinates, heuristic, and parent node
        GreedyNode(int x, int y, int h, GreedyNode parent) {
            this.x = x;
            this.y = y;
            this.h = h;  // Only the heuristic value is considered in Greedy pathfinding
            this.parent = parent;
        }
    }
}
