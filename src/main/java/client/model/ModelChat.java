package client.model;

import client.connection.Client;
import client.connection.NotifyChangeSupport;
import communication.Message;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.concurrent.LinkedBlockingQueue;


/**
 * Model for chat
 *
 * @author Tobias
 * @version 0.1
 */
public class ModelChat {

    private static ModelChat modelChat;
    private StringProperty textfieldProperty;
    private ObjectProperty<Message> chatMessage;
    private final LinkedBlockingQueue<Message> MESSSAGES;
    private NotifyChangeSupport notifyChangeSupport;

    private Client client;


    private ModelChat() {
        client = Client.getInstance();
        notifyChangeSupport = NotifyChangeSupport.getInstance();
        chatMessage = new SimpleObjectProperty<>();
        this.MESSSAGES = new LinkedBlockingQueue<>();
        textfieldProperty = new SimpleStringProperty("");
        chatMessage.bind(client.messageProperty());
        chatMessage.addListener((observable, oldValue, newValue) -> {
            try {
                MESSSAGES.put(client.messageProperty().getValue());
                notifyChangeSupport.notifyInstance();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static ModelChat getInstance() {
        if (modelChat == null) {
            modelChat = new ModelChat();
        }
        return modelChat;
    }

    public StringProperty textfieldProperty() {
        return textfieldProperty;
    }

    public LinkedBlockingQueue<Message> getMESSSAGES() {
        return MESSSAGES;
    }

    public void sendGroupMessage() {
        client.sendGroupMessage(textfieldProperty.get());
        System.out.println("send group message");
    }

    public void sendPrivateMessage(int toUser) {
        client.sendPrivateMessage(textfieldProperty.get(), toUser);
        System.out.println("send private message");
    }
}

