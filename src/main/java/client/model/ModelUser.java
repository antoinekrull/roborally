package client.model;

import client.connection.Client;
import client.connection.NotifyChangeSupport;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


/**
 * Model for login
 *
 * @author Tobias
 * @version 0.1
 */
public class ModelUser {

    private static ModelUser modelUser;
    private BooleanProperty connected;
    private BooleanProperty accepted;
    private String username;
    private IntegerProperty userID;
    private StringProperty usernameProperty;
    private BooleanProperty isAI;
    private NotifyChangeSupport notifyChangeSupport;
    private Client client;


    private ModelUser() {
        client = Client.getInstance();
        notifyChangeSupport = NotifyChangeSupport.getInstance();
        connected = new SimpleBooleanProperty();
        connected.bind(client.connectedProperty());
        usernameProperty = new SimpleStringProperty("");
        userID = new SimpleIntegerProperty();
        userID.bindBidirectional(client.userIDProperty());
        isAI = new SimpleBooleanProperty();
        isAI.bind(client.isAIProperty());
        accepted = new SimpleBooleanProperty();
        accepted.bind(client.acceptedProperty());
        accepted.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (accepted.get()) {
                    notifyChangeSupport.notifyInstance();
                }
            }
        });
    }

    public static ModelUser getInstance() {
        if (modelUser == null) {
            modelUser = new ModelUser();
        }
        return modelUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean getConnection() {return this.connected.get();}
    public void reconnect() {client.reconnect();}

    public void sendPlayerValues(String name, int figure) {
        client.sendPlayerValuesMessage(name, figure);
    }
    public void sendSetStatus(Boolean status) {
        client.sendSetStatusMessage(status);
    }
    public void sendMapSelected(String map) {
        client.sendMapSelected(map);
    }

    public IntegerProperty userIDProperty() {
        return userID;
    }

    public StringProperty usernameProperty() {
        return usernameProperty;
    }

    public boolean getVerification() {
        return this.accepted.get();
    }

    public void connect() {
        client.connectServer();
    }
}
