package game;

import com.fasterxml.jackson.core.JsonProcessingException;
import game.board.*;
import game.card.*;
import game.player.Player;
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
    private final Timer timer = new Timer();
    public static PlayerList playerList = new PlayerList();
    public Board board = new Board();
    private Player activePlayer;
    public static SpamDeck spamDeck = new SpamDeck();
    public static VirusDeck virusDeck = new VirusDeck();
    public static TrojanDeck trojanDeck = new TrojanDeck();
    public static WormDeck wormDeck = new WormDeck();
    public static int currentRegister = 0;
    private boolean timerIsRunning=false;
    private LinkedList<Integer> readyList = new LinkedList<>();
    private final String[] maps = {"DizzyHighway", "ExtraCrispy", "DeathTrap", "LostBearings", "Twister"};
    private static Game INSTANCE;
    private boolean robotSet = false;
    private boolean setUpDone;
    private ArrayList<CheckpointTile> checkpointTileArrayList = null;
    private ArrayList<ArrayList<Pair<Integer, Integer>>> robotLaserList = new ArrayList<>();
    private Server server;
    private final Logger logger = LogManager.getLogger(Game.class);
    private String jsonMap;
    private String currentMap;
    private boolean gameIsRunning = true;

    private Game(){
        new Thread(this).start();
    }

    public static Game getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Game();
        }
        return INSTANCE;
    }
    public void setCurrentMap(String currentMap) {
        this.currentMap = currentMap;
    }
    public void setServerForPlayers() {
        for(int i = 0; i < playerList.size(); i++) {
            playerList.get(i).setServerForPlayerAndRobot(server);
            playerList.get(i).getRobot().setServer(server);
        }
    }
    public void setRobotSet(boolean robotSet) {
        this.robotSet = robotSet;
    }
    public Board getBoard() {
        return board;
    }
    public PlayerList getPlayerList() {
        return playerList;
    }

    public Player getPlayerFromPlayerListById(int id) {
        return playerList.getPlayerFromList(id);
    }

    public void setBoard(Board board) {
        this.board = board;
    }
    public void addPlayer(Player player) {
        playerList.add(player);
    }
    public Player getActivePlayer() {
        return activePlayer;
    }
    public void setPlayerList(PlayerList playerList) {
        Game.playerList = playerList;
    }

    private void applyAllTileEffects() throws Exception {
        try {
            for(int x = 0; x < board.getConveyorBelt2List().size(); x++) {
                for(int y = 0; y < playerList.size(); y++) {
                    if(playerList.get(y).getRobot().getCurrentPosition().equals(board.getConveyorBelt2List().get(x).getPosition())) {
                        board.getConveyorBelt2List().get(x).applyEffect(playerList.get(y));
                        //TODO: Add pit check
                    }
                }
            }

        for(int x = 0; x < board.getConveyorBelt1List().size(); x++) {
            for(int y = 0; y < playerList.size(); y++) {
                if(playerList.get(y).getRobot().getCurrentPosition().equals(board.getConveyorBelt1List().get(x).getPosition())) {
                    board.getConveyorBelt1List().get(x).applyEffect(playerList.get(y));
                    for(PitTile pitTile: board.getPitList()) {
                        if(pitTile.getPosition().equals(playerList.get(y).getRobot().getCurrentPosition())) {
                            reboot(playerList.get(y));
                        }
                    }
                }
            }
        }
        applyPushPanelEffects();
        for(int x = 0; x < board.getGearTileList().size(); x++) {
            for(int y = 0; y < playerList.size(); y++) {
                if(playerList.get(y).getRobot().getCurrentPosition().equals(board.getGearTileList().get(x).getPosition())) {
                    board.getGearTileList().get(x).applyEffect(playerList.get(y));
                }
            }
        }
        for(int x = 0; x < board.getLaserTileList().size(); x++) {
            for(int y = 0; y < playerList.size(); y++) {
                if(playerList.get(y).getRobot().getCurrentPosition().equals(board.getLaserTileList().get(x).getPosition())) {
                    board.getLaserTileList().get(x).applyEffect(playerList.get(y));
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

        for(int x = 0; x < board.getEnergySpaceList().size(); x++) {
            for(int y = 0; y < playerList.size(); y++) {
                if(playerList.get(y).getRobot().getCurrentPosition().equals(board.getEnergySpaceList().get(x).getPosition())) {
                    board.getEnergySpaceList().get(x).applyEffect(playerList.get(y));
                    server.sendEnergy(playerList.get(y), board.getEnergySpaceList().get(x));
                    Thread.sleep(100);
                }
            }
        }
        for(int x = 0; x < board.getCheckpointList().size(); x++) {
            for(int y = 0; y < playerList.size(); y++) {
                if(playerList.get(y).getRobot().getCurrentPosition().equals(board.getCheckpointList().get(x).getPosition())) {
                    board.getCheckpointList().get(x).applyEffect(playerList.get(y));
                }
            }
        }
        } catch(IndexOutOfBoundsException e) {
            logger.warn("You're Robot can not move past this point." + e);
        }
    }

    private void applyPushPanelEffects() throws Exception {
        for (int i = 0; i < playerList.size(); i++) {
            for (PushPanelTile pushPanelTile : board.getPushPanelList()) {
                //checks if any robos on pushPanel
                if (pushPanelTile.getPosition().equals(playerList.get(i).getRobot().getCurrentPosition())) {
                    //checks if pushpanel would be active in current register
                    if (pushPanelTile.getActiveRegisterList().contains(currentRegister)) {
                        logger.debug(3);
                        //activates effect of pushpanel
                        applyTileEffects(board.getTile(playerList.get(i).getRobot().getCurrentPosition()), playerList.get(i));
                        logger.debug(4);
                    }
                }
                //pit check
                for (PitTile pitTile : board.getPitList()) {
                    if (pitTile.getPosition().equals(playerList.get(i).getRobot().getCurrentPosition())) {
                        reboot(playerList.get(i));
                    }
                }
            }
        }
    }

    private void applyTileEffects(ArrayList<Tile> tileList, Player player) throws Exception {
        for (Tile tile : tileList) {
            tile.applyEffect(player);
        }
    }


    private void determinePriority() {
        Pair<Integer, Integer> antennaPosition = board.getAntenna().getPosition();
        playerList.getPlayerList().sort((p1, p2) -> {
            double dist1 = Math.sqrt(Math.pow(p1.getRobot().getCurrentPosition().getValue0() - antennaPosition.getValue0(), 2) + Math.pow(p1.getRobot().getCurrentPosition().getValue1() - antennaPosition.getValue1(), 2));
            double dist2 = Math.sqrt(Math.pow(p2.getRobot().getCurrentPosition().getValue0() - antennaPosition.getValue0(), 2) + Math.pow(p2.getRobot().getCurrentPosition().getValue1(), 2));
            return Double.compare(dist1, dist2);
        });
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
                    while(board.tileIsBlocking(board.getTile(currentPosition))){
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
                    while(board.tileIsBlocking(board.getTile(currentPosition))){
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
                    while(board.tileIsBlocking(board.getTile(currentPosition))){
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
                    while(board.tileIsBlocking(board.getTile(currentPosition))){
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

    private void runTimer() {
        timerIsRunning=true;
        System.out.println("Timer is running");
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
                    server.sendTimerEnded(playerList.getUnreadyPlayers());
                    playerList.setPlayerReadiness(true);
                }
            }
        };
        //TODO: MAKE THIS 30000 AGAIN!!!!
        timer.schedule(timerTask, 3000);
    }

    public GamePhase getCurrentGamePhase() {
        return currentGamePhase;
    }

    private void setCurrentGamePhase(GamePhase currentGamePhase) {
        this.currentGamePhase = currentGamePhase;
    }

    public void setStartDirectionForRobot(String input) {
        switch(input) {
            case "DeathTrap" -> {
                for(int i = 0; i < playerList.size(); i++) {
                    playerList.get(i).getRobot().setDirection(Direction.WEST);
                }
            }
            case "DizzyHighway", "ExtraCrispy", "LostBearings", "Twister" -> {
                for(int i = 0; i < playerList.size(); i++) {
                    playerList.get(i).getRobot().setDirection(Direction.EAST);
                }
            }
        }
    }

    private void runSetupPhase() {
        server.sendActivePhase(0);
        setServerForPlayers();
        try {
            Thread.sleep(100);
            logger.debug("Running Setup Phase now");
            for (int i = 0; i < readyList.size(); i++) {
                activePlayer = playerList.getPlayerFromList(readyList.get(i));
                server.sendCurrentPlayer(readyList.get(i));
                Thread.sleep(100);
                while (!robotSet) {
                    Thread.sleep(100);
                }
                this.robotSet = false;
                Thread.sleep(100);
            }
            logger.debug("finished setupphase");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setStartDirectionForRobot(currentMap);
        setUpDone = true;
    }

    private void runUpgradePhase(){
        logger.debug("This game is running the Upgrade Phase now");
        server.sendActivePhase(1);
        determinePriority();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private void runProgrammingPhase(PlayerList playerList) throws InterruptedException {
        logger.debug("This game is running the Programming Phase now");
        server.sendActivePhase(2);
        timerIsRunning = false;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < playerList.size(); i++) {
            playerList.get(i).drawFullHand();
            server.sendYourCards(playerList.get(i));
        }
        playerList.setPlayersPlaying(true);
        while(!playerList.playersAreReady()) {
            Thread.sleep(3000);
            if (playerList.getAmountOfReadyPlayers() <= 1) {
                if(!timerIsRunning) {
                    runTimer();
                }
            }
        }
        timerIsRunning=false;
        playerList.setPlayerReadiness(false);
    }
    private void runActivationPhase() throws Exception {
        logger.debug("This game is running the Activation Phase now");
        server.sendActivePhase(3);
        try {
            Thread.sleep(100);
        ArrayList<Card> cardList = new ArrayList<>();
        while(currentRegister < 5) {
            for(int i = 0; i < playerList.size(); i++) {
                logger.debug("Activating registers");
                playerList.get(i).getCardFromRegister(currentRegister).setClientID(playerList.get(i).getId());
                cardList.add(playerList.get(i).getCardFromRegister(currentRegister));
                activateRegister(playerList.get(i));
                //playerList.get(i).setStatusRegister(true, currentRegister);
            }
            server.sendCurrentCards(cardList);
            Thread.sleep(100);
            cardList.clear();
            logger.debug(currentRegister);
            Thread.sleep(1000);
            if(playerList.robotNeedsReboot()) {
                for(int i = 0; i < playerList.numberOfNeededReboots(); i++) {
                    reboot(playerList.get(i));
                }
            }
            Thread.sleep(1000);
            determinePriority();
            //checks if all registers have been activated
            if(currentRegister == 4) {
                for(int i = 0; i < playerList.size(); i++) {
                    logger.debug("Emptying card registers");
                    playerList.get(i).emptyAllCardRegisters();
                }
            }
            logger.debug("Applying tile effects");
            applyAllTileEffects();
            currentRegister++;
            /*
            if(checkIfPlayersReachedCheckPoints(playerList)){
                ArrayList<Pair<Integer, Integer>> playersReachedCheckpoints = playersThatReachedCheckpoints(playerList);
                for (Pair<Integer, Integer> playersReachedCheckpoint : playersReachedCheckpoints) {
                    //sends the player id and the number of the reached checkpoint for every player that reaches a
                    //checkpoint each played register
                    server.sendCheckpointReached(playersReachedCheckpoint);
                        Thread.sleep(100);
                }
            }
            if(checkIfPlayerWon(playerList)){
                //TODO: winner not working
                //Player winner = determineWhichPlayerWon(playerList);
                //logger.debug("The winning player is: " + winner);
                //sends a message to all clients
                //server.sendGameFinished(winner);
                    Thread.sleep(100);
                //stops the game thread
                gameIsRunning = false;


            }
        */
        } } catch (InterruptedException e) {
            e.printStackTrace();
        }
        currentRegister = 0;
    }
    public void setServer(Server server) {
        this.server = server;
    }

    /**
     * Checks every game round if a robot stands on a checkpoint that is his current objective.
     *
     * @param playerList The list of players who are playing the game at the moment
     * @return returns if at least one robot stands on a checkpoint
     */
    public boolean checkIfPlayersReachedCheckPoints(PlayerList playerList){
        for(CheckpointTile checkpointTile: board.getCheckpointList()) {
            for (int i = 0; i < playerList.size(); i++) {
                //checks if any robot is on a checkpoint and his current objective matches the checkpoint-number
                if (playerList.get(i).getRobot().getCurrentPosition().getValue0() == checkpointTile.getXCoordinate() &&
                    playerList.get(i).getRobot().getCurrentPosition().getValue1() == checkpointTile.getYCoordinate() &&
                    playerList.get(i).getRobot().getCurrentObjective() == checkpointTile.getNumber()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks every game round if a robot stands on a checkpoint that is his current objective.
     *
     * @param playerList The list of players who are playing the game at the moment
     * @return returns a list on (playerID, checkpointNumber) of each player that reached his current objective
     */
    public ArrayList<Pair<Integer, Integer>> playersThatReachedCheckpoints(PlayerList playerList){
        ArrayList<Pair<Integer, Integer>> playersThatReachedCheckpointsList = new ArrayList<>();
        for(CheckpointTile checkpointTile: board.getCheckpointList()) {
            for (int i = 0; i < playerList.size(); i++) {
                //checks if any robot is on a checkpoint and his current objective matches the checkpoint-number.
                //If so, the player ID is added to the list in combination with the checkpoint-number
                if (playerList.get(i).getRobot().getCurrentPosition().getValue0() == checkpointTile.getXCoordinate() &&
                    playerList.get(i).getRobot().getCurrentPosition().getValue1() == checkpointTile.getYCoordinate() &&
                    playerList.get(i).getRobot().getCurrentObjective() == checkpointTile.getNumber()) {
                    playersThatReachedCheckpointsList.add(new Pair<>(playerList.get(i).getId(), checkpointTile.getNumber()));
                }
            }
        }
        return playersThatReachedCheckpointsList;
    }

    private void activateRegister(Player player) throws Exception {
        try{
            player.getCardFromRegister(currentRegister).applyEffect(player);
            if(player.getCardFromRegister(currentRegister) instanceof PowerUpCard) {
                server.sendEnergy(player, player.getCardFromRegister(currentRegister));
                    Thread.sleep(100);
            }
            if(player.getCardFromRegister(currentRegister) instanceof WormCard) {
                reboot(player);
            }
            for(PitTile pitTile: board.getPitList()) {
                if (pitTile.getPosition().equals(player.getRobot().getCurrentPosition())) {
                    reboot(player);
                }
            }
        } catch (IndexOutOfBoundsException | InterruptedException e) {
            logger.warn("This register was not activated because you're Robot can not move past this point" + e);
        }
    }
    public String[] getMaps(){return this.maps;}
    public void addReady(int clientID) {
        readyList.add(clientID);
        if(clientID == getFirstReadyID()){
            server.sendSelectMap(maps);
            try {
                Thread.sleep(100);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeReady(int clientID) {
        for (int i = 0; i < readyList.size(); i++) {
            if (readyList.get(i).equals(clientID)) {
                int temp = readyList.get(i);
                int first = getFirstReadyID();
                readyList.remove(i);
                if(temp==first){
                    server.sendSelectMap(maps);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
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

    /**
     * Checks every game round if a player has won the game.
     *
     * @param playerList The list of players who are playing the game at the moment
     * @return returns if a Player has won the game
     */
    public boolean checkIfPlayerWon(PlayerList playerList){
        int checkpointsInGame = board.getCheckPointCount();
        for(int i = 0; i < playerList.size(); i++){
            //checks if the current Obj. of the robot is higher than the checkpoints in game. This should happen
            //when the robot gets to the last checkpoint
            if(playerList.get(i).getRobot().getCurrentObjective() == checkpointsInGame){
                return true;
            }
        }
        return false;
    }
    /**
     * Determines which player won the game.
     *
     * @param playerList The list of players who are playing the game at the moment
     * @return returns the Player that has won the game
     */
    private Player determineWhichPlayerWon(PlayerList playerList){
        int checkpointsInGame = board.getCheckPointCount();
        for(int i = 0; i < playerList.size(); i++){
            if(playerList.get(i).getRobot().getCurrentObjective() == checkpointsInGame){
                return playerList.get(i);
            }
        }
        return null;
    }

    public void setJsonMap(String jsonMap) {
        this.jsonMap = jsonMap;
    }
    public void createBoard(String map) {
        try {
            board.createBoard(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkIfStartTileIsTaken(int x, int y) {
        boolean result = false;
        for(StartTile startTile: board.getStartTileList()) {
            if(startTile.getXCoordinate() == x && startTile.getYCoordinate() == y) {
                if(startTile.isTaken()) {
                    result = true;
                }
            }
        }
        return result;
    }
    public void setStartPoint(int x, int y) {
        for(StartTile startTile: board.getStartTileList()) {
            if(startTile.getXCoordinate() == x && startTile.getYCoordinate() == y) {
                if(!startTile.isTaken()) {
                    startTile.setTaken(true);
                }
            }
        }
    }

    public void reboot(Player player) {
        player.getPersonalDiscardPile().addCard(spamDeck.popCardFromDeck());
        player.getPersonalDiscardPile().addCard(spamDeck.popCardFromDeck());
        player.discardEntireHand();
        player.emptyAllCardRegisters();
        player.getRobot().setCurrentPosition(board.getRebootTile().getPosition());
        server.sendReboot(player);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //TODO: Implement additional robo check
    }

    @Override
    public void run() {
        logger.debug("This game is running");
        boolean readyToStart = false;
        while(gameIsRunning) {
            //this while block checks when the game starts
            while (!readyToStart) {
                try {
                    //thread needs to sleep to check the if statement probably
                    Thread.sleep(100);
                    //if condition checks if at least two players are ready and a map was selected and if every joined player is ready
                    //TODO: ZU DEBUG ZWECKEN WURDE DIE MINDESTANZAHL AUF 1 GESETZT! MUSS WIEDER AUF 2
                    if (readyList.size() >= 1 && this.jsonMap != null && readyList.size()==playerList.size()) {
                        server.sendGameStarted(jsonMap);
                        Thread.sleep(100);
                        readyToStart = true;

                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            //TODO: Only run setup phase once
            if(!setUpDone){
                runSetupPhase();
            }
            //runUpgradePhase();
            try {
                runProgrammingPhase(playerList);
                runActivationPhase();
            } catch (Exception e) {
                logger.warn("An error occurred :" + e);
                throw new RuntimeException(e);
            }
        }
    }
}
