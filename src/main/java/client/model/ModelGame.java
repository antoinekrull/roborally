package client.model;

import client.connection.Client;
import client.changesupport.NotifyChangeSupport;
import client.player.ClientPlayerList;
import client.viewmodel.ViewModelGameWindow;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
    private SimpleStringProperty robotAlignment;
    private SimpleIntegerProperty score;
    private BooleanProperty readyToPlay;
    private BooleanProperty activePlayer;
    private ObservableList<Integer> readyList;
    private LinkedBlockingQueue<Message> PLAYERMOVEMENTS;
    private ObjectProperty<Message> movement;
    private BooleanProperty timer;
    private SimpleStringProperty errorMessage;
    private ObservableList<String> maps;
    private ArrayList<ArrayList<ArrayList<Tile>>> gameMap;
    private ObservableList<String> myHandCards;


    private Game game;


    private ModelGame() {
        client = Client.getInstance();
        this.notifyChangeSupport = NotifyChangeSupport.getInstance();
        this.score = new SimpleIntegerProperty(0);
        score.bind(client.scoreProperty());
        this.activePlayer = new SimpleBooleanProperty(false);
        this.activePlayer.bind(client.activePlayerProperty());
        this.robotProperty = new SimpleIntegerProperty();
        this.readyList = FXCollections.observableArrayList();
        this.readyToPlay = new SimpleBooleanProperty();
        this.maps = client.getMaps();
        //this.maps = FXCollections.observableArrayList(client.getMaps());
        this.gameBoard = client.getBoard();
        this.clientPlayerList = client.getPlayerList();
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
        this.PLAYERMOVEMENTS = new LinkedBlockingQueue<>();
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
                    PLAYERMOVEMENTS.put(client.getMovement());
                    notifyChangeSupport.robotSetPosition();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        /*
        this.myHandCards = FXCollections.observableArrayList(client.getMyCards());
        this.x = new SimpleIntegerProperty();
        this.y = new SimpleIntegerProperty();
        x.bind(client.xProperty());
        y.bind(client.yProperty());

        x.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                xChanged = true;
                check_x_y_changed();
            }
        });

        y.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                yChanged = true;
                check_x_y_changed();
            }
        });

        myHandCards.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                while(c.next()) {
                    if(c.wasUpdated()) {
                        System.out.println("wasUpdated");
                        notifyChangeSupport.updateProgrammingHandCards();
                    }
                    if(c.wasAdded()) {
                        System.out.println("wasAdded");
                        notifyChangeSupport.updateProgrammingHandCards();
                    }
                    if(c.wasRemoved()) {
                        System.out.println("wasRemoved");
                        notifyChangeSupport.updateProgrammingHandCards();
                    }
                    if(c.wasPermutated()) {
                        System.out.println("wasPermutated");
                        notifyChangeSupport.updateProgrammingHandCards();
                    }
                    if(c.wasReplaced()) {
                        System.out.println("wasReplaced");
                        notifyChangeSupport.updateProgrammingHandCards();
                    }
                }
            }
        });

         */

        this.timer = new SimpleBooleanProperty();
        timer.bind(client.timerProperty());
        timer.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                notifyChangeSupport.startTimer();
            }
        });

        this.robotAlignment = new SimpleStringProperty();
        robotAlignment.bind(client.roboterAlignmentProperty());
        robotAlignment.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                notifyChangeSupport.setRobotAlignment();
            }
        });

        /*
        this.movementX = new SimpleIntegerProperty();
        movementX.bind(client.movementXProperty());
        movementX.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                movementXChanged = true;
                check_x_y_id();
            }
        });

        this.movementY = new SimpleIntegerProperty();
        movementY.bind(client.movementYProperty());
        movementY.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                movementYChanged = true;
                check_x_y_id();
            }
        });

        this.robotID = new SimpleIntegerProperty();
        robotID.bind(client.robotIDProperty());
        robotID.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                robotIDChanged = true;
                check_x_y_id();
            }
        });
         */
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
    public LinkedBlockingQueue<Message> getPLAYERMOVEMENTS() {
        return PLAYERMOVEMENTS;
    }
    public SimpleIntegerProperty robotProperty() {
        return robotProperty;
    }
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
    public void setMaps(ObservableList<String> maps) {
        this.maps = maps;
    }
    public ObservableList<Integer> getReadyList() {
        return readyList;
    }
    public BooleanProperty activePlayerProperty() {
        return activePlayer;
    }

    /*
    public void check_x_y_id() {
        if (movementXChanged && movementYChanged && robotIDChanged) {
            notifyChangeSupport.robotSetPosition(movementX.get(), movementY.get(), robotID.get());
            movementYChanged = false;
            movementXChanged = false;
            robotIDChanged = false;
        }
    }

    public void check_x_y_changed() {
        if (xChanged && yChanged) {
            notifyChangeSupport.robotSetPosition(xProperty().get(), yProperty().get(), robotProperty.get());
            xChanged = false;
            yChanged = false;
        }
    }

     */

    public void createMap(String jsonMap) throws JsonProcessingException {
        gameBoard.createBoard(jsonMap);
        this.gameMap = gameBoard.getBoard();
    }

    public void sendPlayerValues(String nickname) {
        client.sendPlayerValues(nickname, robotProperty.get());
    }

    public void sendStartTileCoordinates( int x, int y){
        client.sendStartingPoint(x, y);
    }

    public void sendSelectedCard(String card, int register) {
        client.sendSelectCard(card, register);
    }

    public void sendRegisterChosen(int register) {
        client.sendRegister(register);
    }

    public void sendReturnCards(String[] returnCards) {
        client.sendReturnCards(returnCards);
    }

    public void sendDiscardSomeCards(String[] discardSome) {
        client.sendDiscardSome(discardSome);
    }

}
