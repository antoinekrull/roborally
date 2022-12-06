package client.model;

import client.client.ClientService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.concurrent.LinkedBlockingQueue;


/**
 * Model for chat
 *
 * @author Tobias
 * @version 1.0
 */
public class ModelChat {

    private static ModelChat modelChat;

    private StringProperty textfieldProperty;

    private LinkedBlockingQueue<String> messages;

    private ClientService clientService;


    private ModelChat() {
        clientService = ClientService.getInstance();
        textfieldProperty = new SimpleStringProperty("");

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

    public void sendMessage(int userID) {
        clientService.sendMessageToServer(userID, modelChat.getTextfieldProperty());
    }



}
