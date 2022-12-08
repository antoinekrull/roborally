package client.model;

import client.connection.Client;
import javafx.beans.property.*;

import java.util.ArrayList;

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

    private ArrayList<String> maps;
    private ArrayList<String> users;

    private String robot;
    private BooleanProperty readyToPlay;



    private ModelGame() {
        client = Client.getInstance();
        this.robot = "";
        this.robotProperty = new SimpleStringProperty("");
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

    public String getRobot() {
        return robot;
    }

    public void setRobot(String robot) {
        this.robot = robot;
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

    /*

    public void getPlayerList() {

    }

    public void getMapList() {

    }
    */

}
