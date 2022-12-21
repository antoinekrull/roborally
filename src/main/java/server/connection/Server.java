package server.connection;

import communication.Message;
import communication.MessageCreator;
import game.player.Player;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private static Server server;

    protected Socket socket;
    protected ServerSocket serverSocket;
    public HashMap<Integer, Player> players = new HashMap<>();
    public HashMap<Integer, HandleClient> CLIENTS = new HashMap<>();
    //public final LinkedBlockingQueue<Message> messages;
    public int uniqueID = 0;
    public Boolean alive;

    public BooleanProperty online;
    MessageCreator messageCreator;
    private final String protocolVersion;

    private Server() {
        messageCreator = new MessageCreator();
        protocolVersion = "Version 0.1";
        online = new SimpleBooleanProperty(true);
    }

    public static Server getInstance() {
        if (server == null) {
            server = new Server();
        }
        return server;
    }
    public Boolean isAlive() {
        return this.alive;
    }
    public BooleanProperty onlineProperty() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online.set(online);
    }

    public void startServer(int port) {
        this.alive = true;
        Thread acceptClients = new Thread() {
            public void run() {
                try {
                    serverSocket = new ServerSocket(port);

                    while (true) {
                        socket = serverSocket.accept();

                        //handle multithreading for clients
                        int uniqueID = getUniqueID();
                        HandleClient client = new HandleClient(socket.getRemoteSocketAddress().toString(), socket.getPort(), socket, server , uniqueID);
                        client.setUsername("");
                        synchronized (CLIENTS) {
                            CLIENTS.put(uniqueID, client);
                        }

                        new Thread(client).start();
                    }

                } catch (Exception e) {
                    System.out.println("Error here " + e.getMessage());
                }
            }
        };
        acceptClients.start();

    }

    public void broadcast(int id, Message message) {
        for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
            if (client.getKey() != id) {
                client.getValue().write(message);
            }
        }
    }

    public void sendTo(int toUser, Message message) {
        for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
            if (client.getKey() == toUser) {
                client.getValue().write(message);
            }
        }
    }
    public synchronized int getUniqueID() {
        return uniqueID++;
    }
    public String getProtocolVersion() {return this.protocolVersion;}

    public void stopServer() throws IOException {
        //this.alive = false;
        setOnline(false);
        if (socket != null) {
            socket.close();
        }
        if (socket == null) {
            this.alive = false;
        }
        if (serverSocket != null) {
            serverSocket.close();
        }
    }
}

