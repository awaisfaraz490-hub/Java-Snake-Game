import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    final int WIDTH = 600;
    final int HEIGHT = 600;
    final int UNIT = 25;
    final int DELAY = 100;

    int[] x = new int[576];
    int[] y = new int[576];

    int bodyParts = 6;
    int score = 0;

    int foodX;
    int foodY;

    char direction = 'R';

    boolean running = false;
    boolean gameOver = false;
    boolean startScreen = true;

    Timer timer;
    Random random;

    public SnakeGame() {

        random = new Random();

        JFrame frame = new JFrame("Snake Game");

        frame.add(this);
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        frame.setVisible(true);

        timer = new Timer(DELAY, this);
    }

    public void startGame() {

        bodyParts = 6;
        score = 0;

        for (int i = 0; i < x.length; i++) {
            x[i] = 0;
            y[i] = 0;
        }

        direction = 'R';
        running = true;
        gameOver = false;

        newFood();
        timer.start();
    }

    public void newFood() {

        foodX = random.nextInt(WIDTH / UNIT) * UNIT;
        foodY = random.nextInt(HEIGHT / UNIT) * UNIT;
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (startScreen) {

            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("SNAKE GAME", 170, 220);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 25));
            g.drawString("Press ENTER to Start", 180, 300);

            return;
        }

        if (gameOver) {

            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("GAME OVER", 150, 250);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString("Score: " + score, 250, 320);

            g.drawString("Press R to Restart", 180, 380);

            return;
        }

        g.setColor(Color.RED);
        g.fillOval(foodX, foodY, UNIT, UNIT);

        for (int i = 0; i < bodyParts; i++) {

            if (i == 0) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(new Color(0, 180, 0));
            }

            g.fillRect(x[i], y[i], UNIT, UNIT);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("Score: " + score, 10, 25);
    }

    public void move() {

        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {

            case 'U':
                y[0] -= UNIT;
                break;

            case 'D':
                y[0] += UNIT;
                break;

            case 'L':
                x[0] -= UNIT;
                break;

            case 'R':
                x[0] += UNIT;
                break;
        }
    }

    public void checkFood() {

        if (x[0] == foodX && y[0] == foodY) {

            bodyParts++;
            score++;

            newFood();
        }
    }

    public void checkCollision() {

        for (int i = bodyParts; i > 0; i--) {

            if (x[0] == x[i] && y[0] == y[i]) {

                running = false;
                gameOver = true;
            }
        }

        if (x[0] < 0 || x[0] >= WIDTH ||
            y[0] < 0 || y[0] >= HEIGHT) {

            running = false;
            gameOver = true;
        }

        if (!running) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {

            move();
            checkFood();
            checkCollision();
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (startScreen && key == KeyEvent.VK_ENTER) {

            startScreen = false;
            startGame();
            repaint();
            return;
        }

        if (gameOver && key == KeyEvent.VK_R) {

            startGame();
            repaint();
            return;
        }

        if (key == KeyEvent.VK_UP && direction != 'D')
            direction = 'U';

        if (key == KeyEvent.VK_DOWN && direction != 'U')
            direction = 'D';

        if (key == KeyEvent.VK_LEFT && direction != 'R')
            direction = 'L';

        if (key == KeyEvent.VK_RIGHT && direction != 'L')
            direction = 'R';
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new SnakeGame());
    }
}