package CW1.Manager;

import CW1.MVCPatten.GameConfig;
import CW1.MVCPatten.GameModel;

import java.util.Random;

public class Player {
    private int x, y;
    private GameModel model;

    public Player(GameModel model) {
        this.model = model;
    }

    public void placePlayer() {
        Random rand = new Random();
        while (true) {
            x = rand.nextInt(GameConfig.MAP_SIZE);
            y = rand.nextInt(GameConfig.MAP_SIZE);
            if (model.getMap()[x][y] == GameConfig.EMPTY) {
                break;
            }
        }
        model.getMap()[x][y] = GameConfig.PLAYER;
    }

    public void movePlayer(char direction) {
        int newX = x;
        int newY = y;

        switch (direction) {
            case 'w': newX--; break;
            case 's': newX++; break;
            case 'a': newY--; break;
            case 'd': newY++; break;
        }

        if (newX < 0 || newX >= GameConfig.MAP_SIZE || newY < 0 || newY >= GameConfig.MAP_SIZE) {
            model.getController().getView().setMessage("This is the map boundary. Please go back.");
            model.decreaseScore(1);
        } else if (model.getMap()[newX][newY] == GameConfig.OBSTACLE) {
            model.getController().getView().setMessage("You bumped into an obstacle!");
            model.decreaseScore(10);
        } else if (model.getMap()[newX][newY] == GameConfig.TREASURE){
            model.addScore(10);
            model.playerCollectsTreasure(newX,newY);
            model.getMap()[x][y] = GameConfig.EMPTY;
            x = newX;
            y = newY;
            model.getMap()[x][y] = GameConfig.PLAYER;
        }else {
            model.getMap()[x][y] = GameConfig.EMPTY;
            x = newX;
            y = newY;
            model.getMap()[x][y] = GameConfig.PLAYER;
        }

        model.getController().getView().repaint();  // 更新视图
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

