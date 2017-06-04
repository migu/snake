package de.cgan.games.snakes;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

/**
 * @author michael.gutbier@codecentric.de
 */
public class GamePanel extends JPanel implements Consumer<ArrowKey> {
    private static final Dimension FIELD_SIZE = new Dimension(100, 100);

    private static final int CELL_SIZE = 8;

    private Snake snake;

    private final Random positionRandom = new Random(System.currentTimeMillis());

    private Optional<Cell> apple = Optional.empty();

    public GamePanel() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(FIELD_SIZE.width * CELL_SIZE, FIELD_SIZE.height * CELL_SIZE));
        snake = createSnake();
    }

    private Snake createSnake() {
        return new Snake(FIELD_SIZE.width / 2, FIELD_SIZE.height / 2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getSize().width, getSize().height);
        snake.forEach(cell -> paintCell(g, cell, Color.GRAY));
        apple.ifPresent(apple -> paintCell(g, apple, Color.RED));
    }

    private void paintCell(Graphics g, Cell cell, Color color) {
        double height = getPreferredSize().getHeight();
        g.setColor(color);
        g.fillRect(cell.x * CELL_SIZE, (int) (height - cell.y * CELL_SIZE) + CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    @Override
    public void accept(ArrowKey key) {
        Direction dir = snake.direction();
        Direction keyDir = key.direction();
        if ((dir.x == 0 && keyDir.y == 0 && dir.y != keyDir.x) || (dir.y == 0 && keyDir.x == 0 && dir.x == keyDir.y)) {
            snake.turnLeft();
        } else {
            snake.turnRight();
        }
    }

    public boolean canMove() {
        return snake.canMove(FIELD_SIZE);
    }

    public void move() {
        snake.move();
        if (touchesApple()) {
            snake.grow(5);
        }
        repaint();
    }

    private boolean touchesApple() {
        return apple.map(cell -> {
            if (snake.touches(cell)) {
                apple = Optional.empty();
                return true;
            }
            return false;
        }).orElse(Boolean.FALSE);
    }

    public void reset() {
        snake = createSnake();
        createApple();
        repaint();
    }

    public void createApple() {
        if (!apple.isPresent()) {
            Cell cell;
            do {
                cell = new Cell(positionRandom.nextInt(FIELD_SIZE.width), positionRandom.nextInt(FIELD_SIZE.height));
            } while (snake.touches(cell));
            apple = Optional.of(cell);
        }
    }
}
