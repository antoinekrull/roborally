package client.model;

import client.connection.Client;
import client.connection.NotifyChangeSupport;
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

    private StringProperty clientMessage;

    private LinkedBlockingQueue<String> messages;

    private NotifyChangeSupport notifyChangeSupport;

    private Client client;


    private ModelChat() {
        client = Client.getInstance();
        notifyChangeSupport = new NotifyChangeSupport();
        clientMessage = new SimpleStringProperty("");
        this.messages = new LinkedBlockingQueue<>();
        textfieldProperty = new SimpleStringProperty("");
        clientMessage.addListener((observable, oldValue, newValue) -> {
            try {
                messages.put(clientMessage.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            notifyChangeSupport.notifyInstance();
        });
        clientMessage.bind(client.messageProperty());
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

    public String getTextfieldProperty() {
        return textfieldProperty.get();
    }

    public void putMessage(String message) {
        try {
            this.messages.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public LinkedBlockingQueue<String> getMessages() {
        return messages;
    }

    /*public void sendMessage(int userID) {
        clientService.sendMessageToServer(userID, modelChat.getTextfieldProperty());
    }*/



}
