import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import scence.Game;
import server.Server;

public class Main {
    static Server server;

    Main(String hosts) {
        new Game(hosts);
    }

    public static void main(String[] args) {
        MainDialog dialog = new MainDialog();

        try {
            while (!dialog.isConfirm()) {
                Thread.sleep(1);
            }
        } catch (Exception e) {
        }

        boolean isServer = dialog.isServer();

        if (!isServer) {
            new Main(dialog.getHost());
        }

        if (isServer) {
            server = new Server();
            server.start();

            new Main("::1");
        }
    }
}

class MainDialog extends JFrame {
    private String host;
    private boolean isConfirm = false;
    private boolean isServer = false;

    MainDialog() {
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setLayout(null);

        JLabel heroLabel = new JLabel("Space Ship Battle");
        heroLabel.setFont(new Font("CommitMono", Font.BOLD, 32));
        heroLabel.setBounds(20, 20, 600, 50);
        add(heroLabel);

        JLabel ipLabel = new JLabel("Host IP");
        ipLabel.setBounds(20, 100, 600 - 40, 20);
        add(ipLabel);

        JTextField ip = new JTextField();
        ip.setFont(new Font("CommitMono", Font.BOLD, 16));
        ip.setBounds(20, 130, 600 - 40, 40);
        ip.setText(host);
        add(ip);

        JButton clientButton = new JButton("Connect");
        clientButton.setBounds(310, 180, 270, 50);
        clientButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                host = ip.getText();
                isConfirm = true;

                dispose();
            }
        });
        add(clientButton);

        JButton serverButton = new JButton("Start Server");
        serverButton.setBounds(20, 180, 270, 50);
        serverButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isServer = true;
                isConfirm = true;

                dispose();
            }
        });
        add(serverButton);
    }

    public boolean isServer() {
        return isServer;
    }

    public boolean isConfirm() {
        return isConfirm;
    }

    public String getHost() {
        return host;
    }
}