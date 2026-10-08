package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

import config.GameConfig;
import config.XConfig;

public class Server extends Thread {
    // Thread-safe list of all connected clients
    final CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    @Override
    public void run() {
        Server server = new Server();

        try {
            ServerSocket serverSocket = new ServerSocket(XConfig.NETWORK_PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, server);

                server.clients.add(handler); // register
                new Thread(handler).start();

                System.out.println("SERVER::CONNECTED::" + socket.getInetAddress());
                System.out.println("SERVER::CLIENT_SIZE::" + server.clients.size());
                server.broadcast("SERVER::CLIENT_SIZE::" + server.clients.size());

                String pString = "";
                for (ClientHandler c : server.clients) {
                    pString += "::" + String.valueOf(c.getSocket().getPort());
                }
                server.broadcast("SERVER::CLIENT_PORT" + pString);

                if (server.clients.size() == GameConfig.MAX_PLAYER) {
                    server.broadcast("SERVER::START");
                }
            }
        } catch (Exception e) {
        }
    }

    // Call this from anywhere to broadcast
    public void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.send(message);
        }
    }
}