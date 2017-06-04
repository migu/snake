package de.cgan.games.snakes;

import java.util.Objects;

/**
 * @author michael.gutbier@codecentric.de
 */
public class Direction extends Vector {
    private Direction(int x, int y) {
        super(x, y);
    }

    public static Direction fromInt(int n) {
        switch (n) {
        case 0:
            return north();
        case 1:
            return east();
        case 2:
            return south();
        case 3:
            return west();
        default:
            return north();
        }
    }

    public static Direction north() {
        return new Direction(0, 1);
    }

    public static Direction east() {
        return new Direction(1, 0);
    }

    public static Direction south() {
        return new Direction(0, -1);
    }

    public static Direction west() {
        return new Direction(-1, 0);
    }

    public Direction turnLeft() {
        return turn(-1);
    }

    public Direction turnRight() {
        return turn(1);
    }

    private Direction turn(int d) {
        return new Direction(d * y, -d * x);
    }
}
