package CW1.Manager;

import CW1.MVCPatten.GameConfig;
import CW1.MVCPatten.GameModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TreasureManager {
    private GameModel model;  // 引用 GameModel
    private List<int[]> treasures = new ArrayList<>();

    public TreasureManager(GameModel model) {
        this.model = model;  // 在构造函数中初始化 model
    }

    public void placeTreasures() {
        Random rand = new Random();
        for (int i = 0; i < GameConfig.NUM_TREASURES; i++) {
            int x = -1, y = -1;
            boolean isValid = false;
            while (!isValid) {
                x = rand.nextInt(GameConfig.MAP_SIZE);
                y = rand.nextInt(GameConfig.MAP_SIZE);
                if (Math.abs(model.getPlayerX() - x) < 5 || Math.abs(model.getPlayerY() - y) < 5) {
                    continue;
                }
                isValid = true;
                for (int[] treasure : treasures) {
                    if (Math.abs(treasure[0] - x) < 5 && Math.abs(treasure[1] - y) < 5) {
                        isValid = false;
                        break;
                    }
                }
            }
            model.getMap()[x][y] = GameConfig.TREASURE;
            treasures.add(new int[]{x, y});
        }
    }

    public List<int[]> getTreasures() {
        return treasures;
    }

    // 当宝藏被吃掉时，移除宝藏
    public void removeTreasure(int x, int y) {
        for (Iterator<int[]> iterator = treasures.iterator(); iterator.hasNext(); ) {
            int[] treasure = iterator.next();
            if (treasure[0] == x && treasure[1] == y) {
                iterator.remove();  // 从列表中移除宝藏
                break;
            }
        }
    }
}



