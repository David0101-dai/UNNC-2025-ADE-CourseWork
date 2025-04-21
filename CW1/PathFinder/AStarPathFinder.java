package CW1.PathFinder;

import CW1.MVCPatten.GameConfig;
import CW1.MVCPatten.GameModel;

import java.util.*;

public class AStarPathFinder extends PathFinder {
    // Constructor: Inherit from PathFinder and initialize the model
    public AStarPathFinder(GameModel model) {
        super(model);
    }

    // Find the path to the nearest treasure using A* algorithm
    @Override
    public List<int[]> findPathToNearestTreasure(int playerX, int playerY) {
        // Find the nearest treasure based on the player's position
        int[] nearestTreasure = findNearestTreasure(playerX, playerY, model.getTreasures());
        // Use the A* algorithm to find the path to the nearest treasure
        return findPathAStar(playerX, playerY, nearestTreasure[0], nearestTreasure[1]);
    }

    // Check if paths are clear (not implemented here, can be extended in subclasses)
    @Override
    public boolean pathsAreClear() {
        return false; // Not implemented
    }

    // A* pathfinding algorithm to find the optimal path from start to target
    private List<int[]> findPathAStar(int startX, int startY, int targetX, int targetY) {
        // Priority queue to store nodes, ordered by their total cost (f = g + h)
        PriorityQueue<AStarNode> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        Map<String, AStarNode> allNodes = new HashMap<>();  // To store all nodes for path reconstruction

        // Initialize the start node with its g (cost) and h (heuristic) values
        AStarNode startNode = new AStarNode(startX, startY, 0, heuristic(startX, startY, targetX, targetY), null);
        openSet.add(startNode);  // Add the start node to the open set
        allNodes.put(startX + "," + startY, startNode);  // Store start node in the map

        // Directions array for moving up, right, down, left
        int[] directions = {-1, 0, 1, 0, 0, -1, 0, 1};

        // While there are nodes to explore
        while (!openSet.isEmpty()) {
            AStarNode current = openSet.poll();  // Get the node with the lowest f value

            // If the current node is the target, reconstruct and return the path
            if (current.x == targetX && current.y == targetY) {
                return reconstructPath(allNodes, current);
            }

            // Explore neighboring nodes
            for (int i = 0; i < 4; i++) {
                int newX = current.x + directions[i * 2];  // Calculate the new X coordinate
                int newY = current.y + directions[i * 2 + 1];  // Calculate the new Y coordinate

                // Skip invalid moves or out-of-bounds nodes
                if (!isValidMove(newX, newY)) continue;

                // Calculate the g value (cost) for the new node
                int newG = current.g + 1;
                String key = newX + "," + newY;  // Create a unique key for the node

                // If the new node is not in the map or has a lower g value, add it to the open set
                if (!allNodes.containsKey(key) || newG < allNodes.get(key).g) {
                    int newH = heuristic(newX, newY, targetX, targetY);  // Calculate the heuristic (h)
                    AStarNode newNode = new AStarNode(newX, newY, newG, newH, current);  // Create a new node
                    allNodes.put(key, newNode);  // Store the new node in the map
                    openSet.add(newNode);  // Add the new node to the open set
                }
            }
        }
        return null;  // Return null if no path is found
    }

    // Heuristic function: calculates the Manhattan distance between two points
    private int heuristic(int x, int y, int targetX, int targetY) {
        int distance = Math.abs(x - targetX) + Math.abs(y - targetY);  // Manhattan distance
        // Add a penalty if the node is near the map edges
        if (x <= 1 || x >= GameConfig.MAP_SIZE - 2 || y <= 1 || y >= GameConfig.MAP_SIZE - 2) {
            distance += 1;  // Increase the distance for edges
        }
        return distance;  // Return the heuristic value
    }

    // Reconstruct the path from the end node to the start node using parent pointers
    private List<int[]> reconstructPath(Map<String, AStarNode> allNodes, AStarNode endNode) {
        List<int[]> path = new ArrayList<>();
        AStarNode current = endNode;
        // Traverse from the end node to the start node via parent nodes
        while (current != null) {
            path.add(new int[]{current.x, current.y});  // Add the current node to the path
            current = current.parent;  // Move to the parent node
        }
        Collections.reverse(path);  // Reverse the path to get it from start to end
        return path;  // Return the final path
    }

    // AStarNode class represents a node in the A* pathfinding algorithm
    private static class AStarNode {
        int x, y, g, f;  // Coordinates of the node, g (cost), f (total cost)
        AStarNode parent;  // Parent node for path reconstruction

        // Constructor to initialize an AStarNode with coordinates, g, h, and parent
        AStarNode(int x, int y, int g, int h, AStarNode parent) {
            this.x = x;
            this.y = y;
            this.g = g;  // Cost to reach this node
            this.f = g + h;  // Total cost (f = g + h)
            this.parent = parent;
        }
    }
}
