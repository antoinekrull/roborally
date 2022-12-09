package client.model;

import client.connection.Client;
import client.connection.ClientService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * Model for login
 *
 * @author Tobias
 * @version 0.1
 */
public class ModelUser {

    private static ModelUser modelUser;
    private BooleanProperty connected;

    private String username;
    private int userID;
    private StringProperty usernameProperty;

    private Client client;

    private ModelUser() {
        client = Client.getInstance();
        connected = new SimpleBooleanProperty();
        connected.bind(client.connectedProperty());
        usernameProperty = new SimpleStringProperty("");
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

    public boolean getConnection() { return this.connected.get(); }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int clientID) {
        this.userID = clientID;
    }

    public StringProperty usernameProperty() {
        return usernameProperty;
    }

    /*public void sendUsername() {
        client.sendUsername(usernameProperty.get());
    }*/
}
