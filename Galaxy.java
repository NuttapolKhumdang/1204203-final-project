import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import javax.swing.JPanel;

import config.GameConfig;
import config.XConfig;
import helper.Asset;
import objects.Ammo;
import objects.Planet;
import objects.Player;

public class Galaxy extends JPanel implements Runnable {
    Image background = Asset.getImage("background.png");
    Planet[] planet;
    Player player;

    @Override
    public void run() {
        while (true) {
            repaint();
            collisionObserver();

            try {
                Thread.sleep(2);
            } catch (Exception e) {
            }
        }
    }

    public void collisionObserver() {
        // Player ammo with planets
        for (Ammo ammo : player.ammos) {
            if (ammo == null)
                continue;

            for (Planet p : planet) {
                if (ammo.isCollision(p.x, p.y)) {
                    ammo.crash();
                    break;
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, XConfig.DISPLAY_WIDTH, XConfig.DISPLAY_HEIGH, null);

        player.draw(g);

        for (Planet p : planet) {
            p.draw(g);
        }
    }

    public Galaxy() {
        setSize(XConfig.DISPLAY_WIDTH, XConfig.DISPLAY_HEIGH);
        setLayout(null);
        Random random = new Random();

        player = new Player();
        add(player);
        new Thread(player).start();

        planet = new Planet[random.nextInt(GameConfig.PLANET_MAX_COUNT - GameConfig.PLANET_MIN_COUNT)
                + GameConfig.PLANET_MIN_COUNT];

        for (int i = 0; i < planet.length; i++) {
            planet[i] = new Planet();
            planet[i].start();
        }
    }

}
