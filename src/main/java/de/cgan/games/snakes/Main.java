package de.cgan.games.snakes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

/**
 * @author michael.gutbier@codecentric.de
 */
public class Main extends JFrame implements ActionListener, KeyListener {
    public static final int DELAY = 90;

    public static void main(String... args) {
        SwingUtilities.invokeLater(Main::new);
    }

    private final GamePanel gamePanel = new GamePanel();

    private final Timer timer;

    private boolean running;

    public Main() {
        super("Snakes");
        setContentPane(gamePanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setFocusable(true);
        requestFocus();
        addKeyListener(this);
        setLocationRelativeTo(null);
        setVisible(true);
        timer = new Timer(DELAY, this);
        startGameLoop();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_ESCAPE:
            timer.stop();
            System.exit(0);
        case KeyEvent.VK_SPACE:
            startGameLoop();
            break;
        }
        ArrowKey.fromKeyCode(e.getKeyCode()).ifPresent(gamePanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gamePanel.createApple();
        if (gamePanel.canMove()) {
            gamePanel.move();
        } else {
            gamePanel.reset();
            stopGameLoop();
        }
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
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
