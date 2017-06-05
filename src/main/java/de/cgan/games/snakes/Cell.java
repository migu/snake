package de.cgan.games.snakes;

import java.awt.*;

/**
 * @author michael.gutbier@codecentric.de
 */
public class Cell extends Vector {
    public Cell(int x, int y) {
        super(x, y);
    }

    public Cell next(Direction direction, Dimension bounds) {
        int x = this.x + direction.x;
        int y = this.y + direction.y;
        return new Cell(x < 0 || x >= bounds.width ? Math.abs(bounds.width + x) % bounds.width : x,
                y < 0 || y >= bounds.height ? Math.abs(bounds.height + y) % bounds.height : y);
    }
}
