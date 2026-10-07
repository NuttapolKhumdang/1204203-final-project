package experimentals;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * clinet
 */
public class Server extends Thread {
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(33406);

            while (true) {
                String line = "";

                Socket socket = serverSocket.accept();

                InputStream input = socket.getInputStream();
                InputStreamReader stream = new InputStreamReader(input);
                BufferedReader reader = new BufferedReader(stream);

                while ((line = reader.readLine()) != null) {
                    System.out.println("message: " + line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server s = new Server();
        s.start();
    }
}