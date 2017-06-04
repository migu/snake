package de.cgan.games.snakes;

/**
 * @author michael.gutbier@codecentric.de
 */
public class Cell extends Vector {
    public Cell(int x, int y) {
        super(x, y);
    }

    public Cell next(Direction direction) {
        return new Cell(x + direction.x, y + direction.y);
    }
}
