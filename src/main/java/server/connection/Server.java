package server.connection;

import communication.Message;
import communication.MessageCreator;
import communication.MessageType;
import game.Game;
import game.board.Direction;
import game.board.EnergySpaceTile;
import game.card.Card;
import game.card.PowerUpCard;
import game.player.Player;
import game.player.Robot;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Server
 * Changed to Singleton to connect it with a gui
 */
public class Server {

    private static Server server;

    protected Socket socket;
    protected ServerSocket serverSocket;
    private Game game;
    public PlayerList players;
    public HashMap<Integer, HandleClient> CLIENTS = new HashMap<>();
    //Message type instead of Strings
    public LinkedBlockingQueue<Message> messages;
    public int uniqueID = 1;
    public Boolean alive;
    public BooleanProperty online;
    MessageCreator messageCreator;
    private final String protocolVersion;
    private final Logger logger = LogManager.getLogger(Server.class);

    private Server() {
        logger.debug("Server constructor is called");
        this.messageCreator = new MessageCreator();
        this.players = new PlayerList();
        this.protocolVersion = "Version 1.0";
        this.online = new SimpleBooleanProperty(true);
        this.messages = new LinkedBlockingQueue<>();
    }

    public static Server getInstance() {
        if (server == null) {
            server = new Server();
        }
        return server;
    }
    public Boolean isAlive() {
        return this.alive;
    }
    public BooleanProperty onlineProperty() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online.set(online);
    }

    public void startServer(int port) {
        this.game = Game.getInstance();
        this.alive = true;
        server.setOnline(true);
        Thread acceptClients = new Thread() {
            public void run() {
                try {
                    serverSocket = new ServerSocket(port);

                    while (true) {
                        socket = serverSocket.accept();

                        //handle multithreading for clients
                        int uniqueID = getUniqueID();
                        HandleClient client = new HandleClient(socket.getRemoteSocketAddress().toString(), socket.getPort(), socket, server , uniqueID);
                        client.setUsername("");
                        synchronized (CLIENTS) {
                            CLIENTS.put(uniqueID, client);
                        }

                        new Thread(client).start();
                    }

                } catch (Exception e) {
                    logger.warn("Error here " + e.getMessage());
                }
            }
        };
        acceptClients.start();

        //Changes
        Thread writeMessages = new Thread() {
            public void run() {
                while (true) {
                    try {
                        Message message = messages.take();
                        boolean isPrivate = message.getMessageBody().isPrivate();
                        //Added goodbye: added message type in HandleClient to Message instead of String because of protocol (Chat)
                        if (message.getMessageType() == MessageType.Goodbye) {
                            int id = message.getMessageBody().getClientID();
                            for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
                                if (client.getKey() != id) {
                                    client.getValue().write(message);
                                }
                            }
                        } else if(message.getMessageType() == MessageType.SelectMap){
                            for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
                                if (client.getKey() == game.getFirstReadyID()) {
                                    client.getValue().write(message);
                                }
                            }
                        } else if(message.getMessageType() == MessageType.GameStarted){
                            for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
                                    client.getValue().write(message);
                            }
                        } else if (message.getMessageType() == MessageType.CurrentPlayer){
                            int id = message.getMessageBody().getClientID();
                            for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
                                if (client.getKey() == id) {
                                    client.getValue().write(message);
                                }
                            }
                            //TODO: molri will fix
//                        } else if(message.getMessageType() == MessageType.YourCards) {
//                            int id = message.getMessageBody().getClientID();
//                            for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
//                                if (client.getKey() == id) {
//                                    client.getValue().write(message);
//                                }
//                            }
                        } else if(message.getMessageType() == MessageType.ShuffleCoding) {
                            int id = message.getMessageBody().getClientID();
                            for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
                                if (client.getKey() == id) {
                                    client.getValue().write(message);
                                }
                            }
                            //TODO: molri will fix
//                        } else if(message.getMessageType() == MessageType.CardsYouGotNow) {
//                            int id = message.getMessageBody().getClientID();
//                            for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
//                                if (client.getKey() == id) {
//                                    client.getValue().write(message);
//                                }
//                            }
                        } else if(message.getMessageType() == MessageType.DrawDamage) {
                            int id = message.getMessageBody().getClientID();
                            for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
                                if (client.getKey() == id) {
                                    client.getValue().write(message);
                                }
                            }
                            //TODO: molri will fix
//                        } else if(message.getMessageType() == MessageType.PickDamage) {
//                            int id = message.getMessageBody().getClientID();
//                            for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
//                                if (client.getKey() == id) {
//                                    client.getValue().write(message);
//                                }
//                            }
                        }
                        //Changed group messages: will only be displayed to other clients, not to yourself
                        //added private message to work in chat
                        //still need other players clientID to send message
                        else if (isPrivate) {
                            int toUser = message.getMessageBody().getFrom();
                            for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
                                if (client.getKey() == toUser) {
                                    client.getValue().write(message);
                                }
                            }
                        }
                        else {
                            int id = message.getMessageBody().getFrom();
                            for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
                                if (client.getKey() != id) {
                                    client.getValue().write(message);
                                }
                            }
                        }
                    } catch (Exception e) {
                            logger.warn("An Exception occured: " + e);
                    }
                }
            }
        };
        writeMessages.start();
    }
    public void addPlayerToGame(Player player) {
        game.addPlayer(player);
    }

    public void sendPlayerValuesToAll(int clientID, Message message) {
        for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
            if (client.getKey() != clientID) {
                client.getValue().write(message);
            }
        }
    }

    public void sendConnectionLost(int clientID, Message message) {
        for (Map.Entry<Integer, HandleClient> client : CLIENTS.entrySet()) {
            if (client.getKey() != clientID) {
                client.getValue().write(message);
            }
        }
    }

    public void sendGameFinished(Player player) {
        try{
            messages.put(messageCreator.generateGameFinishedMessage(player.getId()));
        } catch (InterruptedException e) {
            logger.warn("An error occurred: " + e);
        }
    }
    public void sendCheckpointReached(Pair<Integer, Integer> pair){
        try{
            messages.put(messageCreator.generateCheckPointReachedMessage(pair.getValue0(), pair.getValue1()));
        } catch (InterruptedException e) {
            logger.warn("An error occurred: " + e);
        }
    }

    public void sendGameStarted(String jsonMap) {
        try {
            messages.put(messageCreator.generateGameStartedMessage(jsonMap));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendSelectMap(String[] maps){
        try {
            messages.put(messageCreator.generateSelectMapMessage(maps));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    public void sendActivePhase(int phase) {
        try {
            messages.put(messageCreator.generateActivePhaseMessage(phase));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendYourCards(Player player) {
        String[] cardsInHand = new String[player.getHand().size()];
        for(int i = 0; i < player.getHand().size(); i++) {
            cardsInHand[i] = player.getHand().get(i).getCard();
        }
        try {
            CLIENTS.get(player.getId()).write(messageCreator.generateYourCardsMessage(cardsInHand));
            messages.put(messageCreator.generateNotYourCardsMessage(player.getId(), player.getHand().size()));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendSelectionFinished(int id) {
        try {
            messages.put(messageCreator.generateSelectionFinishedMessage(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendShuffleCoding(Player player) {
        try {
            messages.put(messageCreator.generateShuffleCodingMessage(player.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendCurrentPlayer(int ID) {
        try {
            messages.put(messageCreator.generateCurrentPlayerMessage(ID));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendTimerStarted() {
        try {
            messages.put(messageCreator.generateTimerStartedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTimerEnded(PlayerList latePlayers) {
        int[] latePlayerIds = new int[latePlayers.size()];
        for(int i = 0; i < latePlayers.size(); i++) {
            latePlayerIds[i] = latePlayers.get(i).getId();
        }
        try {
            messages.put(messageCreator.generateTimerEndedMessage(latePlayerIds));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCardsYouGotNow(Player player, String[] cardNames) {
        try {
            CLIENTS.get(player.getId()).write(messageCreator.generateCardsYouGotNowMessage(cardNames));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO: Fix this
    public void sendCurrentCards(ArrayList<Card> cardArrayList) {
        try {
            messages.put(messageCreator.generateCurrentCardsMessage(cardArrayList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendReplaceCard(Player player) {
        try {
            messages.put(messageCreator.generateReplaceCardMessage(game.getCurrentRegister(),
                    player.getCardFromRegister(game.getCurrentRegister()).getCard(), player.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMovement(Robot robot) {
        try {
            messages.put(messageCreator.generateMovementMessage(robot.getId(), robot.getCurrentPosition().getValue0(),
                    robot.getCurrentPosition().getValue1()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPlayerTurning(Robot robot, String direction) {
        try {
            messages.put(messageCreator.generatePlayerTurningMessage(robot.getId(), direction));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendDrawDamage(Player player, String[] damageCards) {
        try {
            messages.put(messageCreator.generateDrawDamageMessage(player.getId(), damageCards));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPickDamage(Player player, String[] availablePiles) {
        try {
            CLIENTS.get(player.getId()).write(messageCreator.generatePickDamage(availablePiles));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendReboot(Player player) {
        try {
            messages.put(messageCreator.generateRebootMessage(player.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendRebootDirection(Direction direction) {
        try {
            messages.put(messageCreator.generateRebootDirectionMessage(direction.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEnergy(int id, int count, String source) {
        try {
            String energySource = source;
            messages.put(messageCreator.generateEnergyMessage(id, count, energySource));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCheckPointMoved(int count, int xCoordinate, int yCoordinate) {
        try {
            messages.put(messageCreator.generateCheckpointMovedMessage(count, xCoordinate, yCoordinate));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendRefillShop(String[] cards){
        try{
            messages.put(messageCreator.generateRefillShopMessage(cards));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void sendExchangeShop(String[] cards){
        try{
            messages.put(messageCreator.generateExchangeShopMessage(cards));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendUpgradeBought(Player player, String card){
        try{
            messages.put(messageCreator.generateUpgradeBoughtMessage(player.getId(), card));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendBuyUpgrade(Player player) {
        try {
            messages.put(messageCreator.generateBuyUpgradeMessage(player.isBuying(), player.getUpgradeToBuy()));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public synchronized int getUniqueID() {
        return uniqueID++;
    }
    public String getProtocolVersion() {return this.protocolVersion;}

    //Added to stop server (for gui)
    public void stopServer() throws IOException {
        setOnline(false);
        this.alive = false;
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

}

