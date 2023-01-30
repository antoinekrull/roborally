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
import java.util.Arrays;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * Client Class
 * Communicating with Server and chat to send and receive messages from server and puts messages on chat.
 * Therefor putting incoming messages to LinkedBlockingQueue and reading from it.
 *
 * @author Antoine, Dominik, Tobias
 * @version 2.0
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
    private ObjectProperty<Message> gameLogMessage;
    private ObjectProperty<Message> gameEventMessage;
    private BooleanProperty gameStarted;
    private BooleanProperty currentPlayer;
    private SimpleStringProperty activePhase;
    private boolean prioPlayer = false;
    private IntegerProperty score;
    private ObservableList<String> myCards;
    private DoubleProperty energy;
    private ObservableList<String> maps;
    private BooleanProperty timer;
    private ObservableList<String> upgradeShop;
    private BooleanProperty startShop;


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
        this.currentPlayer = new SimpleBooleanProperty(false);
        this.activePhase = new SimpleStringProperty("Construction Phase");
        this.score = new SimpleIntegerProperty(0);
        this.myCards = FXCollections.observableArrayList();
        this.energy = new SimpleDoubleProperty(5);
        this.movement = new SimpleObjectProperty<>();
        this.gameLogMessage = new SimpleObjectProperty<>();
        this.gameEventMessage = new SimpleObjectProperty<>();
        this.timer = new SimpleBooleanProperty(false);
        this.upgradeShop = FXCollections.observableArrayList();
        this.startShop = new SimpleBooleanProperty(false);
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

    public BooleanProperty currentPlayerProperty() {
        return currentPlayer;
    }

    public void setCurrentPlayer(boolean currentPlayer) {
        this.currentPlayer.set(currentPlayer);
    }

    public ObservableList<String> getMyCards() {
        return myCards;
    }

    public void setMyCards(ObservableList<String> myCards) {
        this.myCards = myCards;
    }

    public DoubleProperty energyProperty() {
        return energy;
    }

    public void setEnergy(double energy) {
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

    public ObservableList<String> getUpgradeShop() {
        return upgradeShop;
    }

    public void setUpgradeShop(ObservableList<String> upgradShop) {
        this.upgradeShop = upgradShop;
    }

    public BooleanProperty startShopProperty() {
        return startShop;
    }

    public void setStartShop(boolean startShop) {
        this.startShop.set(startShop);
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
                            if (map.equals("DeathTrap")) {
                                Client.this.setGameEventMessage(message);
                            }
                            Message mapMessage = messageCreator.generateMapSelectedMessage(map);
                            Client.this.setGameLogMessage(mapMessage);
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
                                Platform.runLater(() -> {
                                    Client.this.setCurrentPlayer(true);
                                    for (int i = 0; i < clientPlayerList.getPlayerList().size(); i++) {
                                    clientPlayerList.getPlayerList().get(i).setActivePlayer("It is not their turn");
                                }});
                            }
                            else {
                                Client.this.setCurrentPlayer(false);
                                if (clientPlayerList.containsPlayer(clientID)) {
                                    Platform.runLater(() -> clientPlayerList.getPlayer(clientID).setActivePlayer("It is their turn"));
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
                                //Client.this.startShop.set(false);
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
                            logger.debug("CardsYouGotNow: " + Arrays.toString(message.getMessageBody().getCards()) + message.getMessageBody().getCards().length);
                            Platform.runLater(() -> Client.this.setGameEventMessage(message));
                        }
                        if (message.getMessageType().equals(MessageType.RegisterChosen)) {
                            Client.this.setGameLogMessage(message);
                        }
                        if (message.getMessageType().equals(MessageType.StartingPointTaken)) {
                            Client.this.setMovement(message);
                        }
                        if (message.getMessageType().equals(MessageType.Movement)) {
                            Client.this.setMovement(message);
                        }
                        if (message.getMessageType().equals(MessageType.CheckpointMoved)) {
                            logger.debug("checkpointMoved: checkpoint: " + message.getMessageBody().getCheckpointID() + " " +  message.getMessageBody().getX() + " " +message.getMessageBody().getY());
                            Platform.runLater(() -> Client.this.setGameEventMessage(message));
                        }
                        if (message.getMessageType().equals(MessageType.PlayerTurning)) {
                            Client.this.setGameEventMessage(message);
                        }
                        if (message.getMessageType().equals(MessageType.TimerStarted)) {
                            Client.this.setTimer(true);
                        }
                        if (message.getMessageType().equals(MessageType.TimerEnded)) {
                            Client.this.setTimer(false);
                            Client.this.setGameLogMessage(message);
                        }
                        if (message.getMessageType().equals(MessageType.Energy)) {
                            int clientID = message.getMessageBody().getClientID();
                            int count = message.getMessageBody().getCount();
                            if (userIDProperty().get() == clientID) {
                                Platform.runLater(() -> Client.this.setEnergy(energyProperty().get() + count));
                            }
                            else {
                                for (int i = 0; i < clientPlayerList.getPlayerList().size(); i++) {
                                    if (clientPlayerList.getPlayer(clientID).getId() == clientID) {
                                        Platform.runLater(() -> clientPlayerList.getPlayer(clientID).setEnergyCubes(count));
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
                            logger.debug("refill shop");
                            Client.this.setGameLogMessage(message);
                            String[] refillCards = message.getMessageBody().getCards();
                            Platform.runLater(() -> {
                                Client.this.upgradeShop.addAll(refillCards);
                                for(String card: refillCards){
                                    logger.debug("Client - refillCards: " + card);
                                }
                            });
                            for (int i = 0; i < myCards.size(); i++) {
                                logger.debug("Client - refillCards: " + myCards.get(i));
                            }
                            Client.this.setGameEventMessage(message);
                        }
                        if (message.getMessageType().equals(MessageType.ExchangeShop)){
                            logger.debug("exchange shop started");
                            Client.this.setGameLogMessage(message);
                            String[] exchangeCards = message.getMessageBody().getCards();
                            Platform.runLater(() -> {
                                Client.this.upgradeShop.clear();
                                Client.this.upgradeShop.addAll(exchangeCards);
                                for(String card: exchangeCards){
                                    logger.debug("Client - exchangeCards: " + card);
                                }
                            });
                            for (int i = 0; i < myCards.size(); i++) {
                                logger.debug("Client - myCards: " + myCards.get(i));
                            }
                            Client.this.setGameEventMessage(message);
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

    //maybe are these methods redundant, but they are kept until everything is implemented for them to be there
    public void sendAliveMessage(){
        sendMessageToServer(messageCreator.generateAliveMessage());
    }
    public void sendHelloServerMessage(String group, boolean isAI, String protocolVersion){
        sendMessageToServer(messageCreator.generateHelloServerMessage(group, isAI, protocolVersion));
    }
    public void sendAIMessage(boolean isAI) {
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



