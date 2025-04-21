package CW1.Manager;

import CW1.MVCPatten.GameConfig;
import CW1.MVCPatten.GameModel;

public class MapManager {
    private char[][] map = new char[GameConfig.MAP_SIZE][GameConfig.MAP_SIZE];  // 2D array to represent the game map
    private boolean[][] obstacleTouched = new boolean[GameConfig.MAP_SIZE][GameConfig.MAP_SIZE];  // 2D array to track if obstacles are touched
    private GameModel model;  // Reference to the GameModel to interact with other game components

    // Constructor to initialize MapManager with a reference to the GameModel
    public MapManager(GameModel model) {
        this.model = model;  // Initialize the model
    }

    // Method to initialize the map, setting all cells to empty and obstacles to their initial state
    public void initializeMap() {
        // Set all cells in the map to empty
        for (int i = 0; i < GameConfig.MAP_SIZE; i++) {
            for (int j = 0; j < GameConfig.MAP_SIZE; j++) {
                map[i][j] = GameConfig.EMPTY;  // Set cell to empty space
                obstacleTouched[i][j] = false;  // Set all obstacles as not touched
            }
        }

        // Initialize all obstacles on the map
        for (int[] obstacle : model.getObstacleManager().getObstacles()) {
            int x = obstacle[0];  // Get the X coordinate of the obstacle
            int y = obstacle[1];  // Get the Y coordinate of the obstacle
            map[x][y] = GameConfig.OBSTACLE;  // Place an obstacle at the position
        }
    }

    // Method to reset the map, clearing all non-player and non-treasure cells
    public void resetMap() {
        // Loop through all the cells in the map
        for (int i = 0; i < GameConfig.MAP_SIZE; i++) {
            for (int j = 0; j < GameConfig.MAP_SIZE; j++) {
                // If the cell is not a player or treasure, reset it to empty
                if (map[i][j] != GameConfig.PLAYER && map[i][j] != GameConfig.TREASURE) {
                    map[i][j] = GameConfig.EMPTY;  // Reset the cell to empty
                }
            }
        }
    }

    // Method to get the current state of the map
    public char[][] getMap() {
        return map;  // Return the map array
    }

    // Method to get the state of obstacles being touched (to track obstacles hit by the player)
    public boolean[][] getObstacleTouched() {
        return obstacleTouched;  // Return the obstacleTouched array
    }
}
