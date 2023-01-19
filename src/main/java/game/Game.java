package game;

import com.fasterxml.jackson.core.JsonProcessingException;
import game.board.*;
import game.card.*;
import game.player.Player;
import game.player.Robot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;
import server.connection.PlayerList;
import server.connection.Server;

import java.util.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Game implements Runnable {
    private GamePhase currentGamePhase;
    private Timer timer = new Timer();
    private String mapName;
    public static PlayerList playerList;
    public Board board = new Board();
    private Player activePlayer;
    public static SpamDeck spamDeck = new SpamDeck();
    public static VirusDeck virusDeck = new VirusDeck();
    public static TrojanDeck trojanDeck = new TrojanDeck();
    public static WormDeck wormDeck = new WormDeck();
    public static int currentRegister = 0;
    private LinkedList<Integer> readyList = new LinkedList<>();
    private String[] maps = {"DizzyHighway", "ExtraCrispy", "DeathTrap", "LostBearings"};
    private static Game INSTANCE;
    private ArrayList<CheckpointTile> checkpointTileArrayList = null;
    private ArrayList<ArrayList<Pair<Integer, Integer>>> robotLaserList = new ArrayList<>();
    private Server server;
    private final Logger logger = LogManager.getLogger(Game.class);

    private String jsonMap;

    private Game(){
        new Thread(this).start();
    }

    public static Game getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Game();
        }
        return INSTANCE;
    }
    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
    public void setPlayerList(PlayerList playerList) {
        this.playerList = playerList;
    }

    private void applyAllTileEffects() throws Exception {
        try {
            for(int x = 0; x < Board.conveyorBelt2List.size(); x++) {
                for(int y = 0; y < playerList.size(); y++) {
                    if(playerList.get(y).getRobot().getCurrentPosition().equals(Board.conveyorBelt2List.get(x).getPosition())) {
                        Board.conveyorBelt2List.get(x).applyEffect(playerList.get(y));
                    }
                }
            }

        for(int x = 0; x < Board.conveyorBelt1List.size(); x++) {
            for(int y = 0; y < playerList.size(); y++) {
                if(playerList.get(y).getRobot().getCurrentPosition().equals(Board.conveyorBelt1List.get(x).getPosition())) {
                    Board.conveyorBelt1List.get(x).applyEffect(playerList.get(y));
                }
            }
        }
        applyPushPanelEffects();
        for(int x = 0; x < Board.gearTileList.size(); x++) {
            for(int y = 0; y < playerList.size(); y++) {
                if(playerList.get(y).getRobot().getCurrentPosition().equals(Board.gearTileList.get(x).getPosition())) {
                    Board.gearTileList.get(x).applyEffect(playerList.get(y));
                }
            }
        }
        //TODO: needs to be changed (?)
        for(int x = 0; x < Board.laserTileList.size(); x++) {
            for(int y = 0; y < playerList.size(); y++) {
                if(playerList.get(y).getRobot().getCurrentPosition().equals(Board.laserTileList.get(x).getPosition())) {
                    Board.laserTileList.get(x).applyEffect(playerList.get(y));
                }
            }
        }
        //robotLaser
        computeRobotLaserPositions();
        for(int i = 0; i < playerList.size(); i++){
            for(int x = 0; x < robotLaserList.size(); x++) {
                for(int y = 0; y < robotLaserList.get(x).size(); y++) {
                    if(playerList.get(i).getRobot().getCurrentPosition().equals(robotLaserList.get(y))) {
                        playerList.get(i).addCard(game.Game.spamDeck.popCardFromDeck());
                    }
                }
            }
        }
        robotLaserList.clear();

        for(int x = 0; x < Board.energySpaceList.size(); x++) {
            for(int y = 0; y < playerList.size(); y++) {
                if(playerList.get(y).getRobot().getCurrentPosition().equals(Board.energySpaceList.get(x).getPosition())) {
                    Board.energySpaceList.get(x).applyEffect(playerList.get(y));
                }
            }
        }
        for(int x = 0; x < Board.checkpointList.size(); x++) {
            for(int y = 0; y < playerList.size(); y++) {
                if(playerList.get(y).getRobot().getCurrentPosition().equals(Board.checkpointList.get(x).getPosition())) {
                    Board.checkpointList.get(x).applyEffect(playerList.get(y));
                }
            }
        }
        } catch(IndexOutOfBoundsException e) {
            logger.warn("You're Robot can not move past this point." + e);
        }
    }

    private void applyPushPanelEffects() throws Exception {
        for (int i = 0; i < playerList.size(); i++) {
            if((pushPanelInTile(board.getTile(playerList.get(i).getRobot().getCurrentPosition())).getValue0())){
                int index = pushPanelInTile(board.getTile(playerList.get(i).getRobot().getCurrentPosition())).getValue1();
                if(((PushPanelTile) board.getTile(playerList.get(i).getRobot().getCurrentPosition()).get(index))
                        .getActiveRegisterList().contains(currentRegister)) {
                    applyTileEffects(board.getTile(playerList.get(i).getRobot().getCurrentPosition()), playerList.getPlayerFromList(i));
                }
            }
        }
    }

    private void applyTileEffects(ArrayList<Tile> tileList, Player player) throws Exception {
        for (int i = 0; i < tileList.size(); i++) {
            tileList.get(i).applyEffect(player);
        }
    }

    private Pair<Boolean, Integer> pushPanelInTile(ArrayList<Tile> tileList) {
        boolean result = false;
        int index= -1;
        if(tileList.size() == 1) {
            result = tileList.get(0) instanceof PushPanelTile;
            if(result){index =0;}
        } else {
            result = tileList.get(0) instanceof PushPanelTile || tileList.get(1) instanceof PushPanelTile;
            if(tileList.get(0) instanceof PushPanelTile){index = 0;}
            else if(tileList.get(1) instanceof PushPanelTile){index = 1;}
        }
        return new Pair<>(result, index);
    }

    private ArrayList<Robot> determinePriority() {
        Pair<Integer, Integer> antennaPosition = Board.antenna.getPosition();
        ArrayList<Robot> roboList = new ArrayList<>();
        for (PlayerList it = playerList; it.hasNext(); ) {
            Player player = it.next();
            roboList.add(player.getRobot());
        }
        roboList.sort((r1, r2) -> {
            double dist1 = Math.sqrt(Math.pow(r1.getCurrentPosition().getValue0() - antennaPosition.getValue0(), 2) + Math.pow(r1.getCurrentPosition().getValue1() - antennaPosition.getValue1(), 2));
            double dist2 = Math.sqrt(Math.pow(r2.getCurrentPosition().getValue0() - antennaPosition.getValue0(), 2) + Math.pow(r2.getCurrentPosition().getValue1(), 2));
            return Double.compare(dist1, dist2);
        });
        return roboList;
    }

    private void computeRobotLaserPositions(){
        //initializes the robotLaserList with the current robot positions
        for (int i = 0; i < playerList.size(); i++) {
            //initializes the robotLaserList with the current robot positions
            robotLaserList.add(new ArrayList<>());
            robotLaserList.get(i).add(playerList.get(i).getRobot().getCurrentPosition());
            //determines the current tile positions with robot lasers
            Pair<Integer, Integer> currentPosition;
            switch(playerList.get(i).getRobot().getDirection()){
                case EAST -> {
                    currentPosition = playerList.get(i).getRobot().getCurrentPosition();
                    currentPosition.setAt0(currentPosition.getValue0() + 1);
                    while(!board.tileIsBlocking(board.getTile(currentPosition))){
                        robotLaserList.get(i).add(currentPosition);
                        if(TileTakenByRobot(currentPosition)){
                            break;
                        }
                        currentPosition.setAt0(currentPosition.getValue0() + 1);
                        if(currentPosition.getValue0() > board.getBoard().size()){
                           break;
                        }
                    }
                }
                case SOUTH -> {
                    currentPosition = playerList.get(i).getRobot().getCurrentPosition();
                    currentPosition.setAt1(currentPosition.getValue1() + 1);
                    while(!board.tileIsBlocking(board.getTile(currentPosition))){
                        robotLaserList.get(i).add(currentPosition);
                        if(TileTakenByRobot(currentPosition)){
                            break;
                        }
                        currentPosition.setAt0(currentPosition.getValue1() + 1);
                        if(currentPosition.getValue1() > board.getBoard().get(i).size()){
                            break;
                        }
                    }
                }
                case WEST -> {
                    currentPosition = playerList.get(i).getRobot().getCurrentPosition();
                    currentPosition.setAt0(currentPosition.getValue0() - 1);
                    while(!board.tileIsBlocking(board.getTile(currentPosition))){
                        robotLaserList.get(i).add(currentPosition);
                        if(TileTakenByRobot(currentPosition)){
                            break;
                        }
                        currentPosition.setAt0(currentPosition.getValue0() - 1);
                        if(currentPosition.getValue0() < 0){
                            break;
                        }
                    }
                }
                case NORTH -> {
                    currentPosition = playerList.get(i).getRobot().getCurrentPosition();
                    currentPosition.setAt1(currentPosition.getValue1() - 1);
                    while(!board.tileIsBlocking(board.getTile(currentPosition))){
                        robotLaserList.get(i).add(currentPosition);
                        if(TileTakenByRobot(currentPosition)){
                            break;
                        }
                        currentPosition.setAt1(currentPosition.getValue1() - 1);
                        if(currentPosition.getValue1() < 0){
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean TileTakenByRobot(Pair<Integer, Integer> position){
        boolean result = false;
        for(int i = 0; i < playerList.size(); i++){
            if(position.equals(playerList.get(i).getRobot().getCurrentPosition())){
                result = true;
            }
        }
        return result;
    }

    public void runTimer() {
        server.sendTimerStarted();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                PlayerList unreadyPlayers = playerList.getUnreadyPlayers();
                for (int i = 0; i < unreadyPlayers.size(); i++) {
                    String[] placedCards = unreadyPlayers.get(i).fillRegisterWithRandomCards();
                    server.sendCardsYouGotNow(unreadyPlayers.get(i), placedCards);
                    unreadyPlayers.get(i).fillRegisterWithRandomCards();

                    // for testing purposes
                    unreadyPlayers.get(i).printRegisters();
                    logger.info("Time ran through");
                }
            }
        };
        timer.schedule(timerTask, 30000);
        server.sendTimerEnded(playerList.getUnreadyPlayers());
    }

    public GamePhase getCurrentGamePhase() {
        return currentGamePhase;
    }

    private void setCurrentGamePhase(GamePhase currentGamePhase) {
        this.currentGamePhase = currentGamePhase;
    }

    //TODO: finish this once client server connection allows for it
    public void pickStartLocationForRobot(Player player, int x, int y) {
        Pair<Integer, Integer> input = new Pair<>(x, y);
        player.getRobot().setCurrentPosition(input);
        player.setReady(true);
    }

    public void setStartDirectionForRobot(String input) {
        switch(input) {
            case "Deathtrap" -> {
                for(int i = 0; i < playerList.size(); i++) {
                    playerList.get(i).getRobot().setDirection(Direction.WEST);
                }
            }
            case "DizzyHighway", "ExtraCrispy", "LostBearings" -> {
                for(int i = 0; i < playerList.size(); i++) {
                    playerList.get(i).getRobot().setDirection(Direction.EAST);
                }
            }
        }
    }

    private void runSetupPhase() {
        server.sendActivePhase(0);
        System.out.println("Running Setup Phase now");
        System.out.println(maps[0]);
        playerList.setPlayerReadiness(false);
        while(!playerList.playersAreReady()) {
            //TODO: implement this properly
            //pickStartLocationForRobot();

        }
        //map name logic
        setStartDirectionForRobot(maps[0]);

    }

    private void runUpgradePhase(){
        server.sendActivePhase(1);

    }
    private void runProgrammingPhase(PlayerList playerList) throws InterruptedException {
        server.sendActivePhase(2);
        for(int i = 0; i < playerList.size(); i++) {
            server.sendYourCards(playerList.get(i));
        }
        playerList.setPlayersPlaying(true);
        while(!playerList.playersAreReady()) {
            logger.info("Waiting for players to be ready");
            Thread.sleep(5000);
            if(playerList.getAmountOfReadyPlayers() <= 1) {
                runTimer();
            }
        }
    }
    private void runActivationPhase() throws Exception {
        server.sendActivePhase(3);
        int playerRegisterLength = 5;
        while(!playerList.allPlayerRegistersActivated()) {
            for(int i = 0; i < playerList.size(); i++) {
                logger.debug("Activating registers");
                activateRegister(playerList.get(i));
                playerList.get(i).setStatusRegister(true, currentRegister);
            }
            currentRegister++;
            determinePriority();
            //checks if all registers have been activated
            if(currentRegister == playerRegisterLength) {
                for(int i = 0; i < playerList.size(); i++) {
                    logger.debug("Emptying card registers");
                    playerList.get(i).emptyAllCardRegisters();
                }
            }
            logger.debug("Applying tile effects");
            applyAllTileEffects();
        }
    }
    public void setServer(Server server) {
        this.server = server;
    }

    private void activateRegister(Player player) throws Exception {
        try{
            player.getCardFromRegister(currentRegister).applyEffect(player);
        } catch (IndexOutOfBoundsException e) {
            logger.warn("This register was not activated because you're Robot can not move past this point" + e);
        }
    }
    public String[] getMaps(){return this.maps;}
    public void addReady(int clientID) {readyList.add(clientID);}

    public void removeReady(int clientID) {
        for (int i = 0; i < readyList.size(); i++) {
            if (readyList.get(i).equals(clientID)) {
                readyList.remove(i);
            }
        }
    }
    public int getFirstReadyID() {
        if(readyList.size() > 0) {
            return readyList.getFirst();
        }
        else {
            return -1;
        }
    }
    public LinkedList<Integer> getReadyList() {
        return readyList;
    }

    public String getJsonMap() {
        return jsonMap;
    }

    public void setJsonMap(String jsonMap) {
        this.jsonMap = jsonMap;
    }

    public boolean checkIfStartTileIsTaken(int x, int y) {
        boolean result = false;
        for(StartTile startTile: Board.startTileList) {
            if(startTile.getXCoordinate() == x && startTile.getYCoordinate() == y) {
                if(startTile.isTaken()) {
                    result = true;
                    //Board.startTileList.remove(startTile);
                }
            }
        }
        return result;
    }

    @Override
    public void run() {
        logger.debug("This game is running");
        boolean readyToStart = false;
        while(!readyToStart){
            if (readyList.size()>=2){
                readyToStart = true;
                System.out.println("i can start now");
            }
        }

        /*runSetupPhase();
        while(true) {
            logger.debug("This game is running the Upgrade Phase now");
            runUpgradePhase();
            try {
                logger.debug("This game is running the Programming Phase now");
                runProgrammingPhase(playerList);
                logger.debug("This game is running the Activation Phase now");
                runActivationPhase();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }*/
    }

}
