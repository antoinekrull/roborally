package client.connection;

import client.player.ClientPlayer;
import client.player.ClientPlayerList;
import communication.JsonSerializer;
import communication.Message;
import communication.MessageCreator;
import communication.MessageType;
import game.board.Board;
import game.player.Robot;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;

/**
 *
 * Client Class
 * Communicating with Server and chat to send and receive messages from server and puts messages on chat.
 * Therefor putting incoming messages to LinkedBlockingQueue and reading from it.
 *
 * @author Antoine, Dominik, Tobias
 * @version 0.1
 *
 */

public class Client {

    private static Client client = null;
    MessageCreator messageCreator;
    private Socket socket = null;
    String address = "localhost";
    int port = 3000;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private String protocolVersion = "Version 1.0";
    private String group = "KnorrigeKorrelate";
    private ClientPlayerList clientPlayerList;
    private final Logger logger = LogManager.getLogger(Client.class);
    private Board board = new Board();
    private IntegerProperty userID;
    private BooleanProperty connected;
    private BooleanProperty accepted;
    private BooleanProperty isAI;
    private StringProperty errorMessage;
    private ObjectProperty<Message> message;
    private ObjectProperty<Message> movement;
    private StringProperty robotRotation;
    private ObjectProperty<Message> gameLogMessage;
    private ObjectProperty<Message> gameEventMessage;
    private BooleanProperty gameStarted;
    private StringProperty currentPlayer;
    private BooleanProperty activePlayer;
    private SimpleStringProperty activePhase;
    private boolean prioPlayer = false;
    private IntegerProperty score;
    private ObservableList<String> myCards;
    private IntegerProperty energy;
    private ObservableList<String> maps;
    private BooleanProperty timer;


    private Client() {

        this.messageCreator = new MessageCreator();
        this.clientPlayerList = new ClientPlayerList();
        this.userID = new SimpleIntegerProperty();
        this.connected = new SimpleBooleanProperty();
        this.accepted = new SimpleBooleanProperty(false);
        this.isAI = new SimpleBooleanProperty();
        this.message = new SimpleObjectProperty<>();
        this.maps = FXCollections.observableArrayList();
        this.errorMessage = new SimpleStringProperty("");
        this.gameStarted = new SimpleBooleanProperty();
        this.currentPlayer = new SimpleStringProperty("");
        this.activePhase = new SimpleStringProperty();
        this.activePlayer = new SimpleBooleanProperty(false);
        this.score = new SimpleIntegerProperty(0);
        this.myCards = FXCollections.observableArrayList();
        this.energy = new SimpleIntegerProperty(5);
        this.movement = new SimpleObjectProperty<>();
        this.robotRotation = new SimpleStringProperty("");
        this.gameLogMessage = new SimpleObjectProperty<>();
        this.gameEventMessage = new SimpleObjectProperty<>();
        this.timer = new SimpleBooleanProperty(false);
    }

    public static Client getInstance() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    public IntegerProperty userIDProperty() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID.set(userID);
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public BooleanProperty isAIProperty() {
        return isAI;
    }

    public BooleanProperty connectedProperty() {
        return connected;
    }

    public ClientPlayerList getPlayerList() {
        return clientPlayerList;
    }

    public ObservableList<String> getMaps() {
        return maps;
    }

    public Board getBoard() {
        return board;
    }

    public ObjectProperty<Message> messageProperty() {
        return message;
    }

    public void setMessage(Message message) {
        this.message.set(message);
    }

    public Message getGameLogMessage() {
        return gameLogMessage.get();
    }

    public ObjectProperty<Message> gameLogMessageProperty() {
        return gameLogMessage;
    }

    public void setGameLogMessage(Message gameLogMessage) {
        this.gameLogMessage.set(gameLogMessage);
    }

    public BooleanProperty acceptedProperty() {
        return accepted;
    }

    public void setAcceptedProperty(boolean accepted) {
        this.accepted.set(accepted);
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage.set(errorMessage);
    }

    public BooleanProperty gameStartedProperty() {
        return gameStarted;
    }

    public SimpleStringProperty activePhaseProperty() {
        return activePhase;
    }

    public void setActivePhase(String activePhase) {
        this.activePhase.set(activePhase);
    }

    public StringProperty currentPlayerProperty() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer.set(currentPlayer);
    }

    public BooleanProperty activePlayerProperty() {
        return activePlayer;
    }

    public void setActivePlayer(boolean activePlayer) {
        this.activePlayer.set(activePlayer);
    }

    public ObservableList<String> getMyCards() {
        return myCards;
    }

    public void setMyCards(ObservableList<String> myCards) {
        this.myCards = myCards;
    }

    public IntegerProperty energyProperty() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy.set(energy);
    }

    public Message getMovement() {
        return movement.get();
    }

    public ObjectProperty<Message> movementProperty() {
        return movement;
    }

    public void setMovement(Message movement) {
        this.movement.set(movement);
    }

    public StringProperty robotDirectionProperty() {
        return robotRotation;
    }

    public void setRobotDirection(String robotDirection) {
        this.robotRotation.set(robotDirection);
    }

    public Message getGameEventMessage() {
        return gameEventMessage.get();
    }

    public ObjectProperty<Message> gameEventMessageProperty() {
        return gameEventMessage;
    }

    public void setGameEventMessage(Message gameEventMessage) {
        this.gameEventMessage.set(gameEventMessage);
    }

    public boolean isTimer() {
        return timer.get();
    }

    public BooleanProperty timerProperty() {
        return timer;
    }

    public void setTimer(boolean timer) {
        this.timer.set(timer);
    }


    private class ReadMessagesFromServer implements Runnable {
        DataInputStream in = null;
        DataOutputStream out = null;
        Socket socket;
        ReadMessagesFromServer(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                out = new DataOutputStream(socket.getOutputStream());
                String receiveMessage;
                while (true) {
                    if((receiveMessage = in.readUTF()) != null) {
                        Message message = JsonSerializer.deserializeJson(receiveMessage, Message.class);
                        if (message.getMessageType().equals(MessageType.Alive)) {
                            sendAliveMessage();
                        }
                        if (message.getMessageType().equals(MessageType.HelloClient)) {
                            sendHelloServerMessage(group, isAI.get(), protocolVersion);
                        }
                        if (message.getMessageType().equals(MessageType.Welcome)) {
                            Client.this.setUserID(message.getMessageBody().getClientID());
                        }
                        if (message.getMessageType().equals(MessageType.PlayerAdded)) {
                            int clientID = message.getMessageBody().getClientID();
                            if (Client.this.userIDProperty().get() == clientID) {
                                if(!Client.this.accepted.get()) {
                                    Client.this.setAcceptedProperty(true);
                                }
                            }
                            else {
                                String username = message.getMessageBody().getName();
                                int figure = message.getMessageBody().getFigure();
                                if (!clientPlayerList.containsPlayer(clientID)) {
                                    Platform.runLater(() -> Client.this.getPlayerList().add(new ClientPlayer(clientID, username, new Robot(figure, clientID))));
                                }
                            }
                        }
                        if (message.getMessageType().equals(MessageType.PlayerStatus)) {
                            int clientID = message.getMessageBody().getClientID();
                            boolean ready = message.getMessageBody().isReady();
                            clientPlayerList.changePlayerStatus(clientID, ready);
                        }
                        if (message.getMessageType().equals(MessageType.ConnectionUpdate)) {
                            int clientID = message.getMessageBody().getClientID();
                            String action = message.getMessageBody().getAction();
                            if (action.equals("Remove")) {
                                Platform.runLater(() -> Client.this.clientPlayerList.remove(clientID));
                            }
                        }
                        if (message.getMessageType().equals(MessageType.SelectMap)) {
                            prioPlayer = true;
                            String[] temp = message.getMessageBody().getAvailableMaps();
                            for (int i = 0; i < temp.length; i++) {
                                maps.add(temp[i]);
                            }
                        }
                        if (message.getMessageType().equals(MessageType.MapSelected)) {
                            String map = message.getMessageBody().getMap();
                            Message mapMessage = messageCreator.generateSendChatMessage("Selected map: " + map);
                            Client.this.setMessage(mapMessage);
                        }
                        if (message.getMessageType().equals(MessageType.ReceivedChat)) {
                            Client.this.setMessage(message);
                        }
                        if (message.getMessageType().equals(MessageType.Error)) {
                            String error = message.getMessageBody().getError();
                            logger.debug("Error: " + error);
                            Platform.runLater(() -> Client.this.setErrorMessage(error));
                        }
                        if (message.getMessageType().equals(MessageType.GameStarted)) {
                            board.createBoard(message.getMessageBody().getGameMap());
                            gameStarted.set(true);
                        }
                        if (message.getMessageType().equals(MessageType.CurrentPlayer)) {
                            logger.debug("current player: " + message.getMessageBody().getClientID());
                            int clientID = message.getMessageBody().getClientID();
                            if (Client.this.userIDProperty().get() == clientID) {
                                Client.this.setActivePlayer(true);
                                Client.this.setCurrentPlayer("It's your turn");
                                for (int i = 0; i < clientPlayerList.getPlayerList().size(); i++) {
                                    clientPlayerList.getPlayerList().get(i).setActivePlayer("");
                                }
                            }
                            else {
                                Client.this.setActivePlayer(false);
                                Client.this.setCurrentPlayer("");
                                if (clientPlayerList.containsPlayer(clientID)) {
                                    clientPlayerList.getPlayer(clientID).setActivePlayer("It's their turn");
                                }
                            }
                        }
                        if (message.getMessageType().equals(MessageType.ActivePhase)) {
                            logger.debug("active Phase: " + message.getMessageBody().getPhase());
                            int activePhase = message.getMessageBody().getPhase();
                            if (activePhase == 0) {
                                Platform.runLater(() -> Client.this.setActivePhase("Construction Phase"));
                            }
                            if (activePhase == 1) {
                                Platform.runLater(() -> Client.this.setActivePhase("Upgrade Phase"));
                            }
                            if (activePhase == 2) {
                                Platform.runLater(() -> Client.this.setActivePhase("Programming Phase"));
                            }
                            if (activePhase == 3) {
                                Platform.runLater(() -> Client.this.setActivePhase("Activation Phase"));
                            }
                        }
                        if (message.getMessageType().equals(MessageType.YourCards)) {
                            String[] cardsInHand = message.getMessageBody().getCardsInHand();
                            Platform.runLater(() -> {
                                Client.this.myCards.clear();
                                Client.this.myCards.addAll(cardsInHand);
                                for(String card: cardsInHand){
                                    logger.debug("Client - cardsInHand: " + card);
                                }
                            });
                            for (int i = 0; i < myCards.size(); i++) {
                                logger.debug("Client - myCards: " + myCards.get(i));
                            }
                        }
                        if (message.getMessageType().equals(MessageType.NotYourCards)) {
                            logger.debug("NotYourCards: -Amount of cards: " + message.getMessageBody().getCardsAmountInHand());
                            int clientID = message.getMessageBody().getClientID();
                            int cardsInHand = message.getMessageBody().getCardsAmountInHand();
                            if (clientPlayerList.containsPlayer(clientID)) {
                                Platform.runLater(() -> Client.this.clientPlayerList.getPlayer(clientID).setCardsInHand(cardsInHand));
                            }
                        }
                        /*
                        if (message.getMessageType().equals(MessageType.CardPlayed)) {
                            Client.this.setGameLogMessage(message);
                        }
                         */
                        if (message.getMessageType().equals(MessageType.CardSelected)) {
                            logger.debug("card selected: " + message.getMessageBody().getCard());
                            int clientID = message.getMessageBody().getClientID();
                            if (userIDProperty().get() != clientID) {
                                Client.this.setGameLogMessage(message);
                            }
                        }
                        if (message.getMessageType().equals(MessageType.SelectionFinished)) {
                            Client.this.setGameEventMessage(message);
                        }
                        if (message.getMessageType().equals(MessageType.CurrentCards)) {
                            Client.this.setGameEventMessage(message);
                        }
                        if (message.getMessageType().equals(MessageType.ReplaceCard)) {
                            Client.this.setGameEventMessage(message);
                        }
                        if (message.getMessageType().equals(MessageType.CardsYouGotNow)) {
                            logger.debug("CardsYouGotNow: " + message.getMessageBody().getCards().toString() + message.getMessageBody().getCards().length);
                            Platform.runLater(() -> Client.this.setGameEventMessage(message));
                        }
                        if (message.getMessageType().equals(MessageType.RegisterChosen)) {
                            Client.this.setGameLogMessage(message);
                        }
                        if (message.getMessageType().equals(MessageType.StartingPointTaken)) {
                            Client.this.setMovement(message);
                            logger.debug("StartingPointTaken message: " + message);
                        }
                        if (message.getMessageType().equals(MessageType.Movement)) {
                            Client.this.setMovement(message);
                            logger.debug("Movement message: " + message);
                        }
                        if (message.getMessageType().equals(MessageType.CheckpointMoved)) {
                            Platform.runLater(() -> Client.this.setGameEventMessage(message));
                        }
                        if (message.getMessageType().equals(MessageType.PlayerTurning)) {
                            /*
                            logger.debug("roboter turning");
                            Client.this.setRobotDirection(message.getMessageBody().getRotation());
                             */
                            logger.debug("roboter turning");
                            Client.this.setGameEventMessage(message);
                        }
                        if (message.getMessageType().equals(MessageType.TimerStarted)) {
                            logger.debug("timer started");
                            Client.this.setTimer(true);
                        }
                        if (message.getMessageType().equals(MessageType.TimerEnded)) {
                            logger.debug("timer ended");
                            Client.this.setTimer(false);
                            Client.this.setGameLogMessage(message);
                        }
                        if (message.getMessageType().equals(MessageType.Energy)) {
                            logger.debug("Energy gained");
                            int clientID = message.getMessageBody().getClientID();
                            int count = message.getMessageBody().getCount();
                            if (userIDProperty().get() == clientID) {
                                Platform.runLater(() -> Client.this.addEnergy(count));
                            }
                            else {
                                for (int i = 0; i < clientPlayerList.getPlayerList().size(); i++) {
                                    if (clientPlayerList.getPlayer(clientID).getId() == clientID) {
                                        Platform.runLater(() -> clientPlayerList.getPlayer(clientID).addEnergy(count));
                                    }
                                }
                            }
                        }
                        if (message.getMessageType().equals(MessageType.DrawDamage)) {
                            Client.this.setGameEventMessage(message);
                        }
                        if (message.getMessageType().equals(MessageType.PickDamage)) {
                            Client.this.setGameEventMessage(message);
                        }
                        if (message.getMessageType().equals(MessageType.CheckPointReached)) {
                            int clientID = message.getMessageBody().getClientID();
                            if (Client.this.userIDProperty().get() == clientID) {
                                Platform.runLater(() -> Client.this.setScore(message.getMessageBody().getNumber()));
                            }
                            else {
                                Platform.runLater(() -> clientPlayerList.getPlayer(clientID).setScore(message.getMessageBody().getNumber()));
                                Client.this.setGameLogMessage(message);
                            }
                        }
                        if (message.getMessageType().equals(MessageType.GameFinished)) {
                            Client.this.setGameLogMessage(message);
                        }
                        if (message.getMessageType().equals(MessageType.RefillShop)){
                            //TODO: display cards from the message
                        }
                        if (message.getMessageBody().equals(MessageType.ExchangeShop)){
                            //TODO: no idea why there needs to be a distinction between a refill and a complete redraw
                        }
                        if (message.getMessageType().equals(MessageType.UpgradeBought)){
                            //TODO: receive purchase confirmation
                        }
                        if (message.getMessageType().equals(MessageType.Reboot)) {
                            Client.this.setGameEventMessage(message);
                        }
                    }
                }
            } catch (IOException e) {
                logger.warn("Lost connection to server " + e);
            }
        }
    }

    private void addEnergy(int count) {
        this.energy.add(count);
    }

    //maybe are these methods redundant, but they are kept until everything is implemented for them to be there
    public void sendAliveMessage(){
        sendMessageToServer(messageCreator.generateAliveMessage());
    }
    public void sendHelloServerMessage(String group, boolean isAI, String protocolVersion){
        sendMessageToServer(messageCreator.generateHelloServerMessage(group, isAI, protocolVersion));
    }
    public void sendPlayerValues(String name, int figure){
        sendMessageToServer(messageCreator.generatePlayerValuesMessage(name, figure));
    }
    public void sendSetStatusMessage(boolean ready){
        prioPlayer = false;
        this.maps.clear();
        sendMessageToServer(messageCreator.generateSetStatusMessage(ready));
    }
    public void sendPrivateMessage(String message, int to){
        sendMessageToServer(messageCreator.generateSendChatMessage(message, to));
    }
    public void sendGroupMessage(String message){
        sendMessageToServer(messageCreator.generateSendChatMessage(message));
    }
    public void sendMapSelected(String map) {
        if (prioPlayer) {
            sendMessageToServer(messageCreator.generateMapSelectedMessage(map));
        }
    }
    public void sendPlayCard(String card) {
        sendMessageToServer(messageCreator.generatePlayCardMessage(card));
    }
    public void sendStartingPoint(int x, int y) {
        sendMessageToServer(messageCreator.generateSetStartingPointMessage(x, y));
        this.activePlayer.set(false);
    }
    public void sendSelectedCard(String card, int register) {
        logger.debug("Selected card: " + card + " and register: " + register);
        sendMessageToServer(messageCreator.generateSelectedCardMessage(card, register));
    }
    public void sendSelectedDamageCards(String card, int register) {
        sendMessageToServer(messageCreator.generateSelectedCardMessage(card, register));
    }
    public void sendRebootDirection(String direction) {
        sendMessageToServer(messageCreator.generateRebootDirectionMessage(direction));
    }
    public void sendDiscardSome(String[] discardSome) {
        //TODO: Add discardSome to message
        //sendMessageToServer(messageCreator.generateDiscardSome)
    }
    public void sendChooseRegister(int register) {
        sendMessageToServer(messageCreator.generateChooseRegisterMessage(register));
    }

    public void sendReturnCards(String[] returnCards) {
        //sendMessageToServer(messageCreator.generateReturnCardsMessage(returnCards));
    }
    //TODO: Please call this method in the correct place. Player has both variables isBuying (isBuying lol) and card (upgradeToBuy) accessible with setters and getters
    public void sendBuyUpgrade(Boolean isBuying, String card){
        sendMessageToServer(messageCreator.generateBuyUpgradeMessage(isBuying, card));
    }
    public void sendMessageToServer(Message message) {
        try {
            out.writeUTF(JsonSerializer.serializeJson(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connectServer() {
        if (!connected.get()) {
            try {
                socket = new Socket(address, port);
                in = new DataInputStream(
                        new BufferedInputStream(socket.getInputStream()));
                out = new DataOutputStream(socket.getOutputStream());
                connected.set(true);
                ReadMessagesFromServer server = new ReadMessagesFromServer(socket);
                new Thread(server).start();
            } catch (Exception e) {
                socket = null;
                in = null;
                out = null;
            }
        }
    }
    public void reconnect() {
        if (!connected.get()) {
            connectServer();
        }
    }
}



