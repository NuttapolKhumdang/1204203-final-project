package helper;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class ActiveIPFetcher {
    public static String get() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            String activeIp = socket.getLocalAddress().getHostAddress();
            return activeIp;
        } catch (Exception e) {
            System.err.println("Could not determine active IP: " + e.getMessage());
            return null;
        }
    }
}