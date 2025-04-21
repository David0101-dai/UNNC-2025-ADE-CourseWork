package CW1.MVCPatten;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameView extends JPanel {
    private GameModel model;  // The game model
    private JLabel scoreLabel;  // Label to display the player's score
    private GameController controller;  // The game controller
    private String message = "";  // The message to be displayed
    private long messageTime = 0;  // Time when the message was displayed

    // Constructor for initializing the game view
    public GameView(GameModel model, GameController controller) {
        this.model = model;
        this.controller = controller;

        // Set the preferred size of the view based on the configuration
        setPreferredSize(new Dimension(GameConfig.VIEW_SIZE * GameConfig.TILE_SIZE, GameConfig.VIEW_SIZE * GameConfig.TILE_SIZE));
        setBackground(Color.WHITE);  // Set the background color to white
        setLayout(new BorderLayout());  // Use BorderLayout for layout management

        // Create a score panel to display the player's score
        scoreLabel = new JLabel("Score: " + model.getScore());
        scoreLabel.setFont(GameConfig.SCORE_FONT);  // Set the font for the score label
        JPanel scorePanel = new JPanel();  // Panel to hold the score label
        scorePanel.add(scoreLabel);

        // Listen for key press events (W, A, S, D) for player movement
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                char direction = ' ';  // Variable to hold the direction for movement
                if (e.getKeyCode() == KeyEvent.VK_W) direction = 'w';  // W key for up
                else if (e.getKeyCode() == KeyEvent.VK_S) direction = 's';  // S key for down
                else if (e.getKeyCode() == KeyEvent.VK_A) direction = 'a';  // A key for left
                else if (e.getKeyCode() == KeyEvent.VK_D) direction = 'd';  // D key for right
                model.movePlayer(direction);  // Move the player in the specified direction
                scoreLabel.setText("Score: " + model.getScore());  // Update the score label
                repaint();  // Repaint the view to reflect the updated game state

                controller.checkGameOver();  // Check if the game is over
            }
        });

        setFocusable(true);  // Make the panel focusable to listen for key events
    }

    // Paint the game view (the map and player)
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        char[][] map = model.getMap();  // Get the game map
        boolean[][] obstacleTouched = model.getObstacleTouched();  // Get the obstacle touch states

        // Draw the game map
        for (int i = 0; i < GameConfig.VIEW_SIZE; i++) {
            for (int j = 0; j < GameConfig.VIEW_SIZE; j++) {
                int x = j * GameConfig.TILE_SIZE;  // X position of the tile
                int y = i * GameConfig.TILE_SIZE;  // Y position of the tile

                // Draw the empty spaces
                if (map[i][j] == GameConfig.EMPTY) {
                    g.setColor(GameConfig.EMPTY_COLOR);  // Set color for empty tiles
                }
                // Draw obstacles
                else if (map[i][j] == GameConfig.OBSTACLE) {
                    if (obstacleTouched[i][j]) {
                        g.setColor(GameConfig.TOUCHED_OBSTACLE_COLOR);  // Set color for touched obstacles
                    } else {
                        g.setColor(GameConfig.OBSTACLE_COLOR);  // Set color for normal obstacles
                    }
                }
                // Draw treasures
                else if (map[i][j] == GameConfig.TREASURE) {
                    // If treasure is close to the player, make it yellow
                    int distance = Math.abs(i - model.getPlayerX()) + Math.abs(j - model.getPlayerY());
                    if (distance <= 3) {  // If within 3 tiles, consider it "close"
                        g.setColor(GameConfig.TREASURE_COLOR);
                    } else {
                        g.setColor(GameConfig.EMPTY_COLOR);  // Default color for treasures
                    }
                }
                // Draw the player
                else if (map[i][j] == GameConfig.PLAYER) {
                    g.setColor(GameConfig.PLAYER_COLOR);  // Set color for the player
                }

                // Draw the filled rectangle for each tile
                g.fillRect(x, y, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
                g.setColor(Color.BLACK);  // Set border color to black
                g.drawRect(x, y, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);  // Draw the tile border
            }
        }

        // Display any messages (e.g., game win/loss)
        if (!message.isEmpty()) {
            long elapsedTime = System.currentTimeMillis() - messageTime;  // Calculate elapsed time since message was shown
            if (elapsedTime < GameConfig.MESSAGE_DURATION) {  // If the message should still be displayed
                messageAlpha = Math.max(255 - (int)(elapsedTime / 8), 0);  // Gradually fade the message
                g.setFont(GameConfig.MESSAGE_FONT);  // Set the font for the message
                g.setColor(new Color(255, 0, 0, messageAlpha));  // Set color with fading alpha

                // Center the message on the screen
                FontMetrics metrics = g.getFontMetrics();
                int x = (getWidth() - metrics.stringWidth(message)) / 2;
                int y = getHeight() / 2;
                g.drawString(message, x, y);  // Draw the message
            } else {
                message = "";  // Clear the message if it has been displayed for long enough
            }
        }
    }

    // Set a new message to display on the screen
    public void setMessage(String msg) {
        message = msg;
        messageTime = System.currentTimeMillis();  // Record the time when the message was set
        messageAlpha = 255;  // Reset message opacity to fully visible
    }

    private int messageAlpha = 255;  // Initial message opacity (fully opaque)

    // Get the score label
    public JLabel getScoreLabel() {
        return scoreLabel;
    }
}
