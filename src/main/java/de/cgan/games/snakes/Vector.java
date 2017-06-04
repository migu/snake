package de.cgan.games.snakes;

import java.util.Objects;

/**
 * @author michael.gutbier@codecentric.de
 */
public class Vector {
    public final int x;

    public final int y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Direction direction = (Direction) o;
        return x == direction.x && y == direction.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
