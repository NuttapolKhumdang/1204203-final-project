package objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import config.GameConfig;
import config.XConfig;
import helper.Asset;

public class Ammo extends Thread {
    Image ammoTexture;
    Player owner;

    public int id;

    int size = 25;
    public double x, y;
    double vx, vy;
    double direction;
    int speed = GameConfig.AMMO_SPEED;

    public boolean isCrashed = false;

    public Ammo(Player player, int id, int x, int y, double direction) {
        this.owner = player;
        this.id = id;
        this.ammoTexture = Asset.getAmmo(player.id % 4 + 1);
        this.x = x;
        this.y = y;
        this.direction = direction;
        direction -= Math.PI / 2;

        vx = Math.cos(direction);
        vy = Math.sin(direction);
    }

    public void draw(Graphics g) {
        if (isCrashed)
            return;

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();
        g2d.rotate(direction, x + size / 2, y + size / 2);
        g2d.drawImage(ammoTexture, (int) x, (int) y, size, size, null);
        g2d.setTransform(originalTransform);
    }

    public void crash() {
        isCrashed = true;
    }

    public boolean isCollision(int tx, int ty) {
        if (isCrashed)
            return false;

        return Math.abs(x - tx) <= size
                && Math.abs(y - ty) <= size;
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
