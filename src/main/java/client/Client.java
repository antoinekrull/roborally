package client;

import communication.JsonSerializer;
import communication.Message;
import communication.MessageCreator;
import communication.MessageType;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.javatuples.Pair;
import org.javatuples.Triplet;

/**
 * @author Antoine, Dominik, Tobias
 * @version 1.0
 *
 */

/**
 * Client Class
 * Communicating with Server and chat to send and receive messages from server and puts messages on chat.
 * Therefore putting incoming messages to LinkedBlockingQueue and reading from it.
 */

public class Client {
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private final LinkedBlockingQueue<String> MESSAGES;
    MessageCreator messageCreator;
    private String name = "";
    private final MainMenu loginController;
    private Controller chatController;
    private boolean accessible = false;

    private String protocolVersion = "Version 0.1";
    private String group = "KnorrigeKorrelate";
    private boolean isAI = false;
    private int clientID;
    private ArrayList<Triplet<Integer, String, Integer>> otherPlayers = new ArrayList<>();
    private ArrayList<Pair<Integer, Boolean>> otherPlayersStatus = new ArrayList<>();

    public Client(String address, int port, MainMenu controller) {

        this.MESSAGES = new LinkedBlockingQueue<>();
        this.loginController = controller;
        messageCreator = new MessageCreator();

        try {
            socket = new Socket(address, port);
            in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());

        } catch (Exception e) {
            loginController.setMessage("Couldn't connect to server");
            //TODO: create error message to close window because there is no connection to server
        }

        ReadMessagesFromServer server = new ReadMessagesFromServer(socket);
        new Thread(server).start();
    }

    //public void sendUsernameToServer(String username) {
    //    try {
    //        out.writeUTF(JsonSerializer.serializeJson(new Message(username, username)));
    //        setName(username);
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    }
    //}
    public void setName(String name) {
        this.name = name;
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
                                System.out.println("client alive");
                                sendAliveMessage();
                            }
                            if(message.getMessageType().equals(MessageType.HelloClient)){
                                System.out.println(message.getMessageBody().getProtocol());
                                sendHelloServerMessage(group, isAI, protocolVersion);
                            }
                            if(message.getMessageType().equals(MessageType.Welcome)){
                                clientID = message.getMessageBody().getClientID();
                                System.out.println("This is my ID: "+clientID);
                            }
                            if(message.getMessageType().equals(MessageType.PlayerAdded)){
                                otherPlayers.add(new Triplet<>(message.getMessageBody().getClientID(),
                                        message.getMessageBody().getName(),
                                        message.getMessageBody().getFigure()));
                            }
                            if(message.getMessageType().equals(MessageType.PlayerStatus)){
                                otherPlayersStatus.add(new Pair<>(message.getMessageBody().getClientID(),
                                        message.getMessageBody().isReady()));
                            }
                            if(message.getMessageType().equals(MessageType.ReceivedChat)){
                                MESSAGES.put(message.getMessageBody().getMessage());
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
                                //GameBoard board = new GameBoard();
                                //board.createBoard(message.getMessageBody());
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
                loginController.setMessage("Couldn't connect to server");
            }

        }

    }

    public void readMessageToClientChat() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;
                while(true) {
                    while(accessible) {
                        try {
                            message = MESSAGES.take();
                            chatController.addMessageToChat(message);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
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
    public void sendPrivateMessage(String message, int clientID){
        sendMessageToServer(messageCreator.generateSendChatMessage(message, clientID));
    }
    public void sendGroupMessage(String message){
        sendMessageToServer(messageCreator.generateSendChatMessage(message));
    }

    public void sendMessageToServer(Message message) {
        try {
            out.writeUTF(JsonSerializer.serializeJson(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeApplication() {
        try {
            Thread.sleep(100);
            in.close();
            out.close();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getChat(Controller chatController){
        this.chatController=chatController;
    }
    public void enterChat(Boolean state) {
        accessible = state;
        readMessageToClientChat();
    }
}


