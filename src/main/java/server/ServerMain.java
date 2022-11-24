package server;

import communication.JsonSerializer;
import communication.Message;
import communication.ConcreteMessage;
import javafx.application.Application;
import javafx.stage.Stage;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
        public PlayerList players = new PlayerList();
        protected PlayerList activePlayers = new PlayerList();
        //Network Communication
        protected final ArrayList<HandleClient> CLIENTS = new ArrayList<>();
        public final LinkedBlockingQueue<String> messages;
        Server self = this;

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
                            HandleClient client = new HandleClient(socket.getRemoteSocketAddress().toString(), socket.getPort(), socket, self);
                            client.setUsername("");
                            synchronized (CLIENTS) {
                                CLIENTS.add(client);
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
                            for (HandleClient client : CLIENTS) {
                                client.write(new ConcreteMessage(client.getUsername(), "> " + message));
                            }
                        } catch (Exception e) {
                            System.out.println("Error is it here " + e.getMessage());
                        }
                    }
                }
            };
            writeMessages.start();
        }


        public List<HandleClient> getClients() {
            return this.CLIENTS;
        }


    }
    public static void main(String[] args) {
        launch(args);
    }
}

