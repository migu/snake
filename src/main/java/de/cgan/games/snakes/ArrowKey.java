package de.cgan.games.snakes;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author michael.gutbier@codecentric.de
 */
public enum ArrowKey {
    UP(KeyEvent.VK_UP, Direction.north()), DOWN(KeyEvent.VK_DOWN, Direction.south()), //
    LEFT(KeyEvent.VK_LEFT, Direction.west()), RIGHT(KeyEvent.VK_RIGHT, Direction.east());

    private final int keyCode;

    private final Direction direction;

    ArrowKey(int keyCode, Direction direction) {
        this.keyCode = keyCode;
        this.direction = direction;
    }

    public Direction direction() {
        return direction;
    }

    public static Optional<ArrowKey> fromKeyCode(int keyCode) {
        return Arrays.asList(values()).stream().filter(k -> k.keyCode == keyCode).findFirst();
    }
}
