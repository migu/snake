package de.cgan.games.snakes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.Optional;
import java.util.Random;

/**
 * @author michael.gutbier@codecentric.de
 */
public class Main extends JFrame implements ActionListener {
    private static final Dimension FIELD_SIZE = new Dimension(75, 75);

    private static final int CELL_SIZE = 8;

    private static final int DELAY = 200;

    private static final int INITIAL_SNAKE_SIZE = 5;

    public static void main(String... args) {
        SwingUtilities.invokeLater(Main::new);
    }

    private BufferStrategy bs;

    private final Timer timer;

    private final Random positionRandom = new Random(System.currentTimeMillis());

    private boolean running;

    private Snake snake = createSnake();

    private Optional<Cell> apple = Optional.empty();

    private final KeyAdapter keyAdapter = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                timer.stop();
                System.exit(0);
            case KeyEvent.VK_SPACE:
                reset();
                startGameLoop();
                break;
            }
            ArrowKey.fromKeyCode(e.getKeyCode()).ifPresent(k -> actOnKey(k));
        }
    };

    public Main() {
        super("Snakes");
        prepareWindow();
        timer = new Timer(DELAY, this);
        addKeyListener(keyAdapter);
        startGameLoop();
    }

    private void prepareWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setFrameBounds();
        setResizable(false);
        setFocusable(true);
        requestFocus();
        pack();
        setVisible(true);
    }

    private void setFrameBounds() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screenDevice = ge.getScreenDevices()[0];
        GraphicsConfiguration defaultConfiguration = screenDevice.getDefaultConfiguration();
        Rectangle screenBounds = defaultConfiguration.getBounds();

/*
        DisplayMode displayMode = screenDevice.getDisplayMode();
        System.out.printf("devs: %d, %dx%d\n", ge.getScreenDevices().length, displayMode.getWidth(), displayMode.getHeight());
        System.out.printf("bounds: %fx%f\n", screenBounds.getWidth(), screenBounds.getHeight());

        for (GraphicsDevice sd : ge.getScreenDevices()) {
            GraphicsConfiguration gc = sd.getDefaultConfiguration();
            System.out.printf("%s. bounds: %f, %f\n", sd.getIDstring(), gc.getBounds().getWidth(), gc.getBounds().getHeight());
        }
*/

        Dimension preferredSize = new Dimension(FIELD_SIZE.width * CELL_SIZE, FIELD_SIZE.height * CELL_SIZE);
        setPreferredSize(preferredSize);
        //setBounds(100, 100, preferredSize.width, preferredSize.height);
        //setLocation(screenBounds.x / 2 + preferredSize.width / 2, screenBounds.y / 2 + preferredSize.height / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        createApple();
        if (snake.tryMove(FIELD_SIZE)) {
            consumeApple();
        } else {
            stopGameLoop();
        }
        repaint();
        timer.setDelay(Math.max(DELAY - (snake.size() - INITIAL_SNAKE_SIZE), 50));
    }

    private void startGameLoop() {
        if (!running) {
            running = true;
            timer.start();
        }
    }

    private void stopGameLoop() {
        running = false;
        timer.stop();
    }

    @Override
    public void paint(Graphics g) {
        if (bs == null) {
            createBufferStrategy(2);
            bs = getBufferStrategy();
        }
        do {
            Graphics drawGraphics = null;
            try {
                drawGraphics = bs.getDrawGraphics();
                paintGame((Graphics2D) drawGraphics);
            } finally {
                if (drawGraphics != null) {
                    drawGraphics.dispose();
                }
            }
            bs.show();
        } while (bs.contentsLost());
    }

    private void paintGame(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getSize().width, getSize().height);
        snake.forEach(cell -> paintCell(g, cell, Color.GRAY));
        apple.ifPresent(apple -> paintCell(g, apple, Color.RED));
        if (snake.isDead()) {
            paintGameOver(g);
        }
    }

    private void paintGameOver(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(30f));
        g.drawString("Game over!", getSize().width / 2 - 60, getSize().height / 2);
    }

    private void paintCell(Graphics g, Cell cell, Color color) {
        double height = getPreferredSize().getHeight();
        g.setColor(color);
        g.fillRect(cell.x * CELL_SIZE, (int) (height - cell.y * CELL_SIZE) + CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    private void actOnKey(ArrowKey key) {
        snake.turn(key.direction());
    }

    private Snake createSnake() {
        return new Snake(INITIAL_SNAKE_SIZE, FIELD_SIZE.width / 2, FIELD_SIZE.height / 2);
    }

    private void consumeApple() {
        apple.ifPresent(cell -> {
            if (snake.touches(cell)) {
                apple = Optional.empty();
                snake.grow((int) (5 + 1.5 * snake.size() / 10));
            }
        });
    }

    private void reset() {
        snake = createSnake();
        createApple();
        repaint();
    }

    private void createApple() {
        if (!apple.isPresent()) {
            apple = Optional.of(determineAppleCell());
        }
    }

    private Cell determineAppleCell() {
        Cell cell;
        do {
            cell = new Cell(randomCoordinate(FIELD_SIZE.width), randomCoordinate(FIELD_SIZE.height));
        } while (snake.touches(cell));
        return cell;
    }

    private int randomCoordinate(int bound) {
        return Math.max(2, positionRandom.nextInt(bound - 2));
    }
}
