package client.client;

//import communication.ConcreteMessage;
import communication.JsonSerializer;
import communication.MessageCreator;
import communication.MessageType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


/**
 * Client Singleton to send messages to server and receive messages from server
 *
 * @author Tobias
 * @version 1.0
 */



public class ClientService {

    private static ClientService clientService;
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    private final NotifyChangeSupport notifyChangeSupport;

    StringProperty groupMessage = new SimpleStringProperty("");
    StringProperty directMessage = new SimpleStringProperty("");
    MessageCreator messageCreator;
    //private PropertyChangeSupport support;


    private ClientService() {

        this.notifyChangeSupport = new NotifyChangeSupport();

        this.messageCreator = new MessageCreator();
        //support = new PropertyChangeSupport(this);

        try {
            socket = new Socket("localhost", 3000);
            in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //new Thread(() -> readMessageFromServer(in)).start();

    }

    public static ClientService getInstance() {
        if (clientService == null) {
            clientService = new ClientService();
        }
        return clientService;
    }

    /*
    public String getGroupMessage() {
        return groupMessage.get();
    }

    public StringProperty groupMessageProperty() {
        return groupMessage;
    }

    public String getDirectMessage() {
        return directMessage.get();
    }

    public StringProperty directMessageProperty() {
        return directMessage;
    }

    private void readMessageFromServer(DataInputStream in) {

        try {
            while (socket.isConnected()) {
                try {
                    ConcreteMessage concreteMessage = JsonSerializer.deserializeJson(in.readUTF(), ConcreteMessage.class);
                    if (concreteMessage.getMessageType().equals(MessageType.USERNAME_COMMAND)) {
                        if (concreteMessage.getMessage().equals("accepted")) {
                            //loginController.goToChat(username);
                        } else {
                            //setUsername("");
                            //loginController.setMessage(concreteMessage.getMessage());
                        }
                    }
                    if (accessible && !concreteMessage.getMessageType().equals(MessageType.USERNAME_COMMAND)) {
                        MESSAGES.put(concreteMessage.getMessage());
                    }

                } catch (IOException | InterruptedException e){
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        while (socket.isConnected()) {
            try {
                ConcreteMessage concreteMessage = JsonSerializer.deserializeJson(in.readUTF(), ConcreteMessage.class);
                if (concreteMessage.getMessageType().equals(MessageType.GROUP_CHAT)) {
                    groupMessage.set(concreteMessage.getMessage());
                    notifyChangeSupport.newGroupMessage("groupMessage");
                }
                if (concreteMessage.getMessageType().equals(MessageType.DIRECT_MESSAGE)) {
                    directMessage.set(concreteMessage.getMessage());
                    notifyChangeSupport.newDirectMessage("directMessage");
                }

            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static ClientService getInstance() {
            if (clientService == null) {
                clientService = new ClientService();
            }
            return clientService;
        }

        public int connect () {
            int userID = 0;
            return userID;
        }

        public void sendUsername (String username){
            try {
                out.writeUTF(JsonSerializer.serializeJson(new ConcreteMessage(username, username)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    public void sendMessageToServer(int userID, String messageToServer) {
        try {
            out.writeUTF(JsonSerializer.serializeJson(messageCreator.generateMessage(String.valueOf(userID), messageToServer)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean sendSelection (int userID, String robot) {
        return true;
    }


    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }
*/
}



