package server.connection;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import communication.JsonSerializer;
import communication.Message;
import communication.MessageCreator;
import communication.MessageType;
import game.Game;
import game.player.Player;
import game.player.Robot;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HandleClient implements Runnable{

    private DataInputStream in = null;
    private DataOutputStream out = null;
    private int threadID;
    private int clientID;
    private boolean exit;
    private boolean accepted;
    private boolean alive;
    private SimpleBooleanProperty serverStatus;
    public String address;
    public int port;
    public Socket socket;
    private String username;
    //private ServerMain.Server serverMain;

    private Server server;

    private MessageCreator messageCreator;
    private Game game;

    /**
     * Thread which handles the logged in clients.
     *
     * @param address
     * @param port
     * @param socket
     */
    public HandleClient(String address, int port, Socket socket, Server server, int threadID) {
        this.address = address;
        this.port = port;
        this.socket = socket;
        this.server = server;
        this.threadID = threadID;
        this.messageCreator = new MessageCreator();
        //closes handler when stopServer method is called
        serverStatus = new SimpleBooleanProperty();
        serverStatus.bind(server.onlineProperty());
        serverStatus.addListener(event -> {
            if (!serverStatus.get()) {
                try {
                    closeHandler();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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


    //messages stored in linkedblockingqueue (thread using linkedblockingqueue calls write method which should be enough)
    /*
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
     */

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
        //writeTo(getClientID(), messageCreator.generateHelloClientMessage(server.getProtocolVersion()));
        write(messageCreator.generateHelloClientMessage(server.getProtocolVersion()));

        try {

            String line;

            //Accept client if his protocol version is correct
            while(!accepted) {
                Message incomingMessage = JsonSerializer.deserializeJson(this.in.readUTF(), Message.class);
                try {
                    if (incomingMessage.getMessageType() == MessageType.HelloServer) {
                        if(incomingMessage.getMessageBody().getProtocol().equals(server.getProtocolVersion())){
                            accepted = true;
                            //writeTo(clientID, messageCreator.generateWelcomeMessage(clientID));
                            write(messageCreator.generateWelcomeMessage(clientID));
                        } else{
                            write(messageCreator.generateErrorMessage("Please check your protocol version. This server runs with: "+ server.getProtocolVersion()));
                            closeConnection();
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
                line = incomingMessage.getMessageBody().getMessage();
                String line_formatted = this.username + ":  " + line;
                try {
                    //String changed to Message type (protocol, from: sender): added to linkedblockingqueue
                    if (incomingMessage.getMessageType() == MessageType.SendChat && incomingMessage.getMessageBody().getTo() == -1) {
                        int clientID = getClientID();
                        server.messages.put(messageCreator.generateReceivedChatMessage(line_formatted, clientID, false));
                        //server.broadcast(clientID, messageCreator.generateReceivedChatMessage(line_formatted, clientID, false));
                    } else if (incomingMessage.getMessageType() == MessageType.SendChat) {
                        if (server.CLIENTS.containsKey(incomingMessage.getMessageBody().getTo())) {
                            //String changed to Message type (protocol, from: to send): added to linkedblockingqueue
                            int toUser = incomingMessage.getMessageBody().getTo();
                            server.messages.put(messageCreator.generateReceivedChatMessage(line_formatted, toUser, true));
                            //int toUser = getClientID();
                            //server.sendTo(toUser, messageCreator.generateReceivedChatMessage(line_formatted, toUser, true));
                            //writeTo(incomingMessage.getMessageBody().getTo(), incomingMessage);
                        }
                    } else if (incomingMessage.getMessageType() == MessageType.Alive) {
                        setAlive(true);

                    } else if (incomingMessage.getMessageType() == MessageType.MapSelected) {
                        String map = incomingMessage.getMessageBody().getMap();
                        String fileName = "/maps/"+map+".json";
                        InputStream file = Objects.requireNonNull(HandleClient.class.getResourceAsStream(fileName));
                        BufferedReader content = new BufferedReader(new InputStreamReader(file));
                        String jsonmap = content.lines().collect(Collectors.joining());
                        write(messageCreator.generateMapSelectedMessage(map));

                    } else if (incomingMessage.getMessageType() == MessageType.PlayerValues) {
                        System.out.println("HandleClient: PlayerValues");
                        System.out.println("message incoming: " + incomingMessage.getMessageType());
                        System.out.println("content: " + incomingMessage.getMessageBody().getFigure() + " " + incomingMessage.getMessageBody().getName());
                        this.username = incomingMessage.getMessageBody().getName();
                        System.out.println("Username registered from server: " + this.username + "\n");
                        int figure = incomingMessage.getMessageBody().getFigure();
                        if (server.players.size() == 0) {
                            System.out.println("zero players reached");

                            Message robotAcceptedMessage = messageCreator.generatePlayerAddedMessage(this.username, figure, getClientID());
                            write(robotAcceptedMessage);

                            server.players.add(new Player(getClientID(), incomingMessage.getMessageBody().getName()
                                    , new Robot(incomingMessage.getMessageBody().getFigure())));
                            System.out.println("Added because 0 players\n");
                        }
                        else {
                            boolean taken = false;
                            for (int i = 0; i < server.players.size(); i++) {
                                if (server.players.get(i).getRobot().getFigure() == figure) {
                                    taken = true;
                                    write(messageCreator.generateErrorMessage("Your figure was already chosen. Choose another one."));
                                    System.out.println("Double figures\n");
                                    break;
                                }
                            }
                            if (!taken) {

                                Message robotAcceptedMessage = messageCreator.generatePlayerAddedMessage(this.username, figure, getClientID());
                                write(robotAcceptedMessage);

                                server.players.add(new Player(getClientID(), incomingMessage.getMessageBody().getName()
                                        , new Robot(incomingMessage.getMessageBody().getFigure())));
                                Message playerAddedMessage = messageCreator.generatePlayerAddedMessage(this.username, figure, getClientID());
                                server.sendPlayerValuesToAll(getClientID(), playerAddedMessage);

                                for(int i = 0; i < server.players.size(); i++) {
                                    if(server.players.get(i).getId() != getClientID()) {
                                        Message addOtherPlayer = messageCreator.generatePlayerAddedMessage(server.players.get(i).getUsername(),server.players.get(i).getRobot().getFigure(), server.players.get(i).getId());
                                        write(addOtherPlayer);
                                    }
                                }
                                System.out.println("new player\n");
                            }
                        }
                    } else if(incomingMessage.getMessageType() == MessageType.SetStatus) {
                        boolean ready = incomingMessage.getMessageBody().isReady();
                        if(ready){
                            game.addReady(clientID);
                            if(game.getFirstReadyID() == clientID){
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
            server.players.remove(threadID);
            server.CLIENTS.remove(threadID);
            this.in.close();
            this.out.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Error while closing Connection" + e.getMessage());
        }

    }

    //when server is stopped, close handlers
    public void closeHandler() throws IOException {
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
        if (socket != null) {
            socket.close();
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
