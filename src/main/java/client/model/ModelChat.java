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
    private final LinkedBlockingQueue<Message> GAME_LOG_MESSAGES;
    private final LinkedBlockingQueue<Message> CHAT_MESSSAGES;


    private ModelChat() {
        client = Client.getInstance();
        notifyChangeSupport = NotifyChangeSupport.getInstance();
        chatMessage = new SimpleObjectProperty<>();
        this.CHAT_MESSSAGES = new LinkedBlockingQueue<>();
        textfieldProperty = new SimpleStringProperty("");
        chatMessage.bind(client.messageProperty());
        chatMessage.addListener((observable, oldValue, newValue) -> {
            try {
                CHAT_MESSSAGES.put(client.messageProperty().getValue());
                notifyChangeSupport.chatMessageArrived();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        this.GAME_LOG_MESSAGES = new LinkedBlockingQueue<>();
        logMessage = new SimpleObjectProperty<>();
        logMessage.bind(client.gameLogMessageProperty());
        logMessage.addListener(new ChangeListener<Message>() {
            @Override
            public void changed(ObservableValue<? extends Message> observable, Message oldValue, Message newValue) {
                try {
                    GAME_LOG_MESSAGES.put(client.getGameLogMessage());
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

    public LinkedBlockingQueue<Message> getCHAT_MESSSAGES() {
        return CHAT_MESSSAGES;
    }

    public LinkedBlockingQueue<Message> getGAME_LOG_MESSAGES() {
        return GAME_LOG_MESSAGES;
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

