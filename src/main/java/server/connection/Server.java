package server.connection;

import communication.Message;
import communication.MessageCreator;
import communication.MessageType;
import game.Game;
import game.player.Player;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Server
 * Changed to Singleton to connect it with a gui
 */
public class Server {

    private static Server server;

    protected Socket socket;
    protected ServerSocket serverSocket;
    private static Game game;
    public PlayerList players;
    public HashMap<Integer, HandleClient> CLIENTS = new HashMap<>();
    //Message type instead of Strings
    public LinkedBlockingQueue<Message> messages;
    public int uniqueID = 0;
    public Boolean alive;
    public BooleanProperty online;
    MessageCreator messageCreator;
    private final String protocolVersion;
    private final Logger logger = LogManager.getLogger(Server.class);

    private Server() {
        logger.debug("Server constructor is called");
        this.messageCreator = new MessageCreator();
        this.players = new PlayerList();
        this.protocolVersion = "Version 1.0";
        this.online = new SimpleBooleanProperty(true);
        this.messages = new LinkedBlockingQueue<>();
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
        server.setOnline(true);
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

        //Changes
        Thread writeMessages = new Thread() {
            public void run() {
                while (true) {
                    try {
                        Message message = messages.take();
                        boolean isPrivate = message.getMessageBody().isPrivate();
                        //Added goodbye: added message type in HandleClient to Message instead of String because of protocol (Chat)
                        if (message.getMessageType() == MessageType.Goodbye) {
                            int id = message.getMessageBody().getClientID();
                            for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
                                if (client.getKey() != id) {
                                    client.getValue().write(message);
                                }
                            }
                        }
                        //Changed group messages: will only be displayed to other clients, not to yourself
                        if (!isPrivate) {
                            int id = message.getMessageBody().getFrom();
                            for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
                                if (client.getKey() != id) {
                                    client.getValue().write(message);
                                }
                            }
                        }
                        //added private message to work in chat
                        //still need other players clientID to send message
                        if (isPrivate) {
                            int toUser = message.getMessageBody().getFrom();
                            for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
                                if (client.getKey() == toUser) {
                                    client.getValue().write(message);
                                }
                            }
                        }
                    } catch (Exception e) {
                            logger.warn("An Exception occured: " + e);
                    }
                }
            }
        };
        writeMessages.start();
    }

    public void sendPlayerValuesToAll(int clientID, Message message) {
        for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
            if (client.getKey() != clientID) {
                client.getValue().write(message);
            }
        }
    }

    public void sendConnectionLost(int clientID, Message message) {
        for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
            if (client.getKey() != clientID) {
                client.getValue().write(message);
            }
        }
    }

    public static Game getGameInstance(){
        if (game == null) {
            game = new Game();
        }
        return game;
    }

    public synchronized int getUniqueID() {
        return uniqueID++;
    }
    public String getProtocolVersion() {return this.protocolVersion;}

    //Added to stop server (for gui)
    public void stopServer() throws IOException {
        setOnline(false);
        this.alive = false;
        if (serverSocket != null) {
            serverSocket.close();
        }
    }
}

