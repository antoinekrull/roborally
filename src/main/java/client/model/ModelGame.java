package client.model;

import client.connection.Client;
import com.fasterxml.jackson.core.JsonProcessingException;
import game.Game;
import game.board.Board;
import game.board.Tile;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.javatuples.Triplet;

import java.util.ArrayList;

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

    public void setMaps(ObservableList<String> maps) {
        this.maps = maps;
    }

    private ObservableList<String> maps;
    private ObservableList<String> users;

    public ObservableList<Integer> getReadyList() {
        return readyList;
    }

    private ObservableList<Integer> readyList;
    private ObservableList<String> usersToSelect;
    private ObservableList<Triplet<String, Integer, Boolean>> playerIDs;
    private BooleanProperty readyToPlay;
    private ArrayList<ArrayList<ArrayList<Tile>>> gameMap;
    private Game game;



    private ModelGame() {
        client = Client.getInstance();
        this.robotProperty = new SimpleIntegerProperty();
        this.readyList = FXCollections.observableArrayList();
        this.readyToPlay = new SimpleBooleanProperty();
        this.maps = client.getMaps();
        this.users = client.getPlayersOnline();
        this.usersToSelect = client.getPlayersToChat();
        this.playerIDs = client.getPlayerIDs();
        this.gameBoard = new Board();
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

    //public void addUser(String user) {this.users.add(user);}

    public void setRobotProperty(int robotProperty) {
        this.robotProperty.set(robotProperty);
    }
    public void createMap(String jsonMap) throws JsonProcessingException {
        gameBoard.createBoard(jsonMap);
        this.gameMap = gameBoard.getBoard();
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
    public ArrayList<ArrayList<ArrayList<Tile>>> getGameMap() {
        this.gameMap = gameBoard.getBoard();
        return gameMap;
    }

    public ObservableList<Triplet<String, Integer, Boolean>> getPlayerIDs() {
        return playerIDs;
    }

    public void sendPlayerInformation(String nickname) {
        client.sendPlayerValuesMessage(nickname, robotProperty.get());
    }

    public void setPlayerStatus(int userID) {
        //client.sendPlayerStatus or client.sendMessageToServer(userID, MessageType); true/false
    }

}
