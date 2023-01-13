package client.connection;

import communication.JsonSerializer;
import communication.Message;
import communication.MessageCreator;
import communication.MessageType;
import game.board.Board;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import server.connection.PlayerList;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


/**
 *
 * Client Class
 * Communicating with Server and chat to send and receive messages from server and puts messages on chat.
 * Therefor putting incoming messages to LinkedBlockingQueue and reading from it.
 *
 * @author Antoine, Dominik, Tobias
 * @version 0.1
 *
 */

public class Client {

    private static Client client = null;
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private BooleanProperty connected;
    private BooleanProperty accepted;
    private BooleanProperty isAI;
    private ObjectProperty<Message> message;
    private IntegerProperty userID;

    MessageCreator messageCreator;
    String address = "localhost";
    int port = 3000;
    private String protocolVersion = "Version 0.1";
    private String group = "KnorrigeKorrelate";
    private ObservableList<String> playersOnline;
    private ObservableList<String> playersToChat;
    private ObservableList<Pair<String, Integer>> playerIDs;


    private Client() {
        this.messageCreator = new MessageCreator();
        this.message = new SimpleObjectProperty<>();
        this.userID = new SimpleIntegerProperty();
        this.isAI = new SimpleBooleanProperty();
        this.playersOnline = FXCollections.observableArrayList();
        this.playersToChat = FXCollections.observableArrayList("All");
        this.playerIDs = FXCollections.observableArrayList();
        this.connected = new SimpleBooleanProperty();
        this.accepted = new SimpleBooleanProperty();
        connectServer();
    }

    public static Client getInstance() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    public ObservableList<String> getPlayersOnline() {
        return playersOnline;
    }

    public ObservableList<String> getPlayersToChat() {
        return playersToChat;
    }

    public ObjectProperty<Message> messageProperty() {
        return message;
    }

    public void setMessage(Message message) {
        this.message.set(message);
    }

    public IntegerProperty userIDProperty() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID.set(userID);
    }

    public ObservableList<Pair<String, Integer>> getPlayerIDs() {
        return playerIDs;
    }

    public BooleanProperty isAIProperty() {
        return isAI;
    }

    public BooleanProperty connectedProperty() {
        return connected;
    }

    public BooleanProperty acceptedProperty() {
        return accepted;
    }

    public void setAcceptedProperty() {
        this.accepted.set(true);
    }

   public void addPlayer(String username, int clientID) {
        System.out.println("Client: addPlayer");
        System.out.println(username+ " " + client);
        this.playersOnline.add(username);
        this.playersToChat.add(username);
        this.playerIDs.add(new Pair<>(username, clientID));
   }

   public void setPlayer(PlayerList playerList) {
        ArrayList<String> playerNames = new ArrayList<>();
        ArrayList<Pair<String, Integer>> playerInfo = new ArrayList<>();
        for (int i = 0; i < playerList.size(); i++) {
            playerNames.add(playerList.get(i).getUsername());
            playerInfo.add(new Pair<>(playerList.get(i).getUsername(), playerList.get(i).getId()));
            System.out.println("Client: setPlayer Method:");
            System.out.println(playerList.get(i).getUsername());
            System.out.println(playerList.get(i).getId());
        }
        this.playersOnline.addAll(playerNames);
        this.playersToChat.addAll(playerNames);
        this.playerIDs.setAll(playerInfo);
   }

    private class ReadMessagesFromServer implements Runnable {
        DataInputStream in = null;
        DataOutputStream out = null;
        Socket socket;
        ReadMessagesFromServer(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            if (socket!=null) {
                try {
                    in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                    out = new DataOutputStream(socket.getOutputStream());
                    while (socket.isConnected()) {
                        try {
                            Message message = JsonSerializer.deserializeJson(in.readUTF(), Message.class);
                            if(message.getMessageType().equals(MessageType.Alive)){
                                sendAliveMessage();
                            }
                            if(message.getMessageType().equals(MessageType.HelloClient)){
                                System.out.println(message.getMessageBody().getProtocol());
                                sendHelloServerMessage(group, isAI.get(), protocolVersion);
                            }
                            if(message.getMessageType().equals(MessageType.Welcome)){
                                Client.this.setUserID(message.getMessageBody().getClientID());
                            }
                            if(message.getMessageType().equals(MessageType.Accepted)) {
                                Client.this.setAcceptedProperty();
                            }
                            if(message.getMessageType().equals(MessageType.PlayerAdded)){
                                int clientID = message.getMessageBody().getClientID();
                                String username = message.getMessageBody().getName();
                                Client.this.addPlayer(username, clientID);
                                System.out.println("Client: MessageAdded part");
                                System.out.println("ClientID: " + clientID + " and Username: " + username + "\n");
                                for (int i = 0; i < playersOnline.size(); i++) {
                                    System.out.println("PlayersOnline List:\n" + playersOnline.get(i));
                                }
                                System.out.println("\n");
                                for (int i = 0; i < playersToChat.size(); i++) {
                                    System.out.println("PlayersToChat List: \n" + playersToChat.get(i));
                                }
                                System.out.println("\n");
                                for (int i = 0; i < playerIDs.size(); i++) {
                                    System.out.println("PlayersIDs Pair List:\n" + "Player Username: " + playerIDs.get(i).getKey() + " Player ID: " + playerIDs.get(i).getValue());
                                }
                                System.out.println("\n");
                            }
                            if(message.getMessageType().equals(MessageType.PlayerList)) {
                                PlayerList playerList = message.getMessageBody().getPlayers();
                                Client.this.setPlayer(playerList);
                                System.out.println("Client: setPlayer Method:");
                                for(int i = 0; i < playerList.size(); i++) {
                                    System.out.println(playerList.get(i).getUsername());
                                }
                                for(int i = 0; i < playerList.size(); i++) {
                                    System.out.println(playerList.get(i).getId());
                                }
                            }
                            if(message.getMessageType().equals(MessageType.PlayerStatus)){

                            }
                            if (message.getMessageType().equals(MessageType.SelectMap)){
                                String[] maps = message.getMessageBody().getAvailableMaps();
                                for (int i = 0; i < maps.length; i++) {
                                    System.out.println(maps[i]);
                                }
                                sendMapMessage("hier soll mal die Map rein dann");

                            }
                            if(message.getMessageType().equals(MessageType.ReceivedChat)){
                                Client.this.setMessage(message);
                            }
                            if(message.getMessageType().equals(MessageType.Error)){
                                System.out.println(message.getMessageBody().getMessage());
                            }
                            if(message.getMessageType().equals(MessageType.CardPlayed)){

                            }
                            if(message.getMessageType().equals(MessageType.CurrentPlayer)){

                            }
                            if(message.getMessageType().equals(MessageType.StartingPointTaken)){

                            }
                            if(message.getMessageType().equals(MessageType.GameStarted)){

                                Board board = new Board();
                                board.createBoard(message.getMessageBody().getGameMap());
                            }
                            if(message.getMessageType().equals(MessageType.YourCards)){

                            }
                            if(message.getMessageType().equals(MessageType.NotYourCards)){

                            }
                            //if (message.getMessageType().equals(MessageType.USERNAME_COMMAND)) {
                            //    if (message.getMessage().equals("accepted")) {
                            //        loginController.goToChat(name);
                            //    } else {
                            //        setName("");
                            //        loginController.setMessage(message.getMessage());
                            //    }
                            //}
                            //if (accessible && !message.getMessageType().equals(MessageType.USERNAME_COMMAND)) {
                            //    MESSAGES.put(message.getMessage());
                            //}

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }else {

            }

        }

    }

    //maybe are these methods redundant, but they are kept until everything is implemented for them to be there
    public void sendAliveMessage(){
        sendMessageToServer(messageCreator.generateAliveMessage());
    }
    public void sendHelloServerMessage(String group, boolean isAI, String protocolVersion){
        sendMessageToServer(messageCreator.generateHelloServerMessage(group, isAI, protocolVersion));
    }
    public void sendPlayerValuesMessage(String name, int figure){
        sendMessageToServer(messageCreator.generatePlayerValuesMessage(name, figure));
    }
    public void sendSetStatusMessage(boolean ready){
        sendMessageToServer(messageCreator.generateSetStatusMessage(ready));
    }
    public void sendPrivateMessage(String message, int to){
        sendMessageToServer(messageCreator.generateSendChatMessage(message, to));
        System.out.println("private message");
    }
    public void sendGroupMessage(String message){
        sendMessageToServer(messageCreator.generateSendChatMessage(message));
        System.out.println("group message");
    }
    public void sendMapMessage(String message) {
        sendMessageToServer(messageCreator.generateMapSelectedMessage("DizzyHighway"));
    }

    public void sendMessageToServer(Message message) {
        try {
            out.writeUTF(JsonSerializer.serializeJson(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connectServer() {
        if (!connected.get()) {
            try {
                socket = new Socket(address, port);
                in = new DataInputStream(
                        new BufferedInputStream(socket.getInputStream()));
                out = new DataOutputStream(socket.getOutputStream());
                connected.set(true);
                ReadMessagesFromServer server = new ReadMessagesFromServer(socket);
                new Thread(server).start();
            } catch (Exception e) {
                socket = null;
                in = null;
                out = null;
            }
        }
    }
    public void reconnect() {
        if (!connected.get()) {
            connectServer();
        }
    }
}



