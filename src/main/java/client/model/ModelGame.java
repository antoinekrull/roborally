package client.model;

import client.connection.Client;
import client.connection.NotifyChangeSupport;
import client.player.ClientPlayerList;
import com.fasterxml.jackson.core.JsonProcessingException;
import game.Game;
import game.board.Board;
import game.board.Tile;
import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

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
    private ObservableList<Integer> readyList;
    private NotifyChangeSupport notifyChangeSupport;

    public void setMaps(ObservableList<String> maps) {
        this.maps = maps;
    }
    public ObservableList<Integer> getReadyList() {
        return readyList;
    }

    private ObservableList<String> maps;
    private BooleanProperty readyToPlay;
    private ArrayList<ArrayList<ArrayList<Tile>>> gameMap;
    private ObservableList<String> myHandCards;
    private ClientPlayerList clientPlayerList;

    private Game game;


    private ModelGame() {
        client = Client.getInstance();
        this.notifyChangeSupport = NotifyChangeSupport.getInstance();
        this.robotProperty = new SimpleIntegerProperty();
        this.readyList = FXCollections.observableArrayList();
        this.readyToPlay = new SimpleBooleanProperty();
        readyToPlay.bind(client.gameStartedProperty());
        readyToPlay.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (readyToPlay.get()){
                    try {
                        notifyChangeSupport.enterGame();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        this.maps = client.getMaps();
        //this.maps = FXCollections.observableArrayList(client.getMaps());
        this.gameBoard = new Board();
        this.clientPlayerList = client.getPlayerList();
        this.errorMessage = new SimpleStringProperty();
        errorMessage.bind(client.errorMessageProperty());
        this.myHandCards = FXCollections.observableArrayList(client.getMyCards());
        myHandCards.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                notifyChangeSupport.updateProgrammingHandCards();
            }
        });
    }

    public static ModelGame getInstance() {
        if (modelGame == null) {
            modelGame = new ModelGame();
        }
        return modelGame;
    }

    public ClientPlayerList getPlayerList() {
        return clientPlayerList;
    }

    public SimpleIntegerProperty robotProperty() {
        return robotProperty;
    }

    //public void addUser(String user) {this.users.add(user);}

    public void setRobotProperty(int robotProperty) {
        this.robotProperty.set(robotProperty);
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

    public ObservableList<String> getMyHandCards() {
        return myHandCards;
    }

    public void createMap(String jsonMap) throws JsonProcessingException {
        gameBoard.createBoard(jsonMap);
        this.gameMap = gameBoard.getBoard();
    }

    public void sendPlayerInformation(String nickname) {
        client.sendPlayerValuesMessage(nickname, robotProperty.get());
    }

    public void sendStarttileCoordinates( int x, int y){
        client.sendStartingPoint(x, y);
    }

    public void sendSelectedCard(String card, int register) {
        client.sendSelectCard(card, register);
    }

}
