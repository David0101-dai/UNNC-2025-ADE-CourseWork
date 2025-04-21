package CW1.Manager;

import CW1.MVCPatten.GameConfig;
import CW1.MVCPatten.GameModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TreasureManager {
    private GameModel model;  // Reference to the GameModel to interact with the game state
    private List<int[]> treasures = new ArrayList<>();  // List to store the positions of treasures

    // Constructor to initialize the TreasureManager with the GameModel
    public TreasureManager(GameModel model) {
        this.model = model;  // Initialize the model
    }

    // Method to randomly place treasures on the map
    public void placeTreasures() {
        Random rand = new Random();  // Random number generator for placing treasures
        // Loop to place the specified number of treasures
        for (int i = 0; i < GameConfig.NUM_TREASURES; i++) {
            int x = -1, y = -1;
            boolean isValid = false;

            // Try placing a treasure until a valid position is found
            while (!isValid) {
                x = rand.nextInt(GameConfig.MAP_SIZE);  // Random X coordinate
                y = rand.nextInt(GameConfig.MAP_SIZE);  // Random Y coordinate

                // Ensure the treasure is not too close to the player (at least 5 spaces away)
                if (Math.abs(model.getPlayerX() - x) < 5 || Math.abs(model.getPlayerY() - y) < 5) {
                    continue;  // Try again if the position is too close to the player
                }
                isValid = true;  // Mark as valid once the position is far enough from the player

                // Ensure the treasure is not too close to another treasure (at least 5 spaces away)
                for (int[] treasure : treasures) {
                    if (Math.abs(treasure[0] - x) < 5 && Math.abs(treasure[1] - y) < 5) {
                        isValid = false;  // Mark as invalid if the position is too close to another treasure
                        break;
                    }
                }
            }
            model.getMap()[x][y] = GameConfig.TREASURE;  // Place the treasure on the map
            treasures.add(new int[]{x, y});  // Add the treasure's position to the list
        }
    }

    // Method to retrieve the list of all treasure positions
    public List<int[]> getTreasures() {
        return treasures;  // Return the list of treasure positions
    }

    // Method to remove a treasure from the list when it is collected
    public void removeTreasure(int x, int y) {
        // Iterate through the list of treasures to find the one that matches the coordinates
        for (Iterator<int[]> iterator = treasures.iterator(); iterator.hasNext(); ) {
            int[] treasure = iterator.next();
            // If the treasure matches the coordinates, remove it from the list
            if (treasure[0] == x && treasure[1] == y) {
                iterator.remove();  // Remove the treasure from the list
                break;  // Exit the loop after removing the treasure
            }
        }
    }
}
