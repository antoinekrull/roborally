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

    private StringProperty groupMessage;

    private final LinkedBlockingQueue<String> MESSSAGES;

    private NotifyChangeSupport notifyChangeSupport;

    private Client client;


    private ModelChat() {
        client = Client.getInstance();
        notifyChangeSupport = NotifyChangeSupport.getInstance();
        groupMessage = new SimpleStringProperty("");
        this.MESSSAGES = new LinkedBlockingQueue<>();
        textfieldProperty = new SimpleStringProperty("");
        groupMessage.addListener((observable, oldValue, newValue) -> {
            try {
                MESSSAGES.put(groupMessage.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            notifyChangeSupport.notifyInstance();
        });
        groupMessage.bind(client.messageProperty());
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
            this.MESSSAGES.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public LinkedBlockingQueue<String> getMessages() {
        return MESSSAGES;
    }

    public void sendMessage(int userID) {
        client.sendGroupMessage(textfieldProperty.get());
    }

    /*public void sendMessage(int userID) {
        clientService.sendMessageToServer(userID, modelChat.getTextfieldProperty());
    }*/



}
