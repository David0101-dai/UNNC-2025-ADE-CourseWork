package CW1.Manager;

import CW1.MVCPatten.GameConfig;
import CW1.MVCPatten.GameModel;
import CW1.PathFinder.PathFinder;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ObstacleManager {
    private GameModel model;  // Reference to the GameModel for interacting with the game state
    private PathFinder pathFinder;  // Pathfinding algorithm to check if the path is clear
    private Set<int[]> obstacles;  // A set to store the positions of obstacles, avoiding duplicates

    // Constructor to initialize the ObstacleManager with a reference to the GameModel and PathFinder
    public ObstacleManager(GameModel model, PathFinder pathFinder) {
        this.model = model;
        this.pathFinder = pathFinder;
        this.obstacles = new HashSet<>();  // Initialize the obstacles set
    }

    // Method to place obstacles randomly on the map
    public void placeObstacles() {
        Random rand = new Random();  // Random number generator
        int placedObstacles = 0;

        // Keep placing obstacles until the desired number of obstacles are placed
        while (placedObstacles < GameConfig.NUM_OBSTACLES) {
            int x = rand.nextInt(GameConfig.MAP_SIZE);  // Random X coordinate
            int y = rand.nextInt(GameConfig.MAP_SIZE);  // Random Y coordinate

            // If the position is empty, place an obstacle there
            if (model.getMap()[x][y] == GameConfig.EMPTY) {
                model.getMap()[x][y] = GameConfig.OBSTACLE;  // Place the obstacle on the map
                obstacles.add(new int[]{x, y});  // Add the obstacle position to the set

                // Check if the path is clear after placing the obstacle
                if (!pathFinder.pathsAreClear()) {
                    model.getMap()[x][y] = GameConfig.EMPTY;  // If the path is blocked, remove the obstacle
                    obstacles.remove(new int[]{x, y});  // Remove the obstacle from the set
                } else {
                    placedObstacles++;  // If the path is clear, increment the count of placed obstacles
                }
            }
        }
    }

    // Method to get the positions of all obstacles
    public Set<int[]> getObstacles() {
        return obstacles;  // Return the set of obstacle positions
    }
}
