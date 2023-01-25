package client.connection;

import client.player.ClientPlayer;
import client.player.ClientPlayerList;
import client.player.RegisterInformation;
import communication.JsonSerializer;
import communication.Message;
import communication.MessageCreator;
import communication.MessageType;
import game.board.Board;
import game.card.Card;
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
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private BooleanProperty connected;
    private BooleanProperty accepted;
    private BooleanProperty isAI;
    private BooleanProperty gameStarted;
    private ObjectProperty<Message> message;
    private ObjectProperty<Message> movement;
    private ObjectProperty<Message> gameLogMessage;
    private IntegerProperty userID;
    private StringProperty errorMessage;
    private IntegerProperty x;
    private IntegerProperty y;
    private IntegerProperty movementX;
    private IntegerProperty movementY;
    private IntegerProperty robotID;
    private boolean prioPlayer = false;
    private BooleanProperty activePlayer;

    public static ArrayList<ArrayList<Pair<Integer, Integer>>> robotLaserList = new ArrayList<>();
    public ObservableList<String> myCards;
    MessageCreator messageCreator;
    String address = "localhost";
    int port = 3000;
    private String protocolVersion = "Version 1.0";
    private String group = "KnorrigeKorrelate";
    private ClientPlayerList clientPlayerList;
    private ObservableList<String> maps;
    private BooleanProperty timer;
    private final Logger logger = LogManager.getLogger(Client.class);

    public Board getBoard() {
        return board;
    }

    private Board board = new Board();


    private Client() {

        this.messageCreator = new MessageCreator();
        this.message = new SimpleObjectProperty<>();
        this.userID = new SimpleIntegerProperty();
        this.gameStarted = new SimpleBooleanProperty();
        this.activePlayer = new SimpleBooleanProperty(false);
        this.isAI = new SimpleBooleanProperty();
        this.maps = FXCollections.observableArrayList();
        this.connected = new SimpleBooleanProperty();
        this.accepted = new SimpleBooleanProperty(false);
        this.clientPlayerList = new ClientPlayerList();
        this.errorMessage = new SimpleStringProperty();
        this.myCards = FXCollections.observableArrayList();
        this.x = new SimpleIntegerProperty();
        this.y = new SimpleIntegerProperty();
        this.timer = new SimpleBooleanProperty(false);
        this.movementX = new SimpleIntegerProperty();
        this.movementY = new SimpleIntegerProperty();
        this.robotID = new SimpleIntegerProperty();
        this.movement = new SimpleObjectProperty<>();
    }

    public static Client getInstance() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    public ObjectProperty<Message> messageProperty() {
        return message;
    }

    public void setMessage(Message message) {
        this.message.set(message);
    }

    public IntegerProperty userIDProperty() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID.set(userID);
    }

    public BooleanProperty isAIProperty() {
        return isAI;
    }

    public BooleanProperty connectedProperty() {
        return connected;
    }
    public ObservableList<String> getMaps() {
        return maps;
    }

    public BooleanProperty acceptedProperty() {
        return accepted;
    }

    public void setAcceptedProperty(boolean accepted) {
        this.accepted.set(accepted);
    }

    public ClientPlayerList getPlayerList() {
        return clientPlayerList;
    }

    public String getErrorMessage() {
        return errorMessage.get();
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage.set(errorMessage);
    }
    public boolean isGameStarted() {
        return gameStarted.get();
    }

    public BooleanProperty gameStartedProperty() {
        return gameStarted;
    }

    public ObservableList<String> getMyCards() {
        return myCards;
    }

    public void setMyCards(ObservableList<String> myCards) {
        this.myCards = myCards;
    }

    public int getX() {
        return x.get();
    }

    public IntegerProperty xProperty() {
        return x;
    }

    public void setX(int x) {
        this.x.set(x);
    }

    public int getY() {
        return y.get();
    }

    public IntegerProperty yProperty() {
        return y;
    }

    public void setY(int y) {
        this.y.set(y);
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

    public int getMovementX() {
        return movementX.get();
    }

    public IntegerProperty movementXProperty() {
        return movementX;
    }

    public void setMovementX(int movementX) {
        this.movementX.set(movementX);
    }

    public int getMovementY() {
        return movementY.get();
    }

    public IntegerProperty movementYProperty() {
        return movementY;
    }

    public void setMovementY(int movementY) {
        this.movementY.set(movementY);
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

    public int getRobotID() {
        return robotID.get();
    }

    public IntegerProperty robotIDProperty() {
        return robotID;
    }

    public void setRobotID(int robotID) {
        this.robotID.set(robotID);
    }

    public Boolean getPrioPlayer() {
        return prioPlayer;
    }

    public void setPrioPlayer(Boolean prioPlayer) {
        this.prioPlayer = prioPlayer;
    }

    public boolean isActivePlayer() {
        return activePlayer.get();
    }

    public BooleanProperty activePlayerProperty() {
        return activePlayer;
    }

    public void setActivePlayer(boolean activePlayer) {
        this.activePlayer.set(activePlayer);
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
                        if (message.getMessageType().equals(MessageType.SelectMap)) {
                            prioPlayer = true;
                            //TODO: if client looses prio because he removes ready or looses connection, he isn`t allowed to change map
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
                        if (message.getMessageType().equals(MessageType.CardPlayed)) {
                            Client.this.setMessage(message);
                        }
                        if (message.getMessageType().equals(MessageType.CurrentPlayer)) {
                            Client.this.activePlayer.set(true);
                        }
                        if (message.getMessageType().equals(MessageType.StartingPointTaken)) {
                            Client.this.setMovement(message);
                            logger.debug("StartingPointTaken message: " + message);
                            /*
                            int clientRobot = message.getMessageBody().getClientID();
                            if (Client.this.userIDProperty().get() == clientRobot) {
                                 Client.this.setX(message.getMessageBody().getX());
                                 Client.this.setY(message.getMessageBody().getY());
                                 System.out.println("X= " + message.getMessageBody().getX() + " | Y= " + message.getMessageBody().getY());
                                 System.out.println("My Robot");
                            }
                            else {
                                int robotIDclient = clientPlayerList.getPlayer(clientRobot).getRobot().getFigure();
                                Client.this.setMovementX(message.getMessageBody().getX());
                                Client.this.setMovementY(message.getMessageBody().getY());
                                Client.this.setRobotID(robotIDclient);
                                System.out.println("X = " + message.getMessageBody().getX() + " | Y = " + message.getMessageBody().getY());
                                System.out.println("RobotID in Client: : " + robotIDclient);
                            }
                             */
                        }
                        if (message.getMessageType().equals(MessageType.GameStarted)) {
                            board.createBoard(message.getMessageBody().getGameMap());
                            //TODO: connect to Model/ViewModel to switch scenes
                            System.out.println("game started");
                            gameStarted.set(true);
                        }
                        if (message.getMessageType().equals(MessageType.YourCards)) {
                            String[] cardsInHand = message.getMessageBody().getCardsInHand();
                            Platform.runLater(() -> {
                                Client.this.myCards.clear();
                                Client.this.myCards.addAll(cardsInHand);
                                for(String card: cardsInHand){
                                    logger.debug("Client - cardsInHand" + card);
                                }
                            });
                            for (int i = 0; i < myCards.size(); i++) {
                                logger.debug("Client - myCards: " + myCards.get(i));
                            }
                        }
                        if (message.getMessageType().equals(MessageType.NotYourCards)) {
                            int clientID = message.getMessageBody().getClientID();
                            int cardsInHand = message.getMessageBody().getCardsAmountInHand();
                            for (int i = 0; i < clientPlayerList.getPlayerList().size(); i++) {
                                if (clientPlayerList.getPlayerList().get(i).getId() == clientID) {
                                    Client.this.clientPlayerList.getPlayerList().get(i).setCardsInHand(cardsInHand);
                                }
                            }
                        }
                        if (message.getMessageType().equals(MessageType.ConnectionUpdate)) {
                            int clientID = message.getMessageBody().getClientID();
                            String action = message.getMessageBody().getAction();
                            if (action.equals("Remove")) {
                                Platform.runLater(() -> Client.this.clientPlayerList.remove(clientID));
                            }
                        }
                        if (message.getMessageType().equals(MessageType.Movement)) {
                            Client.this.setMovement(message);
                            logger.debug("Movement message: " + message);
                            /*
                            int clientRobot = message.getMessageBody().getClientID();
                            if (Client.this.userIDProperty().get() == clientRobot) {
                                Client.this.setX(message.getMessageBody().getX());
                                Client.this.setY(message.getMessageBody().getY());
                                System.out.println("X= " + message.getMessageBody().getX() + " | Y= " + message.getMessageBody().getY());
                                System.out.println("My Robot");
                            }
                            else {
                                int robotIDclient = clientPlayerList.getPlayer(clientRobot).getRobot().getFigure();
                                Client.this.setMovementX(message.getMessageBody().getX());
                                Client.this.setMovementY(message.getMessageBody().getY());
                                Client.this.setRobotID(robotIDclient);
                                System.out.println("X = " + message.getMessageBody().getX() + " | Y = " + message.getMessageBody().getY());
                                System.out.println("RobotID in Client: : " + robotIDclient);
                            }
                             */
                        }
                        if (message.getMessageType().equals(MessageType.CardSelected)) {
                            int clientID = message.getMessageBody().getClientID();
                            int register = message.getMessageBody().getRegister();
                            boolean filled = message.getMessageBody().isFilled();
                            for (int i = 0; i < clientPlayerList.getPlayerList().size(); i++) {
                                if (clientPlayerList.getPlayerList().get(i).getId() == clientID) {
                                    Client.this.clientPlayerList.getPlayerList().get(i).getRegisterInformations().add(new RegisterInformation(register, filled));
                                }
                            }
                        }
                        if (message.getMessageType().equals(MessageType.SelectionFinished)) {

                        }
                        if (message.getMessageType().equals(MessageType.TimerStarted)) {
                            Client.this.setTimer(true);
                        }
                        if (message.getMessageType().equals(MessageType.TimerEnded)) {
                            Client.this.setGameLogMessage(message);
                        }
                        if (message.getMessageType().equals(MessageType.ConnectionUpdate)) {

                        }
                        if (message.getMessageType().equals(MessageType.PlayerTurning)) {

                        }
                        if (message.getMessageType().equals(MessageType.DrawDamage)) {

                        }
                        if (message.getMessageType().equals(MessageType.PickDamage)) {

                        }
                        if (message.getMessageType().equals(MessageType.Energy)) {

                        }
                        if (message.getMessageType().equals(MessageType.GameFinished)) {

                        }
                        if (message.getMessageType().equals(MessageType.CheckpointMoved)) {

                        }
                        if (message.getMessageType().equals(MessageType.RegisterChosen)) {

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
    public void sendPlayerValuesMessage(String name, int figure){
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
    public void sendPlayCard(Card card) {
        String sendCard = card.getCard();
        sendMessageToServer(messageCreator.generatePlayCardMessage(sendCard));
    }
    public void sendStartingPoint(int x, int y) {
        sendMessageToServer(messageCreator.generateSetStartingPointMessage(x, y));
        this.activePlayer.set(false);
    }
    public void sendSelectCard(String card, int register) {
        logger.debug("Selected card: " + card + " and register: " + register);
        sendMessageToServer(messageCreator.generateSelectedCardMessage(card, register));
    }
    //TODO: add method in messageCreator
    public void sendSelectedDamageCards() {

    }
    public void sendRebootDirection(String direction) {
        sendMessageToServer(messageCreator.generateRebootDirectionMessage(direction));
    }
    public void sendDiscardSome() {

    }
    public void sendRegister(int register) {
        sendMessageToServer(messageCreator.generateChooseRegisterMessage(register));
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



