package CW1.MVCPatten;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameView extends JPanel {
    private GameModel model;
    private JLabel scoreLabel;
    private GameController controller;

    private String message = "";
    private long messageTime = 0;
    private static final long MESSAGE_DURATION = 2000;

    public GameView(GameModel model, GameController controller) {
        this.model = model;
        this.controller = controller;

        setPreferredSize(new Dimension(GameConfig.VIEW_SIZE * GameConfig.TILE_SIZE, GameConfig.VIEW_SIZE * GameConfig.TILE_SIZE));
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());  // 使用 BorderLayout 来布局

        // 创建积分面板
        scoreLabel = new JLabel("Score: " + model.getScore());
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel scorePanel = new JPanel();
        scorePanel.add(scoreLabel);

        // 监听键盘事件
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                char direction = ' ';
                if (e.getKeyCode() == KeyEvent.VK_W) direction = 'w';
                else if (e.getKeyCode() == KeyEvent.VK_S) direction = 's';
                else if (e.getKeyCode() == KeyEvent.VK_A) direction = 'a';
                else if (e.getKeyCode() == KeyEvent.VK_D) direction = 'd';
                model.movePlayer(direction);
                scoreLabel.setText("Score: " + model.getScore());
                repaint();  // 更新视图

                controller.checkGameOver();
            }
        });

        setFocusable(true);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        char[][] map = model.getMap();
        boolean[][] obstacleTouched = model.getObstacleTouched();

        // 绘制地图
        for (int i = 0; i < GameConfig.VIEW_SIZE; i++) {
            for (int j = 0; j < GameConfig.VIEW_SIZE; j++) {
                int x = j * GameConfig.TILE_SIZE;
                int y = i * GameConfig.TILE_SIZE;

                if (map[i][j] == GameConfig.EMPTY) {
                    g.setColor(Color.WHITE);
                } else if (map[i][j] == GameConfig.OBSTACLE) {
                    if (obstacleTouched[i][j]) {
                        g.setColor(Color.BLACK);  // 被触碰的障碍物
                    } else {
                        g.setColor(Color.GRAY);  // 未触碰的障碍物
                    }
                } else if (map[i][j] == GameConfig.TREASURE) {
                    // 如果宝藏距离玩家较近，变成黄色
                    int distance = Math.abs(i - model.playerX) + Math.abs(j - model.playerY);
                    if (distance <= 3) {  // 3格以内视为靠近
                        g.setColor(Color.YELLOW);
                    } else {
                        g.setColor(Color.WHITE);  // 初始状态为白色
                    }
                } else if (map[i][j] == GameConfig.PLAYER) {
                    g.setColor(Color.BLUE);
                }

                g.fillRect(x, y, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
            }
        }

        if (!message.isEmpty()) {
            long elapsedTime = System.currentTimeMillis() - messageTime;
            if (elapsedTime < MESSAGE_DURATION) {
                messageAlpha = Math.max(255 - (int)(elapsedTime / 8), 0);  // 让消息渐渐变淡
                g.setFont(new Font("Arial", Font.BOLD, 24));
                g.setColor(new Color(255, 0, 0, messageAlpha));

                FontMetrics metrics = g.getFontMetrics();
                int x = (getWidth() - metrics.stringWidth(message)) / 2;
                int y = getHeight() / 2;
                g.drawString(message, x, y);
            } else {
                message = "";
            }
        }
    }

    public void setMessage(String msg) {
        message = msg;
        messageTime = System.currentTimeMillis();
        messageAlpha = 255;  // 每次设置消息时，恢复完全不透明
    }

    private int messageAlpha = 255;  // 初始消息透明度为完全不透明

    public JLabel getScoreLabel() {
        return scoreLabel;
    }

}
