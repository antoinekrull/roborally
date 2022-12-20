package server;

import communication.JsonSerializer;
import communication.Message;
import communication.MessageCreator;
import game.player.Player;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Antoine, Moritz, Dominic, Firas
 * @version 1.0
 */

public class ServerMain extends Application {
    int port;

    @Override
    public void start(Stage primaryStage) {

        //port
        port = 3000;

        //Server Stuff
        new Server(port);

    }

    public static class Server {

        protected Socket socket = null;
        protected ServerSocket server = null;
        public HashMap<Integer, Player> players = new HashMap<>();
        public HashMap<Integer, HandleClient> CLIENTS = new HashMap<>();
        //public final LinkedBlockingQueue<Message> messages;
        public int uniqueID = 0;

        MessageCreator messageCreator = new MessageCreator();
        Server self = this;
        private String protocolVersion = "Version 0.1";

        /**
         * Initialises server.
         *
         * @param port Port where the server listens to.
         */

        public Server(int port) {
            //this.messages = new LinkedBlockingQueue<>();

            Thread acceptClients = new Thread() {
                public void run() {
                    try {
                        server = new ServerSocket(port);

                        while (true) {
                            socket = server.accept();

                            //handle multithreading for clients
                            int uniqueID = getUniqueID();
                            HandleClient client = new HandleClient(socket.getRemoteSocketAddress().toString(), socket.getPort(), socket, self, uniqueID);
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

            /*
            //should be redundant, because linkedblockingqueue is not necessary anymore
            Thread writeMessages = new Thread() {
                public void run() {
                    while (true) {
                        try {
                            Message message = messages.take();
                            int id = message.getMessageBody().getFrom();
                            for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
                                if (client.getKey() != id) {
                                    client.getValue().write(message);
                                }
                                System.out.println("ID: " + id + "\n" + "clientValue:" + client.getValue() + "\n" + "clientKey:" + client.getKey() +
                                        "\n" + "clientKey:" + client.getKey());
                            }
                        } catch (Exception e) {
                            System.out.println("Error is it here " + e.getMessage());
                        }
                    }
                }
            };
            writeMessages.start();

             */
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

        public synchronized int getUniqueID(){
            return uniqueID++;
        }
        public String getProtocolVersion(){return this.protocolVersion;}

    }
    public static void main(String[] args) {
        launch(args);
    }
}

