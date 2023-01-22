package client.model;

import client.connection.Client;
import client.changesupport.NotifyChangeSupport;
import client.player.ClientPlayerList;
import com.fasterxml.jackson.core.JsonProcessingException;
import game.Game;
import game.board.Board;
import game.board.Tile;
import java.io.IOException;
import java.util.ArrayList;

import javafx.beans.property.*;
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
    private IntegerProperty x;
    private IntegerProperty y;
    private boolean yChanged;
    private boolean xChanged;
    private IntegerProperty movementX;
    private IntegerProperty movementY;
    private IntegerProperty playerID;
    private boolean movementXChanged;
    private boolean movementYChanged;
    private boolean playerIDChanged;
    private BooleanProperty timer;
    private BooleanProperty activePlayer;
    private ObservableList<String> maps;
    private BooleanProperty readyToPlay;
    private ArrayList<ArrayList<ArrayList<Tile>>> gameMap;
    private ObservableList<String> myHandCards;
    private ClientPlayerList clientPlayerList;


    private Game game;


    private ModelGame() {
        client = Client.getInstance();
        this.notifyChangeSupport = NotifyChangeSupport.getInstance();
        this.xChanged = false;
        this.yChanged = false;
        this.movementYChanged = false;
        this.movementXChanged = false;
        this.playerIDChanged = false;
        this.activePlayer = new SimpleBooleanProperty(false);
        this.activePlayer.bind(client.activePlayerProperty());
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
        this.gameBoard = client.getBoard();
        this.clientPlayerList = client.getPlayerList();
        this.errorMessage = new SimpleStringProperty();
        errorMessage.bind(client.errorMessageProperty());
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
                notifyChangeSupport.updateProgrammingHandCards();
            }
        });

        this.timer = new SimpleBooleanProperty();
        timer.bind(client.timerProperty());
        timer.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                notifyChangeSupport.startTimer();
            }
        });

        this.movementX = new SimpleIntegerProperty();
        movementX.bind(client.movementXProperty());
        movementX.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                movementXChanged = true;
                check_X_Y_ID();
            }
        });

        this.movementY = new SimpleIntegerProperty();
        movementY.bind(client.movementYProperty());
        movementY.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                movementYChanged = true;
                check_X_Y_ID();
            }
        });

        this.playerID = new SimpleIntegerProperty();
        playerID.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                playerIDChanged = true;
                check_X_Y_ID();
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
    public void setMaps(ObservableList<String> maps) {
        this.maps = maps;
    }
    public ObservableList<Integer> getReadyList() {
        return readyList;
    }
    public int getX() {
        return x.get();
    }

    public IntegerProperty xProperty() {
        return x;
    }

    public int getY() {
        return y.get();
    }

    public IntegerProperty yProperty() {
        return y;
    }

    public int getMovementX() {
        return movementX.get();
    }

    public IntegerProperty movementXProperty() {
        return movementX;
    }

    public int getMovementY() {
        return movementY.get();
    }

    public IntegerProperty movementYProperty() {
        return movementY;
    }

    public int getPlayerID() {
        return playerID.get();
    }

    public IntegerProperty playerIDProperty() {
        return playerID;
    }

    public boolean isMovementXChanged() {
        return movementXChanged;
    }

    public boolean isMovementYChanged() {
        return movementYChanged;
    }

    public boolean isPlayerIDChanged() {
        return playerIDChanged;
    }

    public BooleanProperty activePlayerProperty() {
        return activePlayer;
    }

    public void check_X_Y_ID() {
        if (movementXChanged && movementYChanged && playerIDChanged) {
            notifyChangeSupport.robotSetPosition(movementX.get(), movementY.get(), playerID.get());
            movementYChanged = false;
            movementXChanged = false;
            playerIDChanged = false;
        }
    }

    public void check_x_y_changed() {
        if (xChanged && yChanged) {
            notifyChangeSupport.robotSetPosition(xProperty().get(), yProperty().get(), robotProperty.get());
            xChanged = false;
            yChanged = false;
        }
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
