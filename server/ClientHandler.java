package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Server server;
    private PrintWriter out;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        try {
            socket.setKeepAlive(true);
            socket.setSoTimeout(1000 * 60 * 5);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String line;
            while ((line = in.readLine()) != null) {
                server.broadcast(line);
            }
        } catch (Exception e) {
        } finally {
            server.clients.remove(this);
            try {
                socket.close();
            } catch (Exception ignored) {
            }
        }
    }

    public void send(String message) {
        try {
            synchronized (this) {
                out.println(message);
            }
        } catch (Exception e) {
        }
    }
}