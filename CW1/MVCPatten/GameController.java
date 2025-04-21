package CW1.MVCPatten;

import CW1.PathFinder.AStarPathFinder;
import CW1.PathFinder.BFSPathFinder;
import CW1.PathFinder.GreedyPathFinder;
import CW1.PathFinder.PathFinder;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameController {
    private static GameController instance;
    private GameModel model;
    private GameView view;
    private JFrame frame;
    private Map<String, PathFinder> pathFinders;
    private boolean gameOver = false; // 添加游戏结束标志


    private GameController() {
        model = new GameModel(this);
        view = new GameView(model, this);
        initializePathFinders();
    }

    private void initializePathFinders() {
        pathFinders = new HashMap<>();
        pathFinders.put("BFS", new BFSPathFinder(model));
        pathFinders.put("Greedy", new GreedyPathFinder(model));
        pathFinders.put("A*", new AStarPathFinder(model));
    }

    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    public void start() {
        if (frame == null) {
            frame = new JFrame("Treasure Hunt Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            JPanel scorePanel = new JPanel();
            scorePanel.add(view.getScoreLabel());
            frame.add(scorePanel, BorderLayout.NORTH);

            frame.add(view, BorderLayout.CENTER);
            frame.add(createButtonPanel(), BorderLayout.SOUTH);

            frame.pack();
            frame.setVisible(true);
        }
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton hintButton1 = new JButton("Hint 1 (A*)");
        JButton hintButton2 = new JButton("Hint 2 (Greedy)");
        JButton hintButton3 = new JButton("Hint 3 (BFS)");

        hintButton1.setFocusable(false);
        hintButton2.setFocusable(false);
        hintButton3.setFocusable(false);

        hintButton1.addActionListener(e -> handleHintButton("A*"));
        hintButton2.addActionListener(e -> handleHintButton("Greedy"));
        hintButton3.addActionListener(e -> handleHintButton("BFS"));

        buttonPanel.add(hintButton1);
        buttonPanel.add(hintButton2);
        buttonPanel.add(hintButton3);

        return buttonPanel;
    }

    public void update() {
        getView().repaint();
        checkGameOver();
    }

    public void checkGameOver() {
        if (gameOver) return; // 如果游戏已经结束，直接返回

        if (model.isGameOver()) {
            gameOver = true; // 标记游戏已结束
            showWinDialog();
        } else if (model.getScore() < 0) {
            gameOver = true; // 标记游戏已结束
            showLostDialog();
        }
    }

    private void showWinDialog() {
        showGameOverDialog("YOU WIN!\nFinal Score: " + model.getScore());
    }

    private void showLostDialog() {
        showGameOverDialog("YOU LOST!\nFinal Score: " + model.getScore());
    }

    private void showGameOverDialog(String message) {
        Object[] options = {"Restart", "Exit"};
        int choice = JOptionPane.showOptionDialog(null, message, "Game Over",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 0) restartGame();
        else System.exit(0);
    }

    public GameView getView() {
        return view;
    }

    private void restartGame() {
        gameOver = false; // 重置游戏结束标志
        model = new GameModel(this);
        view = new GameView(model, this);
        initializePathFinders();
        frame.getContentPane().removeAll();
        frame.add(view, BorderLayout.CENTER);

        JPanel scorePanel = new JPanel();
        scorePanel.add(view.getScoreLabel());
        frame.add(scorePanel, BorderLayout.NORTH);
        frame.add(createButtonPanel(), BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();
        view.requestFocusInWindow();
    }

    public void handleHintButton(String hintType) {
        if (model.getScore() >= 3) {
            model.decreaseScore(3);
            updateScoreLabel();
            PathFinder finder = pathFinders.get(hintType);
            if (finder != null) {
                String message = finder.getDirectionHint(model.playerX, model.playerY);
                view.setMessage(message);
            }
        } else {
            view.setMessage("Insufficient points to use the hint.");
        }
    }

    private void updateScoreLabel() {
        view.getScoreLabel().setText("Score: " + model.getScore());
        view.repaint();
    }
}