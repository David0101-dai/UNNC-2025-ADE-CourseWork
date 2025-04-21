package CW1.MVCPatten;

import CW1.Manager.MapManager;
import CW1.Manager.ObstacleManager;
import CW1.Manager.Player;
import CW1.Manager.TreasureManager;
import CW1.PathFinder.BFSPathFinder;
import CW1.PathFinder.PathFinder;

import java.util.List;

public class GameModel {
    private GameController controller;  // The game controller that handles game logic
    private MapManager mapManager;  // The manager for handling the game map
    private Player player;  // The player object in the game
    private TreasureManager treasureManager;  // The manager for handling treasures
    private ObstacleManager obstacleManager;  // The manager for handling obstacles
    private PathFinder pathFinder;  // The pathfinding algorithm (currently using BFS)

    private int score = 100;  // Starting score of the player

    // Constructor for the GameModel. Initializes all managers and the pathfinding algorithm.
    public GameModel(GameController controller) {
        this.controller = controller;
        this.pathFinder = new BFSPathFinder(this);  // Using BFS pathfinder by default

        mapManager = new MapManager(this);
        player = new Player(this);
        treasureManager = new TreasureManager(this);
        obstacleManager = new ObstacleManager(this, pathFinder);

        initializeGame();  // Initializes the game by setting up map, player, treasures, and obstacles
    }

    // Initializes the game by placing the player, treasures, and obstacles.
    private void initializeGame() {
        mapManager.initializeMap();  // Initializes the game map
        player.placePlayer();  // Places the player at a random location
        treasureManager.placeTreasures();  // Places treasures on the map
        obstacleManager.placeObstacles();  // Places obstacles on the map
        ensurePaths();  // Ensures there are valid paths for the player to walk
    }

    // Retrieves the list of treasures on the map
    public List<int[]> getTreasures() {
        return treasureManager.getTreasures();
    }

    // Ensures there is a valid path for the player to reach all treasures.
    private void ensurePaths() {
        int retries = 0;
        // Try 100 times to ensure the paths are clear
        while (!pathFinder.pathsAreClear() && retries < 100) {
            resetMap();  // Reset the map to try again
            player.placePlayer();  // Replaces the player in a new location
            treasureManager.placeTreasures();  // Replaces the treasures
            obstacleManager.placeObstacles();  // Replaces the obstacles
            retries++;
        }

        if (retries == 100) {
            System.out.println("Unable to ensure paths after 100 retries.");
        }
    }

    // Resets the map to an empty state
    private void resetMap() {
        mapManager.resetMap();
    }

    // Moves the player in the given direction and updates the game state
    public void movePlayer(char direction) {
        player.movePlayer(direction);
        controller.update();  // Update the controller to refresh the view and game state
    }

    // Returns the current score of the player
    public int getScore() {
        return score;
    }

    // Retrieves the current game map
    public char[][] getMap() {
        return mapManager.getMap();
    }

    // Checks if the game is over (when all treasures are collected)
    public boolean isGameOver() {
        return treasureManager.getTreasures().isEmpty();
    }

    // Returns the game controller instance
    public GameController getController() {
        return controller;
    }

    // Decreases the player's score by a given number of points
    public void decreaseScore(int points) {
        score -= points;
        if (score < 0) score = 0;  // Ensure the score doesn't go below zero
    }

    // Gets the player's current X coordinate
    public int getPlayerX() {
        return player.getX();  // Assume Player class has a getX method
    }

    // Gets the player's current Y coordinate
    public int getPlayerY() {
        return player.getY();  // Assume Player class has a getY method
    }

    // Returns the map showing which obstacles have been touched by the player
    public boolean[][] getObstacleTouched() {
        return mapManager.getObstacleTouched();  // Returns obstacle touched array from MapManager
    }

    // Handles the player collecting a treasure at the given coordinates
    public void playerCollectsTreasure(int x, int y) {
        treasureManager.removeTreasure(x, y);  // Removes the collected treasure
        this.getMap()[x][y] = GameConfig.EMPTY;  // Updates the map to reflect the collected treasure
    }

    // Adds points to the player's score
    public void addScore(int points) {
        score += points;
    }

    // Returns the MapManager instance
    public MapManager getMapManager() {
        return mapManager;
    }

    // Returns the TreasureManager instance
    public TreasureManager getTreasureManager() {
        return treasureManager;
    }

    // Returns the ObstacleManager instance
    public ObstacleManager getObstacleManager() {
        return obstacleManager;
    }
}
