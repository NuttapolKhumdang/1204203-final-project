package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

import config.GameConfig;
import config.XConfig;

public class Server extends Thread {
    final CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    @Override
    public void run() {
        Server server = new Server();
        System.out.println("SERVER::WATING_FOR::" + GameConfig.MAX_PLAYER);

        try {
            ServerSocket serverSocket = new ServerSocket(XConfig.NETWORK_PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, server);

                server.clients.add(handler);
                new Thread(handler).start();

                System.out.println("SERVER::CONNECTED::" + socket.getInetAddress());
                System.out.println("SERVER::CLIENT_SIZE::" + server.clients.size());
                server.broadcast("SERVER::11::" + server.clients.size());

                String pString = "";
                for (ClientHandler c : server.clients) {
                    pString += "::" + String.valueOf(c.getSocket().getPort());
                }
                server.broadcast("SERVER::12" + pString);

                if (server.clients.size() == GameConfig.MAX_PLAYER) {
                    server.broadcast("SERVER::21");
                }
            }
        } catch (Exception e) {
        }
    }

    public void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.send(message);
        }
    }
}