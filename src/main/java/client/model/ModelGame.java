package client.model;

import client.connection.Client;
import client.changesupport.NotifyChangeSupport;
import client.player.ClientPlayerList;
import client.ui.RobotDirection;
import client.viewmodel.ViewModelGameWindow;
import com.fasterxml.jackson.core.JsonProcessingException;
import communication.Message;
import game.Game;
import game.board.Board;
import game.board.Tile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private ClientPlayerList clientPlayerList;
    private final Logger logger = LogManager.getLogger(ViewModelGameWindow.class);
    private NotifyChangeSupport notifyChangeSupport;
    private SimpleIntegerProperty robotProperty;
    private RobotDirection robotDirection;
    private SimpleIntegerProperty score;
    private SimpleStringProperty activePhase;
    private SimpleIntegerProperty energy;
    private BooleanProperty readyToPlay;
    private SimpleStringProperty currentPlayer;
    private BooleanProperty activePlayer;
    private ObservableList<Integer> readyList;
    private LinkedBlockingQueue<Message> PLAYER_MOVEMENTS;
    private ObjectProperty<Message> movement;
    private LinkedBlockingQueue<Message> GAME_EVENT_MESSAGES;
    private ObjectProperty<Message> gameEvent;
    private BooleanProperty timer;
    private SimpleStringProperty errorMessage;
    private ObservableList<String> maps;
    private ArrayList<ArrayList<ArrayList<Tile>>> gameMap;
    private ObservableList<String> myHandCards;
    private ObservableList<String> upgradeCards;


    private Game game;


    private ModelGame() {
        client = Client.getInstance();
        this.notifyChangeSupport = NotifyChangeSupport.getInstance();
        this.clientPlayerList = client.getPlayerList();
        this.readyList = FXCollections.observableArrayList();
        this.readyToPlay = new SimpleBooleanProperty();
        this.maps = client.getMaps();
        this.gameBoard = client.getBoard();
        this.robotProperty = new SimpleIntegerProperty();
        this.score = new SimpleIntegerProperty();
        score.bind(client.scoreProperty());
        this.energy = new SimpleIntegerProperty();
        energy.bind(client.energyProperty());
        this.activePhase = new SimpleStringProperty();
        activePhase.bind(client.activePhaseProperty());
        this.currentPlayer = new SimpleStringProperty();
        currentPlayer.bind(client.currentPlayerProperty());
        this.activePlayer = new SimpleBooleanProperty();
        this.activePlayer.bind(client.activePlayerProperty());
        //this.maps = FXCollections.observableArrayList(client.getMaps());
        this.errorMessage = new SimpleStringProperty();
        errorMessage.bind(client.errorMessageProperty());
        this.myHandCards = FXCollections.observableArrayList();
        myHandCards = client.getMyCards();
        myHandCards.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                logger.debug("changed list");
                notifyChangeSupport.updateProgrammingHandCards();
            }
        });

        this.upgradeCards = FXCollections.observableArrayList();

        this.PLAYER_MOVEMENTS = new LinkedBlockingQueue<>();
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
        this.movement = new SimpleObjectProperty<>();
        movement.bind(client.movementProperty());
        movement.addListener(new ChangeListener<Message>() {
            @Override
            public void changed(ObservableValue<? extends Message> observable, Message oldValue, Message newValue) {
                try {
                    PLAYER_MOVEMENTS.put(client.getMovement());
                    notifyChangeSupport.robotSetPosition();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        this.robotDirection = RobotDirection.EAST;

        this.GAME_EVENT_MESSAGES = new LinkedBlockingQueue<>();
        this.gameEvent = new SimpleObjectProperty<>();
        gameEvent.bind(client.gameEventMessageProperty());
        gameEvent.addListener(new ChangeListener<Message>() {
            @Override
            public void changed(ObservableValue<? extends Message> observable, Message oldValue, Message newValue) {
                try {
                    GAME_EVENT_MESSAGES.put(client.getGameEventMessage());
                    notifyChangeSupport.gameEventMessageArrived();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        this.timer = new SimpleBooleanProperty();
        timer.bind(client.timerProperty());
        timer.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                logger.debug("timer triggered");
                if (client.timerProperty().get()) {
                    logger.debug("client timerproperty POSITIVE: " + client.timerProperty().get());
                    notifyChangeSupport.startTimer();
                }
                else {
                    logger.debug("client timerproperty NEGATIVE: " + client.timerProperty().get());
                    notifyChangeSupport.stopTimer();
                }
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

    public ArrayList<ArrayList<ArrayList<Tile>>> getGameMap() {
        this.gameMap = gameBoard.getBoard();
        return gameMap;
    }

    public void createMap(String jsonMap) throws JsonProcessingException {
        gameBoard.createBoard(jsonMap);
        this.gameMap = gameBoard.getBoard();
    }

    public SimpleIntegerProperty robotProperty() {
        return robotProperty;
    }

    public void setRobotProperty(int robotProperty) {
        this.robotProperty.set(robotProperty);
    }

    public SimpleIntegerProperty scoreProperty() {
        return score;
    }

    public SimpleStringProperty activePhaseProperty() {
        return activePhase;
    }

    public SimpleStringProperty currentPlayerProperty() {
        return currentPlayer;
    }

    public ObservableList<Integer> getReadyList() {
        return readyList;
    }

    public BooleanProperty activePlayerProperty() {
        return activePlayer;
    }

    public BooleanProperty readyToPlayProperty() {
        return readyToPlay;
    }

    public ObservableList<String> getMaps() {
        return maps;
    }

    public void setMaps(ObservableList<String> maps) {
        this.maps = maps;
    }

    public SimpleIntegerProperty energyProperty() {
        return energy;
    }

    public ObservableList<String> getMyHandCards() {
        return myHandCards;
    }

    public ObservableList<String> getUpgradeCards() {
        return upgradeCards;
    }

    public void setUpgradeCards(ObservableList<String> upgradeCards) {
        this.upgradeCards = upgradeCards;
    }

    public RobotDirection getRobotDirection() {
        return robotDirection;
    }

    public void setRobotDirection(RobotDirection robotDirection) {
        this.robotDirection = robotDirection;
    }

    public LinkedBlockingQueue<Message> getPLAYER_MOVEMENTS() {
        return PLAYER_MOVEMENTS;
    }

    public LinkedBlockingQueue<Message> getGAME_EVENT_MESSAGES() {
        return GAME_EVENT_MESSAGES;
    }

    public SimpleStringProperty errorMessageProperty() {
        return errorMessage;
    }

    public void sendPlayerValues(String nickname) {
        client.sendPlayerValues(nickname, robotProperty.get());
    }

    public void sendStartTileCoordinates( int x, int y){
        client.sendStartingPoint(x, y);
    }

    public void sendPlayCard(String card) {
        client.sendPlayCard(card);
    }
    public void sendSelectedCard(String card, int register) {
        client.sendSelectedCard(card, register);
    }

    public void sendSelectedDamageCards(String card, int register) {
        client.sendSelectedDamageCards(card, register);
    }

    public void sendReturnCards(String[] returnCards) {
        client.sendReturnCards(returnCards);
    }

    public void sendDiscardSomeCards(String[] discardSome) {
        client.sendDiscardSome(discardSome);
    }

    public void sendBuyUpgrad(boolean isBuying, String card) {
        client.sendBuyUpgrade(isBuying, card);
    }

    public void sendChooseRegister(int register) {
        client.sendChooseRegister(register);
    }

    public void sendRebootDirection(String direction) {
        client.sendRebootDirection(direction);
    }

    public void sendAIMessage(boolean AI) {
        client.sendAIMessage(AI);
    }
}
