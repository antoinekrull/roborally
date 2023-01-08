package client.connection;

import client.model.ModelGame;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import communication.JsonSerializer;
import communication.Message;
import communication.MessageCreator;
import communication.MessageType;
import game.Game;
import game.board.Board;
import game.board.NormalTile;
import game.board.TestTile;
import game.board.Tile;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private BooleanProperty isAI;
    private ObjectProperty<Message> message;
    private IntegerProperty userID;
    private Game game = new Game();
    private ArrayList<ArrayList<ArrayList<TestTile>>> map;

    MessageCreator messageCreator;
    String address = "localhost";
    int port = 3000;
    private String protocolVersion = "Version 0.1";
    private String group = "KnorrigeKorrelate";
    private ObservableList<String> playersOnline;
    private ObservableList<String> playersToChat;
    private ArrayList<Triplet<Integer, String, Integer>> otherPlayers = new ArrayList<>();
    private ArrayList<Pair<Integer, Boolean>> otherPlayersStatus = new ArrayList<>();

    private ModelGame modelGame;

    private Client() {
        this.messageCreator = new MessageCreator();
        this.message = new SimpleObjectProperty<>();
        this.userID = new SimpleIntegerProperty();
        this.isAI = new SimpleBooleanProperty();
        this.playersOnline = FXCollections.observableArrayList("Tomi", "Firas", "Molri", "Anto");
        this.playersToChat = FXCollections.observableArrayList("All", "Tomi", "Firas", "Molri", "Anto");
        connected = new SimpleBooleanProperty();
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

    public BooleanProperty isAIProperty() {
        return isAI;
    }

    public BooleanProperty connectedProperty() {
        return connected;
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
                            if(message.getMessageType().equals(MessageType.PlayerAdded)){
                                otherPlayers.add(new Triplet<>(message.getMessageBody().getClientID(),
                                        message.getMessageBody().getName(),
                                        message.getMessageBody().getFigure()));
                                //TODO process the data input like giving the player his robot and stuff like that
                            }
                            if(message.getMessageType().equals(MessageType.PlayerStatus)){
                                otherPlayersStatus.add(new Pair<>(message.getMessageBody().getClientID(),
                                        message.getMessageBody().isReady()));
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
    }
    public void sendGroupMessage(String message){
        sendMessageToServer(messageCreator.generateSendChatMessage(message));
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



