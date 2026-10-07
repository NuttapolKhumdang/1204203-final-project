package objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import config.GameConfig;
import config.XConfig;
import helper.Asset;

public class Ammo extends Thread {
    Image ammoTexture = Asset.getImage("Ship1_ammo.png");

    int size = 25;
    double x, y;
    double vx, vy;
    double direction;
    int speed = GameConfig.AMMO_SPEED;

    boolean isCrashed = false;

    Ammo(int x, int y, double direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;

        direction -= Math.PI / 2;
        vx = Math.cos(direction);
        vy = Math.sin(direction);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();
        g2d.rotate(direction, x + size / 2, y + size / 2);
        g2d.drawImage(ammoTexture, (int) x, (int) y, size, size, null);
        g2d.setTransform(originalTransform);
    }

    protected void move() {
        x += vx;
        y += vy;
    }

    protected void collisionObserver() {
        if (false
                || x <= 0
                || (x + size) >= XConfig.DISPLAY_WIDTH
                || y <= 0
                || (y + size) >= XConfig.DISPLAY_HEIGH)
            isCrashed = true;
    }

    @Override
    public void run() {
        while (!isCrashed) {
            move();
            collisionObserver();

            try {
                Thread.sleep(10 / GameConfig.AMMO_SPEED);
            } catch (Exception e) {
            }
        }
    }
}
