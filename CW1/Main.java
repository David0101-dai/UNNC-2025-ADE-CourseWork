package CW1;

import CW1.MVCPatten.GameController;

import javax.swing.Timer;  // 导入Timer类
import java.awt.event.ActionEvent;  // 导入ActionEvent类

public class Main {
    public static void main(String[] args) {
        GameController controller = GameController.getInstance();
        controller.start();
        // 定时调用 update 方法，周期为 50ms
        Timer timer = new Timer(50, (ActionEvent e) -> controller.update());
        timer.start();  // 启动定时器
    }
}
