package client.model;

import client.connection.Client;
import com.fasterxml.jackson.core.JsonProcessingException;
import game.board.GameBoard;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Model for Game
 *
 * @author Tobias
 * @version 0.1
 */
public class ModelGame {

    private static ModelGame modelGame;
    public GameBoard gameBoard;
    public SimpleStringProperty robotProperty;

    private Client client;

    private ObservableList<String> maps;
    private ObservableList<String> users;

    private int robot;
    private BooleanProperty readyToPlay;



    private ModelGame() {
        this.robot = -1;
        this.robotProperty = new SimpleStringProperty("");
        this.readyToPlay = new SimpleBooleanProperty();
        this.maps = FXCollections.observableArrayList();
        this.users = FXCollections.observableArrayList();
        this.gameBoard = new GameBoard();
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

    public int getRobot() {
        return robot;
    }
    public void addUser(String user) {this.users.add(user);}

    public void setRobot(int robot) {
        this.robot = robot;
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
