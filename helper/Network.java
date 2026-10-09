package helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import config.GameConfig;
import config.XConfig;

public class Network {
    public final static int ACTION_CLIENT_SIZE = 11;
    public final static int ACTION_CLIENT_PORT = 12;
    public final static int ACTION_GAME_START = 21;
    public final static int ACTION_GAME_ENDED = 22;
    public final static int ACTION_PLAYER_MOVE = 31;
    public final static int ACTION_PLAYER_LIFE = 32;
    public final static int ACTION_PLAYER_FIRE = 33;

    private PrintWriter writer;
    private Socket socket;
    private int connectionCount = 0;

    private boolean gameStart = false;
    private boolean gameEnded = false;
    private static ArrayList<Integer> connectionPorts = new ArrayList<>();
    private static HashMap<Integer, Position> player = new HashMap<>();
    private static HashMap<Integer, Position> ammo = new HashMap<>();
    private static HashMap<Integer, Integer> life = new HashMap<>();

    public void connect(String host) throws IOException {
        socket = new Socket(host, XConfig.NETWORK_PORT);
        writer = new PrintWriter(socket.getOutputStream(), true);

        new Thread(() -> {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String line = "";
                while ((line = in.readLine()) != null) {
                    handler(line);
                }
            } catch (IOException e) {

            }
        }, "network-client").start();
    }

    public void emit(int action, String message) {
        String ensemble = "" + socket.getLocalPort() + "::" + action + "::" + message;
        synchronized (writer) {
            writer.println(ensemble);
        }
    }

    public void emit(int action) {
        emit(action, "");
    }

    public void emit(int action, int message) {
        emit(action, String.valueOf(message));
    }

    public void emit(int action, Position position) {
        emit(action, String.valueOf(position));
    }

    public int getPort() {
        return socket.getLocalPort();
    }

    public int getConnectionCount() {
        return connectionCount;
    }

    public boolean isGameStart() {
        return gameStart;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public ArrayList<Integer> enemiesIds() {
        return connectionPorts;
    }

    public Position getPlayer(int id) {
        return player.get(id);
    }

    public Position getAmmo(int id) {
        return ammo.get(id);
    }

    public void removeAmmo(int id) {
        ammo.put(id, null);
    }

    public int getLife(int id) {
        if (!life.containsKey(id))
            return GameConfig.MAX_LIFE;
        return life.get(id);
    }

    public void handler(String message) {
        String[] fracture = message.split("::");
        String sender = fracture[0];
        String action = fracture[1];

        if (action.equals("CLIENT_SIZE")) {
            connectionCount = Integer.valueOf(fracture[2]);
        }

        if (action.equals("CLIENT_PORT")) {
            for (int i = 2; i < fracture.length; i++) {
                int id = Integer.valueOf(fracture[i]);

                if (id == getPort())
                    continue;

                if (!connectionPorts.contains(id))
                    connectionPorts.add(id);

                System.out.println("PORT::" + id);

                player.put(id, new Position(id));
            }
        }

        if (action.equals("START")) {
            gameStart = true;
        }

        if (action.equals("ENDED")) {
            gameEnded = true;
        }

        if (action.equals("MOVE")) {
            int id = Integer.parseInt(sender);
            int x = Integer.parseInt(fracture[2]);
            int y = Integer.parseInt(fracture[3]);
            double rotation = Double.parseDouble(fracture[4]);

            Position position = new Position(id, x, y, rotation);
            player.put(id, position);
        }

        if (action.equals("LIFE")) {
            int id = Integer.parseInt(sender);
            int hp = Integer.parseInt(fracture[2]);

            life.put(id, hp);
        }

        if (action.equals("FIRE")) {
            int id = Integer.parseInt(sender);
            int ammoId = Integer.parseInt(fracture[2]);
            int x = Integer.parseInt(fracture[3]);
            int y = Integer.parseInt(fracture[4]);
            double rotation = Double.parseDouble(fracture[5]);

            Position position = new Position(ammoId, x, y, rotation);
            ammo.put(id, position);
        }
    }
}
