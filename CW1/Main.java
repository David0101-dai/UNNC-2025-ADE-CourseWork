package CW1;

import CW1.MVCPatten.GameController;

import javax.swing.Timer;  // Import Timer class
import java.awt.event.ActionEvent;  // Import ActionEvent class for handling timer events

public class Main {
    public static void main(String[] args) {
        // Get the singleton instance of the GameController
        GameController controller = GameController.getInstance();

        // Start the game by initializing the window and the game view
        controller.start();

        // Create a Timer that calls the update method of the GameController every 50 milliseconds
        Timer timer = new Timer(50, (ActionEvent e) -> controller.update());  // Timer with 50ms delay
        timer.start();  // Start the timer to begin periodic updates
    }
}
