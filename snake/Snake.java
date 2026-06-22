package snake;

import javax.swing.JFrame;

public class Snake {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake");
        frame.setLocation(400, 100);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel panel = new GamePanel();
        frame.setContentPane(panel);
        frame.setVisible(true);

    }
}