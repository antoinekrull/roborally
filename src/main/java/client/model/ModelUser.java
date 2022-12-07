package client.model;

import client.client.ClientService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * Model for login
 *
 * @author Tobias
 * @version 1.0
 */
public class ModelUser {

    private static ModelUser modelUser;

    private String username;
    private int userID;
    private StringProperty usernameProperty;

    private ClientService clientService;

    private ModelUser() {
        clientService = ClientService.getInstance();
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
        clientService.sendUsername(usernameProperty.get());
    }*/
}
