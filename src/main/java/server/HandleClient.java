package server;

import communication.JsonSerializer;
import communication.Message;
import communication.MessageType;
import game.player.Player;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HandleClient implements Runnable{

    private DataInputStream in = null;
    private DataOutputStream out = null;
    private int clientID;

    private boolean alive;
    public String address;
    public int port;
    public Socket socket;
    private String username;
    private ServerMain.Server server;

    /**
     * Thread which handles the logged in clients.
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
            System.out.println("Error in HandleClient constructor " + e.getMessage());
        }
    }

    public boolean containsName(final ArrayList<HandleClient> list, final String name) {
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUsername().equals(name)) {
                        return true;
                }
            }
        }
        return false;
    }

    public boolean containsId(final ArrayList<HandleClient> list, final int id) {
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getClientID() == id) {
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
            System.out.println("Error in write method " + e.getMessage());
        }
    }

    public void writeTo(int id, Message message) {
        try {
            for (server.HandleClient client : server.CLIENTS) {
                if (client.getClientID() == id) {
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
    /*
    public void grantAccess(String username) {
        setUsername(username);
        Message access = new Message();
        access.setMessageType(MessageType.PlayerValues);
        access.setMessageBody().setUsername("Server");
        access.setMessageBody().setMessage("accepted");
        writeTo(username, access);
    }
    */
    /**
     * Send info to client when username is already taken.
     *
     /* @param username Client who tries to log in.
     */
    /*
    public void denyAccess(String username) {
        setUsername(username);
        Message access = new Message();
        access.setMessageType(MessageType.PlayerValues);
        access.setMessageBody("Server");
        access.setMessageBody("The username " + username + " is already taken, choose another one");
        writeTo(username, access);
        setUsername("");
    }
     */

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
        alive = true;

        Runnable helloRunnable = new Runnable() {
            public void run() {
                System.out.println("Test 5 seconds");
            }
        };
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 5, 5, TimeUnit.SECONDS);

        try {
            String username ="";

            while (username == "") {
                if(JsonSerializer.deserializeJson(in.readUTF(), Message.class).getMessageType() == MessageType.PlayerValues) {
                    username = JsonSerializer.deserializeJson(in.readUTF(), Message.class).getMessageBody().getName();
                    if (!containsName(server.CLIENTS, username)) {
                        //grantAccess(username);
                    } else {
                        //denyAccess(username);
                        username = "";
                    }
                }
            }

                //Thread needs to sleep so that the chat form can load and the user sees his welcome message
                Thread.sleep(1000);
                //welcome message to server
                String message = this.username +  " has entered the chat";
                try {
                    server.messages.put(message);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }

            String line = "";
            while (alive) {
                Message incomingMessage = JsonSerializer.deserializeJson(this.in.readUTF(), Message.class);
                try {
                    if (incomingMessage.getMessageType() == MessageType.SendChat && incomingMessage.getMessageBody().getTo() == -1) {
                        line = incomingMessage.getMessageBody().getMessage();
                        String line_formatted = this.username + ":  " + line;

                        if (!this.username.isBlank()) {
                            server.messages.put(line_formatted);
                        }
                    } else if (incomingMessage.getMessageType() == MessageType.SendChat) {
                        if (containsId(server.CLIENTS, incomingMessage.getMessageBody().getTo())) {
                            writeTo(incomingMessage.getMessageBody().getTo(), incomingMessage);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(this.username);
                }
            }
            String goodbyeMessage = "Server: " + this.username + " has left the chat!";
            server.players.remove(username);
            server.messages.put(goodbyeMessage);

            server.CLIENTS.remove(this);
            this.in.close();
            this.out.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Client disconnected");
        }
    }

    public int getClientID() {
        return clientID;
    }
    public void setClientID(int id) {
        clientID = id;
    }
    public boolean isAlive() {
        return alive;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
