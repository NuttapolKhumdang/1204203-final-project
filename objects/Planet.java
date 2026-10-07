package objects;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import config.GameConfig;
import config.XConfig;
import helper.Asset;

public class Planet extends Thread {
    Random random = new Random();
    Image texture = Asset.getImage("Planet_Large.png");
    int[] SIZE = { 30, 45, 60 };

    int x, y;
    int vx, vy;
    int speed;
    int planetSize;
    int canvasWidth, canvasHeight;

    public void draw(Graphics g) {
        g.drawImage(texture, x, y, planetSize, planetSize, null);
    }

    public Planet() {
        this.canvasWidth = XConfig.DISPLAY_WIDTH;
        this.canvasHeight = XConfig.DISPLAY_HEIGH;

        planetSize = SIZE[random.nextInt(3)];
        x = random.nextInt(canvasWidth - planetSize * 2) + planetSize;
        y = random.nextInt(canvasHeight - planetSize * 2) + planetSize;

        do {
            vx = random.nextInt(3) - 1;
            vy = random.nextInt(3) - 1;
        } while (vx == 0 && vy == 0); // do it until some velocity is not zero

        speed = random.nextInt(GameConfig.PLANET_MAX_SPEED) + GameConfig.PLANET_MIN_SPEED;
    }

    protected void move() {
        x += vx;
        y += vy;
    }

    protected void collisionObserver() {
        if (x <= 0) {
            vx = 1;
            vy = random.nextInt(3) - 1;
        } else if ((x + planetSize) >= canvasWidth) {
            vx = -1;
            vy = random.nextInt(3) - 1;
        }

        if (y <= 0) {
            vx = random.nextInt(3) - 1;
            vy = 1;
        } else if ((y + planetSize) >= canvasHeight) {
            vx = random.nextInt(3) - 1;
            vy = -1;
        }

        if (speed > GameConfig.PLANET_MAX_SPEED)
            speed = GameConfig.PLANET_MAX_SPEED;
    }

    @Override
    public void run() {
        while (true) {
            move();
            collisionObserver();

            try {
                Thread.sleep(100 / speed);
            } catch (Exception e) {
            }
        }
    }
}
