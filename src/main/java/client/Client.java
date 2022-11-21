package client;

import client.Controller;
import communication.JsonSerializer;
import communication.Message;
import communication.MessageCreator;
import communication.MessageType;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

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
    private String username = "";
    private final LoginScreen loginController;
    private Controller chatController;
    private boolean accessible = false;

    public Client(String address, int port, LoginScreen controller) {

        this.MESSAGES = new LinkedBlockingQueue<>();
        this.loginController = controller;
        messageCreator = new MessageCreator();

        try {
            socket = new Socket(address, port);
            in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());

        } catch (Exception e) {
            System.out.println("error " + e.getMessage());
        }


        ReadMessagesFromServer server = new ReadMessagesFromServer(socket);
        new Thread(server).start();
    }


    public void sendUsernameToServer(String username) {
        try {
            out.writeUTF(JsonSerializer.serializeJson(new Message(username, username)));
            setUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setUsername(String username) {
        this.username=username;
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
                in = new DataInputStream(
                        new BufferedInputStream(socket.getInputStream()));
                out = new DataOutputStream(socket.getOutputStream());


                while (socket.isConnected()) {
                    try {
                        Message message = JsonSerializer.deserializeJson(in.readUTF(), Message.class);
                        if (message.getMessageType().equals(MessageType.USERNAME_COMMAND)) {
                            if (message.getMessage().equals("accepted")) {
                                loginController.goToChat(username);
                            }
                            else {
                                setUsername("");
                                loginController.setMessage(message.getMessage());
                            }
                        }
                        if (accessible&&!message.getMessageType().equals(MessageType.USERNAME_COMMAND)){
                            MESSAGES.put(message.getMessage());
                        }

                    } catch (IOException | InterruptedException e) {
                        //e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
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

    public void sendMessageToServer(String messageToServer) {
        if (messageToServer.equals("bye")) {
            try {
                //TODO: Rework generateMessage method
                //out.writeUTF(JsonSerializer.serializeJson(messageCreator.generateMessage(username, messageToServer)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            closeApplication();
        }
        else {
            try {
                //out.writeUTF(JsonSerializer.serializeJson(messageCreator.generateMessage(username, messageToServer)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void closeApplication() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            in.close();
            out.close();
            System.exit(0);
        } catch (IOException e) {
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


