import javax.swing.JFrame;
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
        // String hosts = JOptionPane.showInputDialog(null, "Connect to Server (Empty to
        // be Server)", "final@oop",
        // JOptionPane.QUESTION_MESSAGE);

        // if (hosts.length() == 0) {
        // String max = JOptionPane.showInputDialog(null, "Expeted Player (MAX: " +
        // GameConfig.MAX_PLAYER + " )",
        // "final@oop",
        // JOptionPane.QUESTION_MESSAGE);

        // int playerCount = GameConfig.MAX_PLAYER;

        // try {
        // playerCount = Integer.parseInt(max);
        // if (playerCount <= 0)
        // playerCount = GameConfig.MAX_PLAYER;
        // if (playerCount >= GameConfig.MAX_PLAYER)
        // playerCount = GameConfig.MAX_PLAYER;

        // } catch (Exception e) {
        // }

        // new Main(XConfig.ROLE_SERVER, XConfig.NETWORK_HOST, playerCount);
        // } else {
        // new Main(XConfig.ROLE_CLIENT, hosts, -1);
        // }

        new Main("::1");
    }
}
