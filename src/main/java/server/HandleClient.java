package server;

import communication.JsonSerializer;
import communication.ConcreteMessage;
import communication.MessageType;
import game.player.Player;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class HandleClient implements Runnable{

    private DataInputStream in = null;
    private DataOutputStream out = null;
    public String address;
    public int port;
    public Socket socket;
    private String username;
    private ServerMain.Server server;

    /**
     * Thread which handles the loged in clients.
     *
     * @param address
     * @param port
     * @param socket
     */
    public HandleClient(String address, int port, Socket socket, ServerMain.Server server) {
        this.address = address;
        this.port = port;
        this.socket = socket;
        this.server = server;
        try {
            this.in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("Error here " + e.getMessage());
        }
    }

    public boolean containsName(final List<HandleClient> list, final String name) {
        if (list.size() > 0) {
            for (int i = 0; i < list.size() - 1; i++) {
                if (list.get(i).getUsername().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void write(ConcreteMessage concreteMessage) {
        try {
            this.out.writeUTF(JsonSerializer.serializeJson(concreteMessage));
        } catch (IOException e) {
            System.out.println("Error here " + e.getMessage());
        }
    }

    /**
     * Server can write to a specific client.
     *
     * @param username The targeted client.
     * @param concreteMessage  Message the server send to the client.
     */
    //TODO: Prints multiple times, can sometimes crash threads
    public void writeTo(String username, ConcreteMessage concreteMessage) {
        try {
            for (server.HandleClient client : server.CLIENTS) {
                if (client.getUsername().equals(username)) {
                    client.out.writeUTF(JsonSerializer.serializeJson(concreteMessage));
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
        ConcreteMessage access = new ConcreteMessage();
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
        ConcreteMessage access = new ConcreteMessage();
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
                username = JsonSerializer.deserializeJson(in.readUTF(), ConcreteMessage.class).getUsername();
                if (!containsName(server.CLIENTS, username)) {
                    grantAccess(username);
                } else {
                    denyAccess(username);
                    username = null;
                }
            }
            if (this.username.isBlank()) {
                //TODO: Review this part of the code
                out.writeUTF(JsonSerializer.serializeJson(new ConcreteMessage("", "Connection Accepted"
                        + "\nWelcome " + username + "\nNOTE: SINCE YOU HAVEN'T PROVIDED AN USERNAME, " +
                        "YOU'RE IN MODE READ ONLY.")));
            } else {
                // Thread needs to sleep so that the chat form can load and the user sees his welcome message
                Thread.sleep(1000);
                //welcome message to server
                String message = "Welcome " + this.username;
                try {
                    server.messages.put(message);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }

            String line = "";
            while (!line.equals("bye")) {
                ConcreteMessage incomingConcreteMessage = JsonSerializer.deserializeJson(this.in.readUTF(), ConcreteMessage.class);
                try {
                    if (incomingConcreteMessage.getMessageType() == MessageType.GROUP_CHAT) {
                        line = incomingConcreteMessage.getMessage();
                        String line_formatted = this.username + ":  " + line;

                        if (!this.username.isBlank()) {
                            server.messages.put(line_formatted);
                            //print to server
                        }
                    } else if (incomingConcreteMessage.getMessageType() == MessageType.DIRECT_MESSAGE) {
                        if (containsName(server.CLIENTS, incomingConcreteMessage.getTarget())) {
                            writeTo(incomingConcreteMessage.getTarget(), incomingConcreteMessage);
                        } else {
                            writeTo(incomingConcreteMessage.getUsername(), new ConcreteMessage("Server",
                                    "Invalid username, try again"));
                        }
                    } else if (incomingConcreteMessage.getMessageType() == MessageType.JOIN_SESSION) {
                        if (server.players.playerIsInList(incomingConcreteMessage.getUsername())) {
                            server.messages.put(incomingConcreteMessage.getUsername() + " already in gamesession");
                        } else {
                            server.players.add(new Player(incomingConcreteMessage.getUsername(), server));
                            server.messages.put(incomingConcreteMessage.getUsername() + " joined the gamesession");
                        }
                    } else if (incomingConcreteMessage.getMessageType() == MessageType.LEAVE_SESSION) {
                        server.players.remove(server.players.getPlayerFromList(incomingConcreteMessage.getUsername()).getUsername());
                        if (server.players.playerIsInList(incomingConcreteMessage.getUsername())) {
                            server.activePlayers.remove(server.players.getPlayerFromList(incomingConcreteMessage.getUsername()).getUsername());
                            if (server.activePlayers.size() == 1) {
                                writeTo(server.activePlayers.get(0).getUsername(), new ConcreteMessage("Server",
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
                server.players.remove(username);
                server.messages.put(goodbyeMessage);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            server.CLIENTS.remove(this);
            this.in.close();
            this.out.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Error :" + e.getMessage());
        }
    }
}
