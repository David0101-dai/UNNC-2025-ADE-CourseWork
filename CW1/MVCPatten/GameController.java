package CW1.MVCPatten;

import CW1.Manager.Player;
import CW1.PathFinder.AStarPathFinder;
import CW1.PathFinder.BFSPathFinder;
import CW1.PathFinder.GreedyPathFinder;
import CW1.PathFinder.PathFinder;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameController {
    private static GameController instance;  // Singleton instance of GameController
    private GameModel model;  // The model of the game
    private GameView view;  // The view for displaying the game
    private JFrame frame;  // The window frame
    private Map<String, PathFinder> pathFinders;  // A map of available pathfinders (BFS, Greedy, A*)
    private boolean gameOver = false;  // Flag to check if the game is over

    // Private constructor for singleton pattern
    private GameController() {
        model = new GameModel(this);  // Initialize the game model
        view = new GameView(model, this);  // Initialize the game view
        initializePathFinders();  // Initialize pathfinders (BFS, Greedy, A*)
    }

    // Initialize different pathfinders for different algorithms
    private void initializePathFinders() {
        pathFinders = new HashMap<>();  // Create a new HashMap for storing pathfinders
        pathFinders.put("BFS", new BFSPathFinder(model));  // Add BFS pathfinder
        pathFinders.put("Greedy", new GreedyPathFinder(model));  // Add Greedy pathfinder
        pathFinders.put("A*", new AStarPathFinder(model));  // Add A* pathfinder
    }

    // Get the instance of the controller (Singleton pattern)
    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();  // Create the instance if it's not created yet
        }
        return instance;
    }

    // Start the game by initializing the JFrame and adding the game view
    public void start() {
        if (frame == null) {
            frame = new JFrame("Treasure Hunt Game");  // Create a new frame
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Close the game on exit
            frame.setLayout(new BorderLayout());  // Set the layout of the frame

            // Create a score panel to display the score
            JPanel scorePanel = new JPanel();
            scorePanel.add(view.getScoreLabel());  // Add score label to score panel
            frame.add(scorePanel, BorderLayout.NORTH);  // Add score panel to the top

            // Add the game view in the center of the frame
            frame.add(view, BorderLayout.CENTER);
            frame.add(createButtonPanel(), BorderLayout.SOUTH);  // Add hint buttons at the bottom

            frame.pack();  // Adjust the window size to fit the content
            frame.setVisible(true);  // Make the frame visible
        }
    }

    // Create the button panel with hint buttons
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();  // Create a new panel for buttons
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));  // Layout for buttons to the left

        // Create buttons for the different pathfinding algorithms
        JButton hintButton1 = new JButton("Hint 1 (A*)");
        JButton hintButton2 = new JButton("Hint 2 (Greedy)");
        JButton hintButton3 = new JButton("Hint 3 (BFS)");

        hintButton1.setFocusable(false);  // Disable focus for the button
        hintButton2.setFocusable(false);
        hintButton3.setFocusable(false);

        // Set up the action listeners for the buttons
        hintButton1.addActionListener(e -> handleHintButton("A*"));
        hintButton2.addActionListener(e -> handleHintButton("Greedy"));
        hintButton3.addActionListener(e -> handleHintButton("BFS"));

        // Add buttons to the button panel
        buttonPanel.add(hintButton1);
        buttonPanel.add(hintButton2);
        buttonPanel.add(hintButton3);

        return buttonPanel;  // Return the button panel
    }

    // Update the game view and score label
    public void update() {
        updateScoreLabel();  // Update the score label
        getView().repaint();  // Repaint the game view
        checkGameOver();  // Check if the game is over
    }

    // Check if the game is over
    public void checkGameOver() {
        if (gameOver) return;  // If the game is already over, do nothing

        if (model.isGameOver()) {
            gameOver = true;  // Mark the game as over
            showWinDialog();  // Show win dialog if the game is won
        } else if (model.getScore() <= 0) {
            gameOver = true;  // Mark the game as over
            showLostDialog();  // Show lost dialog if the score is 0 or below
        }
    }

    // Show the win dialog if the player wins
    private void showWinDialog() {
        showGameOverDialog("YOU WIN!\nFinal Score: " + model.getScore());
    }

    // Show the lost dialog if the player loses
    private void showLostDialog() {
        showGameOverDialog("YOU LOST!\nFinal Score: " + model.getScore());
    }

    // Show the game over dialog with restart and exit options
    private void showGameOverDialog(String message) {
        Object[] options = {"Restart", "Exit"};
        int choice = JOptionPane.showOptionDialog(null, message, "Game Over",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 0) restartGame();  // Restart the game if "Restart" is clicked
        else System.exit(0);  // Exit the game if "Exit" is clicked
    }

    // Get the game view
    public GameView getView() {
        return view;
    }

    // Restart the game by resetting all game state
    private void restartGame() {
        gameOver = false;  // Reset the game over flag

        // Reinitialize the model and view
        model = new GameModel(this);
        view = new GameView(model, this);
        initializePathFinders();  // Reinitialize pathfinders

        // Clear the old view and add the new view
        frame.getContentPane().removeAll();
        frame.add(view, BorderLayout.CENTER);

        // Update the score panel
        JPanel scorePanel = new JPanel();
        scorePanel.add(view.getScoreLabel());
        frame.add(scorePanel, BorderLayout.NORTH);
        frame.add(createButtonPanel(), BorderLayout.SOUTH);

        // Force refresh of the window
        frame.revalidate();
        frame.repaint();  // Ensure the view is redrawn
        view.requestFocusInWindow();  // Focus on the view
    }

    // Handle the hint buttons for different pathfinding algorithms
    public void handleHintButton(String hintType) {
        if (model.getScore() >= 3) {  // Check if there are enough points to use a hint
            model.decreaseScore(3);  // Deduct 3 points for using the hint
            updateScoreLabel();  // Update the score label
            PathFinder finder = pathFinders.get(hintType);  // Get the corresponding pathfinder
            if (finder != null) {
                String message = finder.getDirectionHint(model.getPlayerX(), model.getPlayerY());  // Get the hint message
                view.setMessage(message);  // Display the hint message in the view
            }
        } else {
            view.setMessage("Insufficient points to use the hint.");  // Display a message if there are not enough points
        }
    }

    // Update the score label in the view
    private void updateScoreLabel() {
        view.getScoreLabel().setText("Score: " + model.getScore());  // Update the score label with the current score
        view.repaint();  // Repaint the view to update the display
    }
}
