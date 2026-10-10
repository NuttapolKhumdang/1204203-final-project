import javax.swing.JFrame;
import javax.swing.JOptionPane;

import config.XConfig;
import scence.Game;

public class Main extends JFrame {
    Main(String hosts) {
        setSize(XConfig.DISPLAY_WIDTH, XConfig.DISPLAY_HEIGH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        dispose();

        new Game(hosts);
    }

    public static void main(String[] args) {
        String hostIp = JOptionPane.showInputDialog(null, "Connect to Server", "final@oop",
                JOptionPane.QUESTION_MESSAGE);
        new Main(hostIp != "" ? hostIp : "::1");
    }
}
