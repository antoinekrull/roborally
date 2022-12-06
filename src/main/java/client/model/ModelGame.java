package client.model;

import client.ScreenController;
import client.client.ClientService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Model for Game
 *
 * @author Tobias
 * @version 1.0
 */
public class ModelGame {

    private static ModelGame modelGame;

    private ClientService clientService;

    private ArrayList<String> maps;
    private ArrayList<String> users;

    private StringProperty robot;
    private BooleanProperty readyToPlay;



    private ModelGame() {
        clientService = ClientService.getInstance();
        this.robot = new SimpleStringProperty("");
        this.readyToPlay = new SimpleBooleanProperty();
        this.maps = new ArrayList<>();
        this.users = new ArrayList<>();

    }

    public static ModelGame getInstance() {
        if (modelGame == null) {
            modelGame = new ModelGame();
        }
        return modelGame;
    }

    public StringProperty robotProperty() {
        return robot;
    }

    public String getRobot() {
        return robot.get();
    }

    public BooleanProperty readyToPlayProperty() {
        return readyToPlay;
    }

    public ArrayList<String> getMaps() {
        return maps;
    }
    public ArrayList<String> getUsers() {
        return users;
    }

    public void setMaps(ArrayList<String> maps) {
        this.maps = maps;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public void sendRobotSelection(int clientID) throws IOException {
        boolean result = clientService.sendSelection(clientID, modelGame.getRobot());
        if (!result) {
            //setSelectionResult("Please choose another robot");
        }
        else {
            ScreenController.switchScene("lobby.fxml");
        }
    }

    public void setPlayerStatus(int userID) {
        //client.sendPlayerStatus or client.sendMessageToServer(userID, MessageType); true/false
    }

    /*

    public void getPlayerList() {

    }

    public void getMapList() {

    }
    */

}
