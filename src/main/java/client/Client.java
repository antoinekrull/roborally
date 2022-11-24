package client;

import communication.JsonSerializer;
import communication.ConcreteMessage;
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
            out.writeUTF(JsonSerializer.serializeJson(new ConcreteMessage(username, username)));
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
                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                out = new DataOutputStream(socket.getOutputStream());

                while (socket.isConnected()) {
                    try {
                        ConcreteMessage concreteMessage = JsonSerializer.deserializeJson(in.readUTF(), ConcreteMessage.class);
                        if (concreteMessage.getMessageType().equals(MessageType.USERNAME_COMMAND)) {
                            if (concreteMessage.getMessage().equals("accepted")) {
                                loginController.goToChat(username);
                            }
                            else {
                                setUsername("");
                                loginController.setMessage(concreteMessage.getMessage());
                            }
                        }
                        if (accessible&&!concreteMessage.getMessageType().equals(MessageType.USERNAME_COMMAND)){
                            MESSAGES.put(concreteMessage.getMessage());
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
        try {
            out.writeUTF(JsonSerializer.serializeJson(messageCreator.generateMessage(username, messageToServer)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeApplication() {
        ConcreteMessage bye = new ConcreteMessage(username, "Bye");
        bye.setMessageType(MessageType.USER_LOGOUT);
        try {
            out.writeUTF(JsonSerializer.serializeJson(bye));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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


