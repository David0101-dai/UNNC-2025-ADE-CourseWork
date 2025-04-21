package CW1.MVCPatten;

import java.awt.*;

public class GameConfig {
    public static final int MAP_SIZE = 20;
    public static final int VIEW_SIZE = 20;
    public static final int NUM_TREASURES = 3;
    public static final int NUM_OBSTACLES = 80;
    public static final int TILE_SIZE = 30;
    public static final long MESSAGE_DURATION = 2000;

    public static final char EMPTY = ' ';
    public static final char OBSTACLE = 'O';
    public static final char TREASURE = 'T';
    public static final char PLAYER = 'P';

    public static final Color PLAYER_COLOR = Color.BLUE;
    public static final Color OBSTACLE_COLOR = Color.GRAY;
    public static final Color TOUCHED_OBSTACLE_COLOR = Color.BLACK;
    public static final Color TREASURE_COLOR = Color.YELLOW;
    public static final Color MESSAGE_COLOR = new Color(255, 0, 0, 255);

    public static final Font SCORE_FONT = new Font("Arial", Font.BOLD, 16);
    public static final Font MESSAGE_FONT = new Font("Arial", Font.BOLD, 24);
}