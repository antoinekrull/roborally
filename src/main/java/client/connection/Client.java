package client.connection;

import client.player.PlayerList;
import communication.JsonSerializer;
import communication.Message;
import communication.MessageCreator;
import communication.MessageType;
import game.CollisionCalculator;
import game.Game;
import game.board.Board;
import client.player.Player;
import game.player.Robot;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.javatuples.Triplet;

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
    private StringProperty errorMessage;
    private Boolean prioPlayer = false;

    MessageCreator messageCreator;
    String address = "localhost";
    int port = 3000;
    private String protocolVersion = "Version 1.0";
    private String group = "KnorrigeKorrelate";
    private PlayerList playerList;
    private ObservableList<String> maps;


    private Client() {
        this.messageCreator = new MessageCreator();
        this.message = new SimpleObjectProperty<>();
        this.userID = new SimpleIntegerProperty();
        this.isAI = new SimpleBooleanProperty();
        this.maps = FXCollections.observableArrayList();
        this.connected = new SimpleBooleanProperty();
        this.accepted = new SimpleBooleanProperty();
        this.playerList = new PlayerList();
        this.errorMessage = new SimpleStringProperty();
    }

    public static Client getInstance() {
        if (client == null) {
            client = new Client();
        }
        return client;
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

    public BooleanProperty isAIProperty() {
        return isAI;
    }

    public BooleanProperty connectedProperty() {
        return connected;
    }
    public ObservableList<String> getMaps() {
        return maps;
    }

    public BooleanProperty acceptedProperty() {
        return accepted;
    }

    public void setAcceptedProperty() {
        this.accepted.set(true);
    }

    public PlayerList getPlayerList() {
        return playerList;
    }

    public String getErrorMessage() {
        return errorMessage.get();
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage.set(errorMessage);
    }

    private class ReadMessagesFromServer implements Runnable {
        DataInputStream in = null;
        DataOutputStream out = null;
        Socket socket;
        ReadMessagesFromServer(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                out = new DataOutputStream(socket.getOutputStream());
                String receiveMessage;
                while (true) {
                    if((receiveMessage =in.readUTF()) != null) {
                        Message message = JsonSerializer.deserializeJson(receiveMessage, Message.class);
                        if (message.getMessageType().equals(MessageType.Alive)) {
                            sendAliveMessage();
                        }
                        if (message.getMessageType().equals(MessageType.HelloClient)) {
                            sendHelloServerMessage(group, isAI.get(), protocolVersion);
                        }
                        if (message.getMessageType().equals(MessageType.Welcome)) {
                            Client.this.setUserID(message.getMessageBody().getClientID());
                        }
                        if (message.getMessageType().equals(MessageType.PlayerAdded)) {
                            int clientID = message.getMessageBody().getClientID();
                            if (Client.this.userIDProperty().get() == clientID) {
                                if(!Client.this.accepted.get()) {
                                    Client.this.setAcceptedProperty();
                                }
                            }
                            else {
                                String username = message.getMessageBody().getName();
                                int figure = message.getMessageBody().getFigure();
                                Platform.runLater(() -> Client.this.getPlayerList().add(new Player(clientID, username, new Robot(figure))));
                            }
                        }
                        if (message.getMessageType().equals(MessageType.PlayerStatus)) {

                        }
                        if (message.getMessageType().equals(MessageType.SelectMap)) {
                            prioPlayer = true;
                            //TODO: if client looses prio because he removes ready or looses connection, he isn`t allowed to change map
                            String[] temp = message.getMessageBody().getAvailableMaps();
                            for (int i = 0; i < temp.length; i++) {
                                maps.add(temp[i]);
                                System.out.println(maps.get(i));
                            }
                        }
                        if (message.getMessageType().equals(MessageType.MapSelected)) {
                            String map = message.getMessageBody().getMap();
                            Message mapMessage = messageCreator.generateSendChatMessage("Selected map: " + map);
                            Client.this.setMessage(mapMessage);
                        }
                        if (message.getMessageType().equals(MessageType.ReceivedChat)) {
                            Client.this.setMessage(message);
                        }
                        if (message.getMessageType().equals(MessageType.Error)) {
                            System.out.println(message.getMessageBody().getError());
                        }
                        if (message.getMessageType().equals(MessageType.CardPlayed)) {

                        }
                        if (message.getMessageType().equals(MessageType.CurrentPlayer)) {

                        }
                        if (message.getMessageType().equals(MessageType.StartingPointTaken)) {

                        }
                        if (message.getMessageType().equals(MessageType.GameStarted)) {
                            Board board = new Board();
                            board.createBoard(message.getMessageBody().getGameMap());
                        }
                        if (message.getMessageType().equals(MessageType.YourCards)) {

                        }
                        if (message.getMessageType().equals(MessageType.NotYourCards)) {

                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Lost connection to server");
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
        if (!ready) {
            prioPlayer = false;
        }
        sendMessageToServer(messageCreator.generateSetStatusMessage(ready));
    }
    public void sendPrivateMessage(String message, int to){
        sendMessageToServer(messageCreator.generateSendChatMessage(message, to));
    }
    public void sendGroupMessage(String message){
        sendMessageToServer(messageCreator.generateSendChatMessage(message));
    }
    public void sendMapSelected(String map) {
        if (prioPlayer) {
            sendMessageToServer(messageCreator.generateMapSelectedMessage(map));
        }
    }

    public void sendMessageToServer(Message message) {
        try {
            out.writeUTF(JsonSerializer.serializeJson(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connectServer() {
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



