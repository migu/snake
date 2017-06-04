package de.cgan.games.snakes;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * @author michael.gutbier@codecentric.de
 */
public class Snake implements Iterable<Cell> {
    private LinkedList<Cell> cells = new LinkedList<>();

    private Direction direction = Direction.fromInt(new Random().nextInt(4));

    private int growByAmount;

    private boolean alive = true;

    public Snake(int initialSize, int xStart, int yStart) {
        cells.add(new Cell(xStart, yStart));
        for (int i = 1; i <= initialSize - 1; i++) {
            cells.addLast(new Cell(xStart - direction.x * i, yStart - direction.y * i));
        }
    }

    public Direction direction() {
        return direction;
    }

    public void turn(Direction keyDir) {
        Direction d = direction;
        if ((d.x == 0 && keyDir.y == 0 && d.y != keyDir.x) || (d.y == 0 && keyDir.x == 0 && d.x == keyDir.y)) {
            turnLeft();
        } else if ((d.x == 0 && keyDir.y == 0) || (d.y == 0 && keyDir.x == 0)) {
            turnRight();
        }
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

    public boolean tryMove(Dimension fieldSize) {
        alive = canMove(fieldSize);
        if (alive) {
            move();
        }
        return alive;
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

    public boolean isDead() {
        return !alive;
    }

    public int size() {
        return cells.size();
    }
}