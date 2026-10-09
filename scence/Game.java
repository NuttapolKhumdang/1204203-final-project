package scence;

import java.util.Random;

import javax.swing.JFrame;

import config.GameConfig;
import config.XConfig;
import helper.Network;

public class Game extends JFrame {
    public final static Network network = new Network();
    private String[] planets;

    public Game(String hosts) {
        try {
            System.out.println("[INFO ] Connecting to " + hosts);
            network.connect(hosts);

            while (!network.isGameStart()) {
                Thread.sleep(100);

                if (!network.isPlanetExists() && !network.isPlanetController()) {
                    network.setPlanetController();
                }

                if (network.isPlanetController()) {
                    if (planets == null) {
                        Random random = new Random();

                        planets = new String[random.nextInt(GameConfig.PLANET_MAX_COUNT - GameConfig.PLANET_MIN_COUNT)
                                + GameConfig.PLANET_MIN_COUNT];

                        for (int i = 0; i < planets.length; i++) {
                            planets[i] = String.valueOf(random.nextInt(9999));
                        }
                    }

                    network.emit(Network.ACTION_PLANET_SIZE, planets.length);
                    network.emit(Network.ACTION_PLANET_INFO, String.join("::", planets));
                }
            }
            Thread.sleep(1000);
        } catch (Exception e) {
            System.err.println("[ERROR] Cannot connect to server");
            System.exit(1);
        } finally {
            System.out.println("getPort(): " + network.getPort());
            System.out.println("isPlanetController(): " + network.isPlanetController());
        }

        setSize(XConfig.DISPLAY_WIDTH, XConfig.DISPLAY_HEIGH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Galaxy g = new Galaxy(network.enemiesIds().size());
        add(g);

        Thread galaxyThread = new Thread(g);
        galaxyThread.start();

        setVisible(true);
    }
}
