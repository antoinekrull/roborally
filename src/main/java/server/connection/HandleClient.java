package server.connection;

import communication.JsonSerializer;
import communication.Message;
import communication.MessageCreator;
import communication.MessageType;
import game.CollisionCalculator;
import game.Game;
import game.player.AI_Player;
import game.GamePhase;
import game.player.Player;
import game.player.Robot;
import javafx.beans.property.SimpleBooleanProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HandleClient implements Runnable{

    private DataInputStream in = null;
    private DataOutputStream out = null;
    private int threadID;
    private int clientID;
    private boolean exit;
    private boolean accepted;
    private boolean alive;
    private SimpleBooleanProperty serverStatus;
    public String address;
    public int port;
    public Socket socket;
    private String username;
    private String jsonMap = null;
    //private ServerMain.Server serverMain;

    private Server server;
    private int ai_id = 20;

    private MessageCreator messageCreator;
    private Game game;
    private final Logger logger = LogManager.getLogger(HandleClient.class);
    private JsonSerializer jsonSerializer;

    /**
     * Thread which handles the logged in clients.
     *
     * @param address
     * @param port
     * @param socket
     */
    public HandleClient(String address, int port, Socket socket, Server server, int threadID) {
        this.address = address;
        this.port = port;
        this.socket = socket;
        this.server = server;
        this.threadID = threadID;
        this.messageCreator = new MessageCreator();
        jsonSerializer = new JsonSerializer();
        //closes handler when stopServer method is called
        serverStatus = new SimpleBooleanProperty();
        serverStatus.bind(server.onlineProperty());
        serverStatus.addListener(event -> {
            if (!serverStatus.get()) {
                try {
                    closeHandler();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        this.game = Game.getInstance();
        game.setServer(server);
        try {
            this.in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            logger.warn("An error occurred: " + e);
        }
    }

    public boolean containsName(HashMap<Integer, HandleClient> list, final String name) {
        if (list.size() > 0) {
            for (Map.Entry<Integer, HandleClient> client : server.CLIENTS.entrySet()) {
                if (client.getValue().getUsername().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void write(Message message) {
        try {
            this.out.writeUTF(jsonSerializer.serializeJson(message));
        } catch (IOException e) {
            logger.warn("An error occurred: " + e);
        }
    }

    public void aliveMessage(int id) {
        try {
            for (Map.Entry<Integer, HandleClient> client : server.CLIENTS.entrySet()) {
                if (client.getValue().getClientID() == id) {
                    client.getValue().out.writeUTF(jsonSerializer.serializeJson(messageCreator.generateAliveMessage()));
                }
            }
        } catch (Exception e) {
            logger.warn("Client "+ id +" couldn't be reached\nAn exception occurred: " + e);
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    /**
     * Runs the server and game logic.
     */
    public void run() {
        setAlive(false);
        accepted = false;
        setClientID(threadID);

        //send protocol version to client
        //writeTo(getClientID(), messageCreator.generateHelloClientMessage(server.getProtocolVersion()));
        write(messageCreator.generateHelloClientMessage(server.getProtocolVersion()));

        try {

            String chatMessage;

            //Accept client if his protocol version is correct
            while(!accepted) {
                Message incomingMessage = jsonSerializer.deserializeJson(this.in.readUTF(), Message.class);
                try {
                    if (incomingMessage.getMessageType() == MessageType.HelloServer) {
                        logger.debug("HELLO SERVER MESSAGES: " + incomingMessage.getMessageBody().getProtocol());
                        if (incomingMessage.getMessageBody().getProtocol().equals(server.getProtocolVersion())){
                            accepted = true;
                            //writeTo(clientID, messageCreator.generateWelcomeMessage(clientID));
                            write(messageCreator.generateWelcomeMessage(clientID));
                        } else{
                            write(messageCreator.generateErrorMessage("Please check your protocol version. This server runs with: "+ server.getProtocolVersion()));
                            closeConnection();
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            Runnable sendAliveMessages = new Runnable() {
                public void run() {
                    while(!exit) {
                        try {
                            aliveMessage(clientID);
                            Thread.sleep(5000);
                            if (isAlive()) {
                                setAlive(false);
                            } else {
                                Message connectionMessage = messageCreator.generateConnectionUpdateMessage(getClientID(), false, "Remove");
                                server.sendConnectionLost(getClientID(), connectionMessage);
                                closeConnection();
                                exit = true;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            };
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(sendAliveMessages, 5, 5, TimeUnit.SECONDS);

            while (accepted) {
                Message incomingMessage = jsonSerializer.deserializeJson(this.in.readUTF(), Message.class);
                chatMessage = incomingMessage.getMessageBody().getMessage();
                try {
                    if(incomingMessage.getMessageType() == MessageType.HelloServer &&
                            incomingMessage.getMessageBody().getProtocol().equals(server.getProtocolVersion()) &&
                            incomingMessage.getMessageBody().isAI()){
                        // creates an ai player
                        int ai_id = server.getUniqueID();
                        Game.playerList.add(new AI_Player(ai_id, "ai_player", new Robot(game.findUnusedRobotId(), server.getUniqueID()), game.board));
                        Game.playerList.getPlayerFromList(ai_id).setIsAI(true);
                        logger.debug("AI player with the id " + ai_id + " was added");
                    }
                    //String changed to Message type (protocol, from: sender): added to linkedblockingqueue
                    if (incomingMessage.getMessageType() == MessageType.SendChat && incomingMessage.getMessageBody().getTo() == -1) {
                        int clientID = getClientID();
                        server.messages.put(messageCreator.generateReceivedChatMessage(chatMessage, clientID, false));
                    } else if (incomingMessage.getMessageType() == MessageType.SendChat) {
                        if (server.CLIENTS.containsKey(incomingMessage.getMessageBody().getTo())) {
                            int toUser = incomingMessage.getMessageBody().getTo();
                            server.sendPrivateMessage(toUser, messageCreator.generateReceivedChatMessage(chatMessage, getClientID(), true));
                        }
                    } else if (incomingMessage.getMessageType() == MessageType.Alive) {
                        setAlive(true);
                    } else if (incomingMessage.getMessageType() == MessageType.MapSelected) {

                        String map = incomingMessage.getMessageBody().getMap();
                        String fileName = "/maps/"+map+".json";
                        InputStream file = Objects.requireNonNull(HandleClient.class.getResourceAsStream(fileName));
                        BufferedReader content = new BufferedReader(new InputStreamReader(file));
                        this.jsonMap = content.lines().collect(Collectors.joining());

                        game.setJsonMap(jsonMap);
                        game.setCurrentMap(map);
                        game.createBoard(jsonMap);
                        server.messages.put(messageCreator.generateMapSelectedMessage(map));
                        //write(messageCreator.generateMapSelectedMessage(map));

                        //write(messageCreator.generateGameStartedMessage(jsonMap));

                    } else if (incomingMessage.getMessageType() == MessageType.CurrentPlayer) {
                        Game.playerList.getPlayerFromList(incomingMessage.getMessageBody().getClientID()).setPlaying(true);
                    } else if (incomingMessage.getMessageType() == MessageType.PlayCard) {
                        logger.info(incomingMessage.getMessageBody().getCard());
                        server.messages.put(messageCreator.generateCardPlayedMessage(incomingMessage.getMessageBody().getCard(),
                                getClientID()));
                    }
                    else if (incomingMessage.getMessageType() == MessageType.SelectedCard) {
                        if (game.getCurrentGamePhase() == GamePhase.PROGRAMMING_PHASE) {
                            Game.playerList.getPlayerFromList(getClientID()).playCard(incomingMessage.getMessageBody().getCard(),
                                    incomingMessage.getMessageBody().getRegister() - 1);
                            if (incomingMessage.getMessageBody().getCard().equals("Null")) {
                                Message cardRemovedMessage = messageCreator.generateCardSelectedMessage(getClientID(),
                                        incomingMessage.getMessageBody().getRegister(), false);
                                server.messages.put(cardRemovedMessage);
                            } else {
                                Message cardPlayedMessage = messageCreator.generateCardSelectedMessage(getClientID(),
                                        incomingMessage.getMessageBody().getRegister(), true);
                                server.messages.put(cardPlayedMessage);
                                logger.debug(incomingMessage.getMessageBody().getCard());
                            }
                        } else {
                            logger.warn("Card was changed during the" + game.getCurrentGamePhase() + "so it wasnt applied");
                        }
                    } else if(incomingMessage.getMessageType().equals(MessageType.BuyUpgrade)) {
                        logger.warn(incomingMessage.getMessageBody().isBuying());
                        if(game.getActivePlayer().getId() == clientID) {
                            if(!incomingMessage.getMessageBody().isBuying()) {
                                game.setShopping(true);
                            } else if(incomingMessage.getMessageBody().isBuying()) {
                                game.getPlayerFromPlayerListById(clientID);
                                game.setShopping(true);
                                switch (incomingMessage.getMessageBody().getCard()) {
                                    case "MemorySwap", "SpamBlocker" -> game.addTemporaryUpgradeToPlayer(game.getPlayerFromPlayerListById(clientID)
                                            , incomingMessage.getMessageBody().getCard());
                                    case "AdminPrivilege", "RearLaser" -> game.addPermanentUpgradeToPlayer(game.getPlayerFromPlayerListById(clientID)
                                            , incomingMessage.getMessageBody().getCard());
                                }
                                server.messages.put(messageCreator.generateUpgradeBoughtMessage(clientID
                                        , incomingMessage.getMessageBody().getCard()));
                            }
                        }
                    }
                    else if (incomingMessage.getMessageType() == MessageType.SelectedDamage) {
                        //Should be damage card
                        game.drawChosenDamageCards(game.getPlayerFromPlayerListById(getClientID())
                                , incomingMessage.getMessageBody().getCards());
                    } else if (incomingMessage.getMessageType() == MessageType.SetStartingPoint) {
                        if (clientID==game.getActivePlayer().getId()) {
                            int x = incomingMessage.getMessageBody().getX();
                            int y = incomingMessage.getMessageBody().getY();
                            Message startingPointTakenMessage = messageCreator.generateStartingPointTakenMessage(x, y, clientID);
                            if (!game.checkIfStartTileIsTaken(x, y)) {
                                game.setStartPoint(x, y);
                                server.messages.put(startingPointTakenMessage);
                                game.setRobotSet(true);
                                game.getPlayerFromPlayerListById(clientID).getRobot().setStartPosition(new Pair<>(x, y));
                            }
                            else {
                                write(messageCreator.generateErrorMessage("Starting point is already taken"));

                            }
                        }
                    } else if (incomingMessage.getMessageType() == MessageType.PlayerValues) {
                        this.username = incomingMessage.getMessageBody().getName();
                        int figure = incomingMessage.getMessageBody().getFigure();
                        if (game.playerList.size() == 0) {
                            Message robotAcceptedMessage = messageCreator.generatePlayerAddedMessage(this.username, figure, getClientID());
                            write(robotAcceptedMessage);
                            server.addPlayerToGame(new Player(getClientID(), incomingMessage.getMessageBody().getName()
                                    , new Robot(incomingMessage.getMessageBody().getFigure(), getClientID())));
                        }
                        else {
                            boolean taken = false;
                            for (int i = 0; i < game.playerList.size(); i++) {
                                if (game.playerList.get(i).getRobot() != null && game.playerList.get(i).getRobot().getFigure() == figure) {
                                    taken = true;
                                    write(messageCreator.generateErrorMessage("Your figure was already chosen. Choose another one."));
                                    break;
                                }
                            }
                            if (!taken) {

                                Message robotAcceptedMessage = messageCreator.generatePlayerAddedMessage(this.username, figure, getClientID());
                                write(robotAcceptedMessage);

                                server.addPlayerToGame(new Player(getClientID(), incomingMessage.getMessageBody().getName()
                                        , new Robot(incomingMessage.getMessageBody().getFigure(), getClientID())));
                                Message playerAddedMessage = messageCreator.generatePlayerAddedMessage(this.username, figure, getClientID());
                                server.sendPlayerValuesToAll(getClientID(), playerAddedMessage);

                                for(int i = 0; i < game.playerList.size(); i++) {
                                    if(game.playerList.get(i).getRobot() != null && game.playerList.get(i).getId() != getClientID()) {
                                        Message addOtherPlayer = messageCreator.generatePlayerAddedMessage(game.playerList.get(i).getUsername(), game.playerList.get(i).getRobot().getFigure(), game.playerList.get(i).getId());
                                        write(addOtherPlayer);
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < game.playerList.size(); i++) {
                            logger.debug(game.playerList.get(i).getId() + " " + game.playerList.get(i).getUsername());
                        }
                    } else if(incomingMessage.getMessageType() == MessageType.SetStatus) {
                        boolean ready = incomingMessage.getMessageBody().isReady();
                        if(ready){
                            game.addReady(clientID);
                            //sets all ais to ready when the first player is ready (to prevent that the player cant
                            // choose a map because he wasn't the first one to be ready)
                            for(Player player: game.getPlayerList().getPlayerList()){
                                if(player instanceof AI_Player && !player.isReady()){
                                    player.setReady(true);
                                    game.addReady(player.getId());
                                    server.messages.put(messageCreator.generatePlayerStatusMessage(player.getId(), true));
                                }
                            }
                            /*if(game.getFirstReadyID() == clientID){
                                write(messageCreator.generateSelectMapMessage(game.getMaps()));
                            }
                            if((game.getReadyList().size() >= 2)&&game.getJsonMap()!=null){
                                server.messages.put(messageCreator.generateGameStartedMessage(game.getJsonMap()));
                            }*/
                        } else {
                            game.removeReady(clientID);
                        }
                        server.messages.put(messageCreator.generatePlayerStatusMessage(clientID, ready));
                        //server.messages.put(messageCreator.generatePlayerStatusMessage(clientID, ready));
                    }
                    else if (incomingMessage.getMessageType() == MessageType.ConnectionUpdate) {

                    }
                } catch (Exception e) {
                    logger.warn("An exception occurred: " + e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeConnection(){
        try {
            game.playerList.remove(threadID);
            server.CLIENTS.remove(threadID);
            this.in.close();
            this.out.close();
            socket.close();
        } catch (Exception e) {
            logger.warn("An exception occurred: " + e);
        }

    }

    //when server is stopped, close handlers
    public void closeHandler() throws IOException {
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
        if (socket != null) {
            socket.close();
        }
    }

    public int getClientID() {
        return clientID;
    }
    public void setClientID(int id) {
        clientID = id;
    }
    public boolean isAlive() {
        return alive;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
