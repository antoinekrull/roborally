package server;

import communication.JsonSerializer;
import communication.Message;
import communication.MessageCreator;
import communication.MessageType;
import game.Game;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HandleClient implements Runnable{

    private DataInputStream in = null;
    private DataOutputStream out = null;
    private int threadID;
    private int clientID;
    private boolean exit;
    private boolean accepted;

    private boolean alive;
    public String address;
    public int port;
    public Socket socket;
    private String username;
    private ServerMain.Server server;
    private MessageCreator messageCreator;
    private Game game;

    /**
     * Thread which handles the logged in clients.
     *
     * @param address
     * @param port
     * @param socket
     */
    public HandleClient(String address, int port, Socket socket, ServerMain.Server server, int threadID) {
        this.address = address;
        this.port = port;
        this.socket = socket;
        this.server = server;
        this.threadID = threadID;
        this.messageCreator = new MessageCreator();
        this.game = server.getGameInstance();
        try {
            this.in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("Error in HandleClient constructor " + e.getMessage());
        }
    }

    public boolean containsName(HashMap<Integer, HandleClient> list, final String name) {
        if (list.size() > 0) {
            for (Map.Entry<Integer, HandleClient> client : server.CLIENTS.entrySet()) {
                if (client.getValue().getUsername().equals(name)) {
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
            for (Map.Entry<Integer, HandleClient> client : server.CLIENTS.entrySet()) {
                if (client.getValue().getClientID() == id) {
                    client.getValue().out.writeUTF(JsonSerializer.serializeJson(message));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void aliveMessage(int id) {
        try {
            for (Map.Entry<Integer, HandleClient> client : server.CLIENTS.entrySet()) {
                if (client.getValue().getClientID() == id) {
                    client.getValue().out.writeUTF(JsonSerializer.serializeJson(messageCreator.generateAliveMessage()));
                }
            }
        } catch (Exception e) {
            System.out.println("Client "+ id +" couldn't be reached");
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
        setAlive(false);
        accepted = false;
        setClientID(threadID);

        //send protocol version to client
        writeTo(getClientID(), messageCreator.generateHelloClientMessage(server.getProtocolVersion()));

        try {
            String username ="";

            /*while (username == "") {
                if(JsonSerializer.deserializeJson(in.readUTF(), Message.class).getMessageType() == MessageType.PlayerValues) {
                    username = JsonSerializer.deserializeJson(in.readUTF(), Message.class).getMessageBody().getName();
                    if (!containsName(server.CLIENTS, username)) {
                        //grantAccess(username);
                    } else {
                        //denyAccess(username);
                        username = "";
                    }
                }
            }*/

            //Thread needs to sleep so that the chat form can load and the user sees his welcome message
            /*Thread.sleep(1000);
            //welcome message to server
            String message = this.username +  " has entered the chat";
            try {
                server.messages.put(message);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }*/

            String line = "";

            //Accept client if his protocol version is correct
            while(!accepted) {
                Message incomingMessage = JsonSerializer.deserializeJson(this.in.readUTF(), Message.class);
                try {
                    if (incomingMessage.getMessageType() == MessageType.HelloServer) {
                        if(incomingMessage.getMessageBody().getProtocol().equals(server.getProtocolVersion())){
                            accepted = true;
                            writeTo(clientID, messageCreator.generateWelcomeMessage(clientID));
                        } else{
                            //TODO message for client, that its version is not compatible
                            System.out.println("Client version is not correct: "
                                    +incomingMessage.getMessageBody().getProtocol()
                                    + " but it should be: "+server.getProtocolVersion());
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            Runnable sendAliveMessages = new Runnable() {
                public void run() {
                    while(!exit) {
                        try {
                            aliveMessage(clientID);
                            Thread.sleep(5000);
                            if (isAlive()) {
                                setAlive(false);
                            } else {
                                closeConnection();
                                exit = true;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            };
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(sendAliveMessages, 5, 5, TimeUnit.SECONDS);

            while (accepted) {
                Message incomingMessage = JsonSerializer.deserializeJson(this.in.readUTF(), Message.class);
                try {
                    if (incomingMessage.getMessageType() == MessageType.SendChat && incomingMessage.getMessageBody().getTo() == -1) {
                        line = incomingMessage.getMessageBody().getMessage();
                        String line_formatted = this.username + ":  " + line;
                        server.messages.put(line_formatted);
                    } else if (incomingMessage.getMessageType() == MessageType.SendChat) {
                        if (server.CLIENTS.containsKey(incomingMessage.getMessageBody().getTo())) {
                            writeTo(incomingMessage.getMessageBody().getTo(), incomingMessage);
                        }
                    } else if (incomingMessage.getMessageType() == MessageType.Alive) {
                        setAlive(true);
                    } else if (incomingMessage.getMessageType() == MessageType.MapSelected) {
                        //write(messageCreator.generateGameStartedMessage(game.board.BoardModels.DizzyHighwayGameBoard));
                    } else if (incomingMessage.getMessageType() == MessageType.PlayerValues) {
                        write(messageCreator.generatePlayerAddedMessage(incomingMessage.getMessageBody().getName(), incomingMessage.getMessageBody().getFigure(), this.clientID));
                    } else if(incomingMessage.getMessageType() == MessageType.SetStatus) {
                        boolean ready = incomingMessage.getMessageBody().isReady();
                        if(ready){
                            game.addReady(clientID);
                            if(game.getFirstReadyID()==clientID){
                                write(messageCreator.generateSelectMapMessage(game.getMaps()));
                            }
                        } else {
                            game.removeReady(clientID);
                        }
                        write(messageCreator.generatePlayerStatusMessage(clientID,ready));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(this.username);
                }
            }
        } catch (Exception e) {
            //System.out.println("Client disconnected");
        }
    }
    private void closeConnection(){
        try {
            String goodbyeMessage = "Server: " + this.threadID + " has left the chat!";
            System.out.println(goodbyeMessage);
            server.messages.put(goodbyeMessage);
            server.players.remove(threadID);
            server.CLIENTS.remove(threadID);
            this.in.close();
            this.out.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Error while closing Connection" + e.getMessage());
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
