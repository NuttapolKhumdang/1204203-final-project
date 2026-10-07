package objects;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.Random;

import javax.swing.JPanel;

import config.GameConfig;
import config.XConfig;
import helper.Asset;

public class Player extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {
    Image shipTexture = Asset.getImage("Ship1.png");
    Random random = new Random();
    Ammo[] ammos = new Ammo[GameConfig.MAX_AMMO];

    int canvasWidth = XConfig.DISPLAY_WIDTH;
    int canvasHeight = XConfig.DISPLAY_HEIGH;

    boolean isAPressed = false;
    boolean isWPressed = false;
    boolean isSPressed = false;
    boolean isDPressed = false;

    double rotation = 180;

    int x, y;
    int vx = 0;
    int vy = 0;
    int size = 50;
    int ammo = GameConfig.MAX_AMMO;
    int life = GameConfig.MAX_LIFE;
    int speed = GameConfig.SHIP_MIN_SPEED;

    @Override
    public void run() {
        while (true) {
            move(isAPressed, isWPressed, isSPressed, isDPressed);
            ammoObserver();
            try {
                Thread.sleep(15 / speed);
            } catch (Exception e) {
            }
        }
    }

    public Player() {
        setOpaque(false);
        setFocusable(true);
        requestFocusInWindow();
        setSize(canvasWidth, canvasHeight);

        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        x = random.nextInt(canvasWidth - size * 2) + size;
        y = random.nextInt(canvasHeight - size * 2) + size;
    }

    public void draw(Graphics g) {
        g.setFont(new Font("CommitMono", Font.BOLD, 16));
        g.drawString("HP  : " + life + "/" + GameConfig.MAX_LIFE, 25, 25);
        g.drawString("AMMO: " + ammo + "/" + GameConfig.MAX_AMMO, 25, 50);

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();
        g2d.rotate(rotation, x + size / 2, y + size / 2);
        g2d.drawImage(shipTexture, x, y, size, size, null);
        g2d.setTransform(originalTransform);

        for (Ammo a : ammos) {
            if (a != null)
                a.draw(g);
        }
    }

    protected double calculateRadians(int ax, int ay, int bx, int by) {
        int dx = bx - ax;
        int dy = by - ay;

        double radians = Math.atan2(dy, dx);
        radians += Math.PI / 2;

        return radians;
    }

    protected void ammoObserver() {
        for (int idx = 0; idx < ammos.length; idx++) {
            if (ammos[idx] != null && ammos[idx].isCrashed) {
                ammos[idx] = null;
                ammo++;
            }
        }
    }

    protected void fire() {
        if (ammo <= 0) {
            return; // out of ammo
        }

        for (int idx = 0; idx < ammos.length; idx++) {
            if (ammos[idx] != null)
                continue;

            ammos[idx] = new Ammo(x, y, rotation);
            ammos[idx].start();
            ammo--;
            break;
        }
    }

    protected void move(boolean a, boolean w, boolean s, boolean d) {
        if (s) {
            y += speed;

            if ((y + size * 2) >= canvasHeight)
                y = canvasHeight - size * 2;
        }

        if (w) {
            y -= speed;

            if (y <= size)
                y = size;
        }

        if (d) {
            x += speed;

            if ((x + size * 2) >= canvasWidth)
                x = canvasWidth - size * 2;
        }

        if (a) {
            x -= speed;

            if (x <= size)
                x = size;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 65) {
            // A
            isAPressed = true;
        }
        if (e.getKeyCode() == 87) {
            // W
            isWPressed = true;
        }
        if (e.getKeyCode() == 83) {
            // S
            isSPressed = true;
        }
        if (e.getKeyCode() == 68) {
            // D
            isDPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 65) {
            // A
            isAPressed = false;
        }
        if (e.getKeyCode() == 87) {
            // W
            isWPressed = false;
        }
        if (e.getKeyCode() == 83) {
            // S
            isSPressed = false;
        }
        if (e.getKeyCode() == 68) {
            // D
            isDPressed = false;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        rotation = calculateRadians(x, y, mx, my);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        fire();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
