package client.model;

import client.connection.Client;
import com.fasterxml.jackson.core.JsonProcessingException;
import game.board.Board;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Model for game
 *
 * @author Tobias
 * @version 0.1
 */
public class ModelGame {

    private static ModelGame modelGame;
    public Board gameBoard;
    public SimpleIntegerProperty robotProperty;
    private Client client;
    private ObservableList<String> maps;
    private ObservableList<String> users;
    private ObservableList<String> usersToSelect;
    private BooleanProperty readyToPlay;



    private ModelGame() {
        client = Client.getInstance();
        this.robotProperty = new SimpleIntegerProperty();
        this.readyToPlay = new SimpleBooleanProperty();
        this.maps = FXCollections.observableArrayList("Dizzy Highway", "Extra Crispy", "Lost Bearings", "Death Trap");
        this.users = FXCollections.observableArrayList(client.getPlayersOnline());
        this.usersToSelect = FXCollections.observableArrayList(client.getPlayersToChat());
        this.maps = FXCollections.observableArrayList();
        this.users = FXCollections.observableArrayList();
        this.gameBoard = new Board();
        maps.add("Dizzy Highway");
        maps.add("KackJavaFX");
        /*users.add("Tomi");
        users.add("Firas");
        users.add("Molri");*/
    }

    public static ModelGame getInstance() {
        if (modelGame == null) {
            modelGame = new ModelGame();
        }
        return modelGame;
    }

    public SimpleIntegerProperty robotProperty() {
        return robotProperty;
    }
    public void addUser(String user) {this.users.add(user);}

    public void setRobotProperty(int robotProperty) {
        this.robotProperty.set(robotProperty);
    }
    public void createMap(Object jsonMap) throws JsonProcessingException {
        gameBoard.createBoard(jsonMap);
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
