package scence;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import javax.swing.JPanel;

import config.GameConfig;
import config.XConfig;
import helper.Asset;
import objects.Ammo;
import objects.AmmoData;
import objects.Enemy;
import objects.Planet;
import objects.Player;
import objects.PlayerData;

public class Galaxy extends JPanel implements Runnable {
    Image background = Asset.getImage("background.png");
    Planet[] planet;
    Player player;
    Enemy[] enemies;

    @Override
    public void run() {
        while (true) {
            repaint();
            collisionObserver();
            enemyObserver();
            ammoObserver();
            gameObserver();

            try {
                Thread.sleep(15);
            } catch (Exception e) {
            }
        }
    }

    public void gameObserver() {
        if (Game.network.isGameEnded()) {
            player.ended();
        }
    }

    public void enemyObserver() {
        int living = enemies.length;

        for (Player enemy : enemies) {
            PlayerData data = Game.network.getPLayerData(enemy.id);
            enemy.move(data);

            int life = Game.network.getLife(enemy.id);
            enemy.setLife(life);

            if (life <= 0)
                living--;
        }

        if (living <= 0 && !Game.network.isGameEnded()) {
            Game.network.emit("ENDED");
        }
    }

    public void ammoObserver() {
        for (Player e : enemies) {

            AmmoData ammoData = Game.network.getAmmo(e.id);
            if (ammoData == null)
                continue;

            e.fire(ammoData);
            Game.network.removeAmmo(e.id);
        }
    }

    public void collisionObserver() {
        // Crashed ammo data
        for (Enemy enemy : enemies) {
            for (Ammo ammo : player.ammos) {
                if (ammo == null)
                    continue;

                if (enemy.isCollision((int) ammo.x, (int) ammo.y)) {
                    ammo.crash();
                    break;
                }
            }
        }

        // Player, player's ammo with planets
        for (Planet p : planet) {
            // Player's ammo with planet
            for (Ammo ammo : player.ammos) {
                if (ammo == null || ammo.isCrashed)
                    continue;

                if (ammo.isCollision(p.x, p.y)) {
                    ammo.crash();
                    break;
                }
            }

            // Player with planet
            if (player.isCollision(p.x, p.y)) {
                player.crashWithPlanet();
            }
        }

        // Player with enemies's ammo
        for (Enemy enemy : enemies) {
            if (enemy == null)
                continue;

            for (Ammo ammo : enemy.ammos) {
                if (ammo == null)
                    continue;

                if (player.isCollision((int) ammo.x, (int) ammo.y)) {
                    player.crashWithAmmo(ammo);
                    ammo.crash();
                    continue;
                }

                for (Enemy target : enemies) {
                    if (enemy == null || target == enemy)
                        continue;

                    if (target.isCollision((int) ammo.x, (int) ammo.y)) {
                        ammo.crash();
                        continue;
                    }
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, XConfig.DISPLAY_WIDTH, XConfig.DISPLAY_HEIGH, null);

        player.draw(g);

        for (Player e : enemies) {
            e.draw(g);
        }

        for (Planet p : planet) {
            p.draw(g);
        }
    }

    public Galaxy(int enemiesCount) {
        setSize(XConfig.DISPLAY_WIDTH, XConfig.DISPLAY_HEIGH);
        setLayout(null);
        Random random = new Random();

        player = new Player(Game.network.getPort());
        add(player);
        new Thread(player).start();

        enemies = new Enemy[enemiesCount];
        for (int i = 0; i < enemies.length; i++) {
            enemies[i] = new Enemy(Game.network.enemiesIds().get(i));
            new Thread(enemies[i]).start();
        }

        planet = new Planet[random.nextInt(GameConfig.PLANET_MAX_COUNT - GameConfig.PLANET_MIN_COUNT)
                + GameConfig.PLANET_MIN_COUNT];

        for (int i = 0; i < planet.length; i++) {
            planet[i] = new Planet();
            planet[i].start();
        }
    }
}
