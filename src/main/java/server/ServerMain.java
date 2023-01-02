package server;

import client.connection.Client;
import communication.MessageCreator;
import game.Game;
import game.player.Player;
import javafx.application.Application;
import javafx.stage.Stage;
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
    private static Game game;

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
        public final LinkedBlockingQueue<String> messages;
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
            this.messages = new LinkedBlockingQueue<>();

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

            Thread writeMessages = new Thread() {
                public void run() {
                    while (true) {
                        try {
                            String message = messages.take();
                            for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
                                client.getValue().write(messageCreator.generateReceivedChatMessage(message, 0, false));
                            }
                        } catch (Exception e) {
                            System.out.println("Error is it here " + e.getMessage());
                        }
                    }
                }
            };
            writeMessages.start();
        }

        public synchronized int getUniqueID(){
            return uniqueID++;
        }
        public String getProtocolVersion(){return this.protocolVersion;}
        public static Game getGameInstance(){
            if (game == null) {
                game = new Game();
            }
            return game;

        }

    }
    public static void main(String[] args) {launch(args);}
}

