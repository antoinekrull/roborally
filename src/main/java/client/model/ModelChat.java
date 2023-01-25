package client.model;

import client.connection.Client;
import client.changesupport.NotifyChangeSupport;
import communication.Message;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.LinkedBlockingQueue;


/**
 * Model for chat
 *
 * @author Tobias
 * @version 0.1
 */
public class ModelChat {

    private static ModelChat modelChat;
    private Client client;
    private final Logger logger = LogManager.getLogger(ModelChat.class);
    private NotifyChangeSupport notifyChangeSupport;
    private StringProperty textfieldProperty;
    private ObjectProperty<Message> chatMessage;
    private ObjectProperty<Message> logMessage;
    private final LinkedBlockingQueue<Message> GAMELOGMESSAGES;
    private final LinkedBlockingQueue<Message> MESSSAGES;


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
                notifyChangeSupport.messageArrived();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        this.GAMELOGMESSAGES = new LinkedBlockingQueue<>();
        logMessage = new SimpleObjectProperty<>();
        logMessage.bind(client.gameLogMessageProperty());
        logMessage.addListener(new ChangeListener<Message>() {
            @Override
            public void changed(ObservableValue<? extends Message> observable, Message oldValue, Message newValue) {
                try {
                    GAMELOGMESSAGES.put(client.getGameLogMessage());
                    notifyChangeSupport.logMessageArrived();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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

    public LinkedBlockingQueue<Message> getGAMELOGMESSAGES() {
        return GAMELOGMESSAGES;
    }

    public void sendGroupMessage() {
        client.sendGroupMessage(textfieldProperty.get());
        logger.debug("send group message");
    }

    public void sendPrivateMessage(int toUser) {
        client.sendPrivateMessage(textfieldProperty.get(), toUser);
        logger.debug("send private message");
    }
}

