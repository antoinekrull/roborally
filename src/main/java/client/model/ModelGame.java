package client.model;

import client.connection.Client;
import client.playerlist.PlayerList;
import com.fasterxml.jackson.core.JsonProcessingException;
import game.board.Board;
import game.board.Tile;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * Model for game
 *
 * @author Tobias
 * @version 0.1
 */
public class ModelGame {

    private static ModelGame modelGame;
    private Client client;
    private Board gameBoard;
    private SimpleIntegerProperty robotProperty;
    private SimpleStringProperty errorMessage;
    private ObservableList<String> maps;
    private BooleanProperty readyToPlay;
    private ArrayList<ArrayList<ArrayList<Tile>>> gameMap;
    private PlayerList playerList;



    private ModelGame() {
        client = Client.getInstance();
        this.robotProperty = new SimpleIntegerProperty();
        this.readyToPlay = new SimpleBooleanProperty();
        this.maps = FXCollections.observableArrayList("Dizzy Highway", "Extra Crispy", "Lost Bearings", "Death Trap");
        this.gameBoard = new Board();
        this.playerList = client.getPlayerList();
        this.errorMessage = new SimpleStringProperty();
        errorMessage.bind(client.errorMessageProperty());
    }

    public static ModelGame getInstance() {
        if (modelGame == null) {
            modelGame = new ModelGame();
        }
        return modelGame;
    }

    public PlayerList getPlayerList() {
        return playerList;
    }

    public SimpleIntegerProperty robotProperty() {
        return robotProperty;
    }

    //public void addUser(String user) {this.users.add(user);}

    public void setRobotProperty(int robotProperty) {
        this.robotProperty.set(robotProperty);
    }
    public void createMap(String jsonMap) throws JsonProcessingException {
        gameBoard.createBoard(jsonMap);
        this.gameMap = gameBoard.getBoard();
    }
    public SimpleStringProperty errorMessageProperty() {
        return errorMessage;
    }
    public BooleanProperty readyToPlayProperty() {
        return readyToPlay;
    }

    public ObservableList<String> getMaps() {
        return maps;
    }

    public ArrayList<ArrayList<ArrayList<Tile>>> getGameMap() {
        this.gameMap = gameBoard.getBoard();
        return gameMap;
    }
    public void sendPlayerInformation(String nickname) {
        client.sendPlayerValuesMessage(nickname, robotProperty.get());
    }

    public void setPlayerStatus(int userID) {
        //client.sendPlayerStatus or client.sendMessageToServer(userID, MessageType); true/false
    }

}
