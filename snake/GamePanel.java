package beginner;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

public class GamePanel extends JPanel implements KeyListener {

    private int x = 240;
    private int y = 240;
    private int size = 20;
    private int direction = KeyEvent.VK_UP;
    private int oppositeDirection = KeyEvent.VK_DOWN;
    private Timer timer;

    private int[] snakeX = new int[100];
    private int[] snakeY = new int[100];
    private int snakeLength = 3;
    private final int MAX_LENGTH = 100;

    private Random random = new Random();
    private int appleX = 100;
    private int appleY = 100;

    private JLabel gameOverLabel;
    private JButton restartButton;

    private int score = 0;
    private JLabel scoreLabel;



    public GamePanel() {
        this.setFocusable(true);
        this.addKeyListener(this);

        setLayout(null); 
        gameOverLabel = new JLabel("Game Over!");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gameOverLabel.setOpaque(true);
        gameOverLabel.setBackground(Color.WHITE);  
        gameOverLabel.setForeground(Color.BLACK);  
        gameOverLabel.setBorder(new LineBorder(Color.WHITE, 3, true));
        gameOverLabel.setHorizontalAlignment(SwingConstants.CENTER);

        int labelWidth = 250;
        int labelHeight = 50;
        gameOverLabel.setBounds((500 - labelWidth) / 2, (500 - labelHeight) / 2, labelWidth, labelHeight);
        gameOverLabel.setVisible(false);
        add(gameOverLabel);

        restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Arial", Font.BOLD, 14));
        restartButton.setBounds(150, 275, 200, 30); 
        restartButton.setVisible(false);
        add(restartButton);

        restartButton.addActionListener(e -> resetGame());

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        scoreLabel.setBounds(10, 10, 100, 20);
        add(scoreLabel);


        snakeX = new int[MAX_LENGTH];
        snakeY = new int[MAX_LENGTH];
        snakeX[0] = 240;
        snakeY[0] = 240;

        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                
                for(int i = snakeLength; i > 0; i--) {
                    snakeX[i] = snakeX[i-1];
                    snakeY[i] = snakeY[i-1];
                }

                switch (direction) {
                    case KeyEvent.VK_UP: snakeY[0] -= 10; break;
                    case KeyEvent.VK_DOWN: snakeY[0] += 10; break;
                    case KeyEvent.VK_LEFT: snakeX[0] -= 10; break;
                    case KeyEvent.VK_RIGHT: snakeX[0] += 10; break;
                }

            if (snakeX[0] < 0 || snakeX[0] > getWidth() - size || snakeY[0] < 0 || snakeY[0] > getHeight() - size) {
                gameOver();
            }

            for(int i = 1; i < snakeLength; i++) {
                if(snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                    gameOver();
                    break;
                }
            } 

            if((snakeX[0] == appleX || snakeX[0] + (size/2) == appleX || snakeX[0] - (size/2) == appleX) && (snakeY[0] == appleY || snakeY[0] + (size/2) == appleY || snakeY[0] - (size/2) == appleY)) {
                int newDelay = Math.max(50, 100 - (snakeLength - 3) * 2); 
                timer.setDelay(newDelay);

                snakeLength++;
                generateApple();
                score++;
                scoreLabel.setText("Score: " + score);
            }
                repaint();
            }
        });
        timer.start();
    }


    @Override
    protected void paintComponent(Graphics g) {        
        super.paintComponent(g);
        g.setColor(Color.RED);
        for(int i = 0; i < snakeLength; i++) {
            g.fillRect(snakeX[i], snakeY[i], size, size);
        }
        paintApple(g);
    }


    protected void setOppositeDirection() {
        switch (this.direction) {
            case KeyEvent.VK_UP: this.oppositeDirection = KeyEvent.VK_DOWN; break;
            case KeyEvent.VK_DOWN: this.oppositeDirection = KeyEvent.VK_UP; break;
            case KeyEvent.VK_LEFT: this.oppositeDirection = KeyEvent.VK_RIGHT; break;
            case KeyEvent.VK_RIGHT: this.oppositeDirection = KeyEvent.VK_LEFT; break;
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() != this.direction && e.getKeyCode() != this.oppositeDirection) {
            this.direction = e.getKeyCode();
            setOppositeDirection();
        }

        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }

        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            // System.out.println(getWidth() + " : " + getHeight());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}


    // APPLE LOGIC
    private void paintApple(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(appleX, appleY, size, size);
    }

    private void generateApple() {
        int cols = getWidth() / size;
        int rows = getHeight() / size;
        appleX = random.nextInt(0, cols) * size;
        appleY = random.nextInt(0, rows) * size;
    }

        private void resetGame() {
        snakeLength = 3;
        snakeX[0] = 240;
        snakeY[0] = 240;

        for(int i = 0; i < snakeLength; i++) {
            snakeX[i] = 240;
            snakeY[i] = 240;
        }

        score = 0;
        scoreLabel.setText("Score: 0");

        timer.setDelay(100);
        direction = KeyEvent.VK_UP;
        oppositeDirection = KeyEvent.VK_DOWN;
        gameOverLabel.setVisible(false);
        restartButton.setVisible(false);
        timer.start();
        generateApple();
        repaint();
    }

    private void gameOver() {
        timer.stop();
        gameOverLabel.setText("<html> Game Over! <br>Final Score: " + score + "</html>");
        gameOverLabel.setVisible(true);
        restartButton.setVisible(true);
    }
}
