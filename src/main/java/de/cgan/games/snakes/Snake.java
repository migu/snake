package de.cgan.games.snakes;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * @author michael.gutbier@codecentric.de
 */
public class Snake implements Iterable<Cell> {
    private static final int INITIAL_SIZE = 5;

    private LinkedList<Cell> cells = new LinkedList<>();

    private Direction direction = Direction.fromInt(new Random().nextInt(4));

    private int growByAmount;

    public Snake(int xStart, int yStart) {
        cells.add(new Cell(xStart, yStart));
        for (int i = 1; i <= INITIAL_SIZE; i++) {
            cells.addLast(new Cell(xStart - direction.x * i, yStart - direction.y * i));
        }
    }

    public Direction direction() {
        return direction;
    }

    public void turnLeft() {
        direction = direction.turnLeft();
    }

    public void turnRight() {
        direction = direction.turnRight();
    }

    public Cell head() {
        return cells.getFirst();
    }

    public Snake grow(int amount) {
        this.growByAmount += amount;
        return this;
    }

    public boolean canMove(Dimension bounds) {
        Cell nextCell = head().next(direction);
        return doesNotTouch(nextCell) && cellIsInBounds(nextCell, bounds);
    }

    public boolean doesNotTouch(Cell cell) {
        return !touches(cell);
    }

    public boolean touches(Cell cell) {
        return cells.stream().anyMatch(c -> c.x == cell.x && c.y == cell.y);
    }

    private boolean cellIsInBounds(Cell cell, Dimension bounds) {
        return cell.x >= 0 && cell.x < bounds.width && cell.y >= 0 && cell.y < bounds.height;
    }

    public Snake move() {
        cells.addFirst(head().next(direction));
        if (growByAmount > 0) {
            growByAmount--;
        } else {
            cells.removeLast();
        }
        return this;
    }

    @Override
    public Iterator<Cell> iterator() {
        return cells.iterator();
    }
}