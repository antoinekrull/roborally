package client.model;

import client.connection.Client;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * Model for Game
 *
 * @author Tobias
 * @version 0.1
 */
public class ModelGame {

    private static ModelGame modelGame;
    public SimpleStringProperty robotProperty;

    private Client client;

    private ObservableList<String> maps;
    private ObservableList<String> users;
    private ObservableList<String> usersToSelect;

    private String robot;
    private BooleanProperty readyToPlay;



    private ModelGame() {
        client = Client.getInstance();
        this.robot = "";
        this.robotProperty = new SimpleStringProperty("");
        this.readyToPlay = new SimpleBooleanProperty();
        this.maps = FXCollections.observableArrayList();
        this.users = FXCollections.observableArrayList(client.getPlayersOnline());
        this.usersToSelect = FXCollections.observableArrayList(client.getPlayersToChat());

        maps.add("Dizzy Highway");
        maps.add("KackJavaFX");
    }

    public static ModelGame getInstance() {
        if (modelGame == null) {
            modelGame = new ModelGame();
        }
        return modelGame;
    }

    public String getRobot() {
        return robot;
    }

    public void setRobot(String robot) {
        this.robot = robot;
    }

    public BooleanProperty readyToPlayProperty() {
        return readyToPlay;
    }

    public ObservableList<String> getMaps() {
        return maps;
    }

    public ObservableList<String> getUsers() {
        return users;
    }

    public ObservableList<String> getUsersToSelect() {
        return usersToSelect;
    }

    /*public void sendRobotSelection(int clientID) throws IOException {
        boolean result = clientService.sendSelection(clientID, modelGame.getRobot());
        if (!result) {
            //setSelectionResult("Please choose another robot");
        }
        else {
            ScreenController.switchScene("lobby.fxml");
        }
    }
    */

    public void setPlayerStatus(int userID) {
        //client.sendPlayerStatus or client.sendMessageToServer(userID, MessageType); true/false
    }

}
