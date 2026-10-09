package scence;

import javax.swing.JFrame;

import config.XConfig;
import helper.Network;

public class Game extends JFrame {
    public final static Network network = new Network();

    public Game(String hosts) {
        try {
            System.out.println("[INFO ] Connecting to " + hosts);
            network.connect(hosts);

            while (!network.isGameStart()) {
                Thread.sleep(100);
            }
            Thread.sleep(1000);
        } catch (Exception e) {
            System.err.println("[ERROR] Cannot connect to server");
            System.exit(1);
        } finally {
            System.out.println(network.getPort());
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
