package scence;

import java.awt.Font;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import config.GameConfig;
import config.XConfig;
import helper.ActiveIPFetcher;
import helper.Network;

public class Game extends JFrame {
    public final static Network network = new Network();
    private String[] planets;

    public Game(String hosts) {
        JFrame watingRoom = new JFrame();
        watingRoom.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        watingRoom.setLocationRelativeTo(null);
        watingRoom.setLayout(null);
        watingRoom.setSize(600, 300);

        JLabel watingText = new JLabel("Wating for Connection", JLabel.CENTER);
        watingText.setFont(new Font("CommitMono", Font.BOLD, 32));
        watingText.setBounds(0, 50, 600, 100);
        watingRoom.add(watingText);

        if (hosts.equals("::1")) {
            JLabel serverText = new JLabel("Server IP: " + ActiveIPFetcher.get(), JLabel.CENTER);
            serverText.setFont(new Font("CommitMono", Font.BOLD, 32));
            serverText.setBounds(0, 100, 600, 100);
            watingRoom.add(serverText);
        }

        watingRoom.setVisible(true);

        new Thread(() -> {
            try {
                System.out.println("[INFO ] Connecting to " + hosts);
                network.connect(hosts);

                while (!network.isGameStart()) {
                    Thread.sleep(XConfig.FRAME_TIME * 2);

                    if (!network.isPlanetExists() && !network.isPlanetController()) {
                        network.setPlanetController();
                    }

                    if (network.isPlanetController()) {
                        if (planets == null) {
                            Random random = new Random();

                            planets = new String[random
                                    .nextInt(GameConfig.PLANET_MAX_COUNT - GameConfig.PLANET_MIN_COUNT)
                                    + GameConfig.PLANET_MIN_COUNT];

                            for (int i = 0; i < planets.length; i++) {
                                planets[i] = String.valueOf(random.nextInt(9999));
                            }
                        }

                        network.emit(Network.ACTION_PLANET_SIZE, planets.length);
                        network.emit(Network.ACTION_PLANET_INFO, String.join("::", planets));
                    }
                }
                Thread.sleep(XConfig.FRAME_TIME * 4);
            } catch (Exception e) {
                System.err.println("[ERROR] Cannot connect to server");
                System.exit(1);
            } finally {
                System.out.println("getPort(): " + network.getPort());
                System.out.println("isPlanetController(): " + network.isPlanetController());

                SwingUtilities.invokeLater(() -> {
                    watingRoom.dispose();
                    startGameWindow();
                });
            }
        }).start();
    }

    private void startGameWindow() {
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
