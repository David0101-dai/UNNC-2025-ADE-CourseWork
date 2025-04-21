package CW1.Manager;

import CW1.MVCPatten.GameConfig;
import CW1.MVCPatten.GameModel;

import java.util.Random;

public class Player {
    private int x, y;  // Player's coordinates on the map
    private GameModel model;  // Reference to the GameModel to access game state

    // Constructor to initialize the Player with the GameModel
    public Player(GameModel model) {
        this.model = model;
    }

    // Place the player on the map at a random empty location
    public void placePlayer() {
        Random rand = new Random();  // Random number generator
        while (true) {
            x = rand.nextInt(GameConfig.MAP_SIZE);  // Random X coordinate
            y = rand.nextInt(GameConfig.MAP_SIZE);  // Random Y coordinate
            if (model.getMap()[x][y] == GameConfig.EMPTY) {  // Check if the spot is empty
                break;  // Break the loop if an empty spot is found
            }
        }
        model.getMap()[x][y] = GameConfig.PLAYER;  // Place the player on the map at the chosen spot
    }

    // Move the player in the specified direction
    public void movePlayer(char direction) {
        int newX = x;  // Start with the current X coordinate
        int newY = y;  // Start with the current Y coordinate

        // Update the new position based on the direction
        switch (direction) {
            case 'w': newX--; break;  // Move up
            case 's': newX++; break;  // Move down
            case 'a': newY--; break;  // Move left
            case 'd': newY++; break;  // Move right
        }

        // Check if the new position is out of bounds
        if (newX < 0 || newX >= GameConfig.MAP_SIZE || newY < 0 || newY >= GameConfig.MAP_SIZE) {
            model.getController().getView().setMessage("This is the map boundary. Please go back.");
            model.decreaseScore(1);  // Penalize the player for hitting the boundary
        }
        // Check if the new position is an obstacle
        else if (model.getMap()[newX][newY] == GameConfig.OBSTACLE) {
            model.getController().getView().setMessage("You bumped into an obstacle!");
            if(model.getMapManager().getObstacleTouched()[newX][newY] == false) {
                model.decreaseScore(10);  // Penalize the player for hitting an obstacle
                // Update the obstacleTouched array to mark the obstacle as touched
                model.getMapManager().getObstacleTouched()[newX][newY] = true;
            }
        }
        // Check if the new position is a treasure
        else if (model.getMap()[newX][newY] == GameConfig.TREASURE){
            model.addScore(10);  // Increase the score for collecting the treasure
            model.playerCollectsTreasure(newX, newY);  // Collect the treasure and update the map
            model.getMap()[x][y] = GameConfig.EMPTY;  // Clear the old player position
            x = newX;  // Update the player's X coordinate
            y = newY;  // Update the player's Y coordinate
            model.getMap()[x][y] = GameConfig.PLAYER;  // Place the player at the new position
        }
        // If the player moves to an empty space
        else {
            model.decreaseScore(1);  // Penalize the player for moving to an empty space
            model.getMap()[x][y] = GameConfig.EMPTY;  // Clear the old player position
            x = newX;  // Update the player's X coordinate
            y = newY;  // Update the player's Y coordinate
            model.getMap()[x][y] = GameConfig.PLAYER;  // Place the player at the new position
        }

        model.getController().getView().repaint();  // Repaint the game view to reflect the updated state
    }

    // Getter method for the player's X coordinate
    public int getX() {
        return x;
    }

    // Getter method for the player's Y coordinate
    public int getY() {
        return y;
    }
}
