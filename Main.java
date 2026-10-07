import javax.swing.JFrame;

import config.XConfig;

public class Main extends JFrame {
    Main() {
        setSize(XConfig.DISPLAY_WIDTH, XConfig.DISPLAY_HEIGH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Galaxy g = new Galaxy();
        add(g);

        Thread galaxyThread = new Thread(g);
        galaxyThread.start();
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
