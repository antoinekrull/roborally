package server;

import communication.JsonSerializer;
import communication.Message;
import communication.MessageType;
import game.card.Deck;
import game.player.Player;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Antoine, Moritz, Dominic, Firas
 * @version 1.0
 */

public class ServerMain extends Application {
    int port;
//TODO: Seperate server and HandleClient classes
    @Override
    public void start(Stage primaryStage) {

        //port
        port = 3000;

        //Server Stuff
        new Server(port);

    }

    public static class Server {

        private Socket socket = null;
        private ServerSocket server = null;
        public PlayerList players = new PlayerList();
        private PlayerList activePlayers = new PlayerList();
        //Network Communication
        private final List<HandleClient> CLIENTS = Collections.synchronizedList(new ArrayList<>());
        public final LinkedBlockingQueue<String> messages;
        public boolean isRunning = false;
        int turnCount = 0;
        int winningScore = 0;
        public Deck sessionDeck;
        public Deck publicDiscardPile;

        public Player currentPlayer;

        Server self = this;

        /**
         * Initialises server.
         *
         * @param port Port where the server listens to.
         */
        public Server(int port) {
            this.messages = new LinkedBlockingQueue<>();
        //TODO: Fix Client issues in server

            Thread acceptClients = new Thread() {

                public void run() {
                    try {
                        server = new ServerSocket(port);

                        while (true) {

                            socket = server.accept();

                            //handle multithreading for clients
                            HandleClient client = new HandleClient(socket.getRemoteSocketAddress().toString(), socket.getPort(), socket);
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
                                client.write(new Message(client.username, "> " + message));
                            }
                        } catch (Exception e) {
                            System.out.println("Error is it here " + e.getMessage());
                        }
                    }
                }
            };
            writeMessages.start();
        }


        //TODO: think about thread writing to clients, probably multiple times the same output
        public List<HandleClient> getClients() {
            return this.CLIENTS;
        }

        public class HandleClient implements Runnable {

            private DataInputStream in = null;
            private DataOutputStream out = null;
            public String address;
            public int port;
            public Socket socket;
            private String username;

            /**
             * Thread which handles the loged in clients.
             *
             * @param address
             * @param port
             * @param socket
             */
            public HandleClient(String address, int port, Socket socket) {
                this.address = address;
                this.port = port;
                this.socket = socket;
                try {
                    this.in = new DataInputStream(
                            new BufferedInputStream(socket.getInputStream()));
                    this.out = new DataOutputStream(socket.getOutputStream());
                } catch (Exception e) {
                    System.out.println("Error here " + e.getMessage());
                }
            }

            public boolean containsName(final List<server.HandleClient> list, final String name) {
                if (list.size() > 0) {
                    for (int i = 0; i < list.size() - 1; i++) {
                        if (list.get(i).getUsername().equals(name)) {
                            return true;
                        }
                    }
                }
                return false;
            }

            public void write(Message message) {
                try {
                    this.out.writeUTF(JsonSerializer.serializeJson(message));
                } catch (IOException e) {
                    System.out.println("Error here " + e.getMessage());
                }
            }

            /**
             * Server can write to a specific client.
             *
             * @param username The targeted client.
             * @param message  Message the server send to the client.
             */
            //TODO: Prints multiple times, can sometimes crash threads
            public void writeTo(String username, Message message) {
                try {
                    for (HandleClient client : CLIENTS) {
                        if (client.getUsername().equals(username)) {
                            client.out.writeUTF(JsonSerializer.serializeJson(message));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /**
             * Send info to client that he is connected to server.
             *
             * @param username Client who is connecting.
             */
            public void grantAccess(String username) {
                setUsername(username);
                Message access = new Message();
                access.setMessageType(MessageType.USERNAME_COMMAND);
                access.setUsername("Server");
                access.setMessage("accepted");
                writeTo(username, access);
            }

            /**
             * Send info to client when username is already taken.
             *
             * @param username Client who tries to log in.
             */
            public void denyAccess(String username) {
                setUsername(username);
                Message access = new Message();
                access.setMessageType(MessageType.USERNAME_COMMAND);
                access.setUsername("Server");
                access.setMessage("The username " + username + " is already taken, choose another one");
                writeTo(username, access);
                setUsername(null);
            }

            @Override
            public String toString() {
                return getUsername();
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getUsername() {
                return this.username;
            }

            /**
             * Runs the server and game logic.
             */
            public void run() {
                try {
                    String username = null;

                    while (username == null) {
                        username = JsonSerializer.deserializeJson(in.readUTF(), Message.class).getUsername();
                        if (!containsName(CLIENTS, username)) {
                            grantAccess(username);
                        } else {
                            denyAccess(username);
                            username = null;
                        }
                    }
                    if (this.username.isBlank()) {
                        //TODO: Review this part of the code
                        out.writeUTF(JsonSerializer.serializeJson(new Message("", "Connection Accepted"
                                + "\nWelcome " + username + "\nNOTE: SINCE YOU HAVEN'T PROVIDED AN USERNAME, " +
                                "YOU'RE IN MODE READ ONLY.")));
                    } else {
                        // Thread needs to sleep so that the chat form can load and the user sees his welcome message
                        Thread.sleep(1000);
                        //welcome message to server
                        String message = "Welcome " + this.username;
                        try {
                            messages.put(message);
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                    }

                    String line = "";
                    while (!line.equals("bye")) {
                        Message incomingMessage = JsonSerializer.deserializeJson(this.in.readUTF(), Message.class);
                        try {
                            if (incomingMessage.getMessageType() == MessageType.GROUP_CHAT) {
                                line = incomingMessage.getMessage();
                                String line_formatted = this.username + ":  " + line;

                                if (!this.username.isBlank()) {
                                    messages.put(line_formatted);
                                    //print to server
                                }
                            } else if (incomingMessage.getMessageType() == MessageType.DIRECT_MESSAGE) {
                                if (containsName(CLIENTS, incomingMessage.getTarget())) {
                                    writeTo(incomingMessage.getTarget(), incomingMessage);
                                } else {
                                    writeTo(incomingMessage.getUsername(), new Message("Server",
                                            "Invalid username, try again"));
                                }
                            } else if (incomingMessage.getMessageType() == MessageType.JOIN_SESSION) {
                                if (players.playerIsInList(incomingMessage.getUsername())) {
                                    messages.put(incomingMessage.getUsername() + " already in gamesession");
                                } else {
                                    players.add(new Player(incomingMessage.getUsername(), self));
                                    messages.put(incomingMessage.getUsername() + " joined the gamesession");
                                }
                            } else if (incomingMessage.getMessageType() == MessageType.LEAVE_SESSION) {
                                players.remove(players.getPlayerFromList(incomingMessage.getUsername()).getUsername());
                                if (players.playerIsInList(incomingMessage.getUsername())) {
                                    activePlayers.remove(players.getPlayerFromList(incomingMessage.getUsername()).getUsername());
                                    if (activePlayers.size() == 1) {
                                        writeTo(activePlayers.get(0).getUsername(), new Message("Server",
                                                "All other players left, please restart"));
                                    }
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(this.username);
                        }
                    }

                    try {
                        String goodbyeMessage = "Server:  Hasta la vista " + this.username;
                        //may not work since player not only identified by his username
                        players.remove(username);
                        messages.put(goodbyeMessage);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }

                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    CLIENTS.remove(this);
                    this.in.close();
                    this.out.close();
                    socket.close();
                } catch (Exception e) {
                    System.out.println("Error :" + e.getMessage());
                }
            }
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}

