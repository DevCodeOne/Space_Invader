package Graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Display extends Canvas implements KeyListener {

    private JFrame display;
    private BufferStrategy strategy;
    private BufferedImage offscreen;
    private DrawInterface drawInterface;
    private boolean keys[];

    public Display(int width, int height, DrawInterface drawInterface, String title) {
        display = new JFrame(title);
        display.setSize(width, height);
        display.setLocationRelativeTo(null);
        display.setResizable(false);
        display.setVisible(true);
        display.setLayout(null);
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(width, height);
        setLocation(0, 0);
        addKeyListener(this);

        display.add(this);

        createBufferStrategy(1);
        strategy = getBufferStrategy();

        offscreen = new BufferedImage(width>>1, height>>1, BufferedImage.TYPE_INT_RGB);
        keys = new boolean[220];

        this.drawInterface = drawInterface;

    }

    public void display() {
        if (!strategy.contentsLost()) {
            Graphics2D graphics = offscreen.createGraphics();
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, getWidth(), getHeight());
            drawInterface.draw(graphics);
            strategy.getDrawGraphics().drawImage(offscreen, 0, 0, getWidth(), getHeight(), null);
            strategy.show();
        }
    }

    public boolean[] grabKeys() {
        return keys;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        keys[keyEvent.getKeyCode()] = true;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        keys[keyEvent.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        keys[keyEvent.getKeyCode()] = false;
    }
}