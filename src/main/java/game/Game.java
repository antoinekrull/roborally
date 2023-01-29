package game;

import com.fasterxml.jackson.core.JsonProcessingException;
import communication.JsonSerializer;
import communication.Message;
import game.board.*;
import game.card.*;
import game.player.AI_Player;
import game.player.Player;
import game.player.Robot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;
import server.connection.PlayerList;
import server.connection.Server;

import java.util.*;

import java.util.ArrayList;

public class Game implements Runnable {
    private GamePhase currentGamePhase;
    public static PlayerList playerList = new PlayerList();
    public Board board = new Board();
    private Player activePlayer;
    public static SpamDeck spamDeck = new SpamDeck();
    public static VirusDeck virusDeck = new VirusDeck();
    public static TrojanDeck trojanDeck = new TrojanDeck();
    public static WormDeck wormDeck = new WormDeck();
    public static UpgradeDeck upgradeDeck = new UpgradeDeck();
    public static ArrayList<Card> upgradeShop = new ArrayList<>();
    public static int currentRegister = 0;
    private boolean timerIsRunning=false;
    private LinkedList<Integer> readyList = new LinkedList<>();
    private final String[] maps = {"DizzyHighway", "ExtraCrispy", "DeathTrap", "LostBearings", "Twister"};
    private static Game INSTANCE;
    private boolean robotSet = false;
    private boolean isShopping = false;
    private boolean setUpDone;
    private ArrayList<CheckpointTile> checkpointTileArrayList = null;
    private ArrayList<ArrayList<Pair<Integer, Integer>>> robotLaserList = new ArrayList<>();
    private Server server;
    private final Logger logger = LogManager.getLogger(Game.class);
    private String jsonMap;
    private String currentMap;
    private boolean gameIsRunning = true;
    private CollisionCalculator collisionCalculator;

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
    public static int getCurrentRegister() {
        return currentRegister;
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
    public boolean isTimerIsRunning() {
        return timerIsRunning;
    }
    public void setTimerIsRunning(boolean timerIsRunning) {
        this.timerIsRunning = timerIsRunning;
    }
    public boolean isShopping() {
        return isShopping;
    }
    public void setShopping(boolean shopping) {
        isShopping = shopping;
    }


    private void applyAllTileEffects() throws Exception {
        try {
            for (int x = 0; x < playerList.size(); x++) {
                Pair position = playerList.get(x).getRobot().getCurrentPosition();
                for (int y = 0; y < board.getConveyorBelt2List().size(); y++) {
                    if (board.getConveyorBelt2List().get(y).getPosition().equals(position)) {
                        for (int i = 0; i < 2; i++) {
                            logger.info("Applying conveyor belt 2 ");
                            collisionCalculator.moveConveyorBelt(playerList.get(x).getRobot());
                        }
                    }
                }
            }
            Thread.sleep(500);
            for (int x = 0; x < playerList.size(); x++) {
                Pair position = playerList.get(x).getRobot().getCurrentPosition();
                for (int y = 0; y < board.getConveyorBelt1List().size(); y++) {
                    if (board.getConveyorBelt1List().get(y).getPosition().equals(position)) {
                        logger.info("Applying conveyor belt 1");
                        collisionCalculator.moveConveyorBelt(playerList.get(x).getRobot());
                    }
                }
            }
            if (currentMap.equals("Twister")) {
                for (CheckpointTile checkpointTile : board.getCheckpointList()) {
                    logger.warn("CHECKPOINT MOVING");
                    logger.warn(checkpointTile.getPosition());
                    collisionCalculator.moveConveyorBelt(checkpointTile);
                    int x = checkpointTile.getPosition().getValue0();
                    int y = checkpointTile.getPosition().getValue1();
                    server.sendCheckPointMoved(checkpointTile.getCount(), x, y);
                    Thread.sleep(100);
                    logger.warn(checkpointTile.getPosition());
                }
            }
            Thread.sleep(500);
            applyPushPanelEffects();

            for (int x = 0; x < board.getGearTileList().size(); x++) {
                for (int y = 0; y < playerList.size(); y++) {
                    if (playerList.get(y).getRobot().getCurrentPosition().equals(board.getGearTileList().get(x).getPosition())) {
                        board.getGearTileList().get(x).applyEffect(playerList.get(y));
                    }
                }
            }
            Thread.sleep(500);
            collisionCalculator.shootMapLasers();
            Thread.sleep(500);
            //robotLaser
            collisionCalculator.shootRobotLasers();
            Thread.sleep(500);
            for (int x = 0; x < board.getEnergySpaceList().size(); x++) {
                for (int y = 0; y < playerList.size(); y++) {
                    if (playerList.get(y).getRobot().getCurrentPosition().equals(board.getEnergySpaceList().get(x).getPosition())) {
                        logger.info("Applying energy tile effects");
                        board.getEnergySpaceList().get(x).applyEffect(playerList.get(y));
                        Thread.sleep(100);
                    }
                }
            }
            Thread.sleep(500);
            for (int x = 0; x < board.getCheckpointList().size(); x++) {
                for (int y = 0; y < playerList.size(); y++) {
                    if (playerList.get(y).getRobot().getCurrentPosition().equals(board.getCheckpointList().get(x).getPosition())) {
                        logger.info("Applying checkpoint effects");
                        logger.info("Number of Checkpoints " + board.getCheckpointList().size());
                        board.getCheckpointList().get(x).applyEffect(playerList.get(y));
                        if(playerList.get(y).getRobot().getCurrentObjective() == board.getCheckpointList().size()) {
                            logger.info("YAAAAAAAAAAY " + playerList.get(y).getUsername() + " WON THE GAME <3");
                            server.sendGameFinished(playerList.get(y));
                            gameIsRunning = false;
                        }
                    }
                }
            }
            Thread.sleep(500);
        } catch (IndexOutOfBoundsException e) {
            logger.warn("Your Robot can not move past this point." + e);
        }
    }

    private void applyPushPanelEffects() {
        for (int i = 0; i < playerList.size(); i++) {
            for (PushPanelTile pushPanelTile : board.getPushPanelList()) {
                //checks if any robos on pushPanel
                if (pushPanelTile.getPosition().equals(playerList.get(i).getRobot().getCurrentPosition())) {
                    //checks if pushpanel would be active in current register
                    if (pushPanelTile.getRegisters().contains(currentRegister + 1)) {
                        logger.info("Applying pushpanel effects");
                        Pair<Integer, Integer> temp = new Pair<>(0 ,0);
                        Pair<Integer, Integer> newPosition = new Pair<>(playerList.get(i).getRobot().getCurrentPosition().getValue0(),
                                playerList.get(i).getRobot().getCurrentPosition().getValue1());
                        switch(pushPanelTile.getPushDirection()) {
                            case NORTH -> temp = newPosition.setAt1(newPosition.getValue1() - 1);
                            case SOUTH -> temp = newPosition.setAt1(newPosition.getValue1() + 1);
                            case EAST -> temp = newPosition.setAt0(newPosition.getValue0() + 1);
                            case WEST -> temp = newPosition.setAt0(newPosition.getValue0() - 1);
                        }
                        collisionCalculator.moveRobot(playerList.get(i).getRobot(), temp);
                    }
                }
            }
        }
    }
    private void refreshUpgradeShop(){
        int leftoverCards;
        if(upgradeShop.size() == playerList.size()){
            upgradeShop.clear();
            for(int i = 0; i < playerList.size(); i++){
                upgradeShop.add(upgradeDeck.popCardFromDeck());
            }
            server.sendExchangeShop(upgradeShopContent());
        } else {
            leftoverCards = playerList.size() - upgradeShop.size();
            for(int i = 0; i < leftoverCards; i++){
                upgradeShop.add(upgradeDeck.popCardFromDeck());
            }
            server.sendRefillShop(upgradeShopContent());
        }
    }
    public String[] upgradeShopContent(){
        String[] upgradesOnOffer = new String[playerList.size()];
        for(int i = 0; i < playerList.size(); i++){
            upgradesOnOffer[i] = upgradeShop.get(i).getCard();
        }
        return upgradesOnOffer;
    }

    public void addPermanentUpgradeToPlayer(Player player, String cardName) {
        int energyCost = 0;
        Card newUpgrade = new NullCard();
        switch(cardName) {
            case "MemorySwap" -> {
                energyCost = 1;
                newUpgrade = getCardFromUpgradeShop(cardName);
            }
            case "SpamBlocker" -> {
                energyCost = 3;
                newUpgrade = getCardFromUpgradeShop(cardName);
            }
        }
        getPlayerFromPlayerListById(player.getId()).addTemporaryUpgrade(newUpgrade);
        int currentCubes = getPlayerFromPlayerListById(player.getId()).getRobot().getEnergyCubes();
        currentCubes = currentCubes - energyCost;
        getPlayerFromPlayerListById(player.getId()).getRobot().setEnergyCubes(currentCubes);
        server.sendEnergy(player.getId(), player.getRobot().getEnergyCubes(), "Upgrade");
    }

    public void addTemporaryUpgradeToPlayer(Player player, String cardName) {
        int energyCost = 0;
        Card newUpgrade = new NullCard();
        switch(cardName) {
            case "AdminPrivilege" -> {
                energyCost = 3;
                newUpgrade = getCardFromUpgradeShop(cardName);
            }
            case "RearLaser" -> {
                energyCost = 2;
                newUpgrade = getCardFromUpgradeShop(cardName);
            }
        }
        getPlayerFromPlayerListById(player.getId()).addTemporaryUpgrade(newUpgrade);
        int currentCubes = getPlayerFromPlayerListById(player.getId()).getRobot().getEnergyCubes();
        currentCubes = currentCubes - energyCost;
        getPlayerFromPlayerListById(player.getId()).getRobot().setEnergyCubes(currentCubes);
        server.sendEnergy(player.getId(), player.getRobot().getEnergyCubes(), "Upgrade");
    }

    public Card getCardFromUpgradeShop(String cardName) {
        Card result = null;
        for(int i = 0; i < upgradeShop.size(); i++) {
            if(upgradeShop.get(i).getCard().equals(cardName)) {
                result = upgradeShop.get(i);
                upgradeShop.remove(i);
                return result;
            }
        }
        return result;
    }

    //method for applying damage to robot
    public void drawDamageCards(Player player) {
        try {
            String[] drawnDamageCards = new String[player.getRobot().getDamageCount()];

                if(spamDeck.getSize() > 0) {
                    for(int i = 0; i < player.getRobot().getDamageCount() && spamDeck.getSize() > 0; i++) {
                        player.addCardToHand(spamDeck.popCardFromDeck());
                        drawnDamageCards[i] = "Spam";
                        logger.info("Spam added");
                    }
                    server.sendDrawDamage(player, drawnDamageCards);
                    Thread.sleep(100);
                } else {
                    ArrayList<String> availableDecks = new ArrayList<>();
                    if(wormDeck.getSize() > 0) {
                        availableDecks.add("Worm");
                    }
                    if(trojanDeck.getSize() > 0) {
                        availableDecks.add("Trojan");
                    }
                    if(virusDeck.getSize() > 0) {
                        availableDecks.add("Virus");
                    }
                    String[] availablePiles = availableDecks.toArray(String[]::new);
                    server.sendPickDamage(player, availablePiles);
                    Thread.sleep(100);
                }

            player.getRobot().setDamageCount(0);
            if(!(player instanceof AI_Player)){
                server.sendDrawDamage(player, drawnDamageCards);
            }
            else{
                ((AI_Player) player).sendDrawDamage(player, drawnDamageCards);
            }
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //called by HandleCLient when player chooses what cards to draw
    public void drawChosenDamageCards(Player player, String[] selectedDecks) {
        try {
            String[] drawnDamageCards = new String[selectedDecks.length];
            for(int i = 0; i < selectedDecks.length; i++) {
                switch (selectedDecks[i]) {
                    case "Worm" -> {
                        player.addCardToHand(wormDeck.popCardFromDeck());
                        drawnDamageCards[i] = "Worm";
                    }
                    case "Trojan" -> {
                        player.addCardToHand(trojanDeck.popCardFromDeck());
                        drawnDamageCards[i] = "Trojan";
                    }
                    case "Virus" -> {
                        player.addCardToHand(virusDeck.popCardFromDeck());
                        drawnDamageCards[i] = "Virus";
                    }
                }
            }
            if(!(player instanceof AI_Player)){
                server.sendDrawDamage(player, drawnDamageCards);
            }
            else{
                ((AI_Player) player).sendDrawDamage(player, drawnDamageCards);
            }
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void applyCardEffect(Player player, Card card) throws InterruptedException {
        String cardName = card.getCard();
        switch (cardName) {
            case "Again" -> {
                if (player.getCurrentRegister(card) == 0) {
                } else {
                    int previousRegister = player.getCurrentRegister(card) - 1;
                    applyCardEffect(player, player.getCardFromRegister(previousRegister));
                }
            }
            case "BackUp" -> {
                Pair<Integer, Integer> newPosition = new Pair<>(player.getRobot().getCurrentPosition().getValue0(),
                        player.getRobot().getCurrentPosition().getValue1());
                for (int i = 0; i < card.getVelocity(); i++) {

                        Pair<Integer, Integer> tempPosition;
                        switch (player.getRobot().getDirection()) {
                            case NORTH -> tempPosition = newPosition.setAt1(newPosition.getValue1() + 1);
                            case SOUTH -> tempPosition = newPosition.setAt1(newPosition.getValue1() - 1);
                            case EAST -> tempPosition = newPosition.setAt0(newPosition.getValue0() - 1);
                            case WEST -> tempPosition = newPosition.setAt0(newPosition.getValue0() + 1);
                            default -> tempPosition = newPosition;
                        }
                        newPosition = tempPosition;
                        collisionCalculator.moveRobot(player.getRobot(), newPosition);

                }
            }
            case "MoveI", "MoveII", "MoveIII" -> {

                for (int i = 0; i < card.getVelocity(); i++) {
                    Pair<Integer, Integer> newPosition = new Pair<>(player.getRobot().getCurrentPosition().getValue0(),
                            player.getRobot().getCurrentPosition().getValue1());
                    Pair<Integer, Integer> tempPosition;
                        switch (player.getRobot().getDirection()) {
                            case NORTH -> tempPosition = newPosition.setAt1(newPosition.getValue1() - 1);
                            case SOUTH -> tempPosition = newPosition.setAt1(newPosition.getValue1() + 1);
                            case EAST -> tempPosition = newPosition.setAt0(newPosition.getValue0() + 1);
                            case WEST -> tempPosition = newPosition.setAt0(newPosition.getValue0() - 1);
                            default -> tempPosition = newPosition;
                        }

                        newPosition = tempPosition;

                        collisionCalculator.moveRobot(player.getRobot(), newPosition);
                    Thread.sleep(500);
                }
            }
            case "PowerUp" -> {
                player.getRobot().increaseEnergyCubes("PowerUpCard");
            }
            case "Spam" -> {
                Card topProgrammingCard = player.getRobot().getDeck().popCardFromDeck();
                spamDeck.addCard(new SpamCard());
                player.setCardRegister(topProgrammingCard, currentRegister);
                server.sendReplaceCard(player);
                player.getCardFromRegister(currentRegister);
            }
            case "Trojan" -> {
                player.getRobot().increaseDamageCount();
                player.getRobot().increaseDamageCount();
                drawDamageCards(player);
            }
            case "TurnLeft" -> player.getRobot().rotateRobot(Direction.LEFT);
            case "TurnRight" -> player.getRobot().rotateRobot(Direction.RIGHT);
            case "UTurn" -> {
                for(int i = 0; i < 2; i++) {
                    player.getRobot().rotateRobot(Direction.LEFT);
                    Thread.sleep(500);
                }
            }
            case "Virus" -> {
                for (int i = 0; i < Game.playerList.size(); i++) {
                    if (isInRangeOfVirus(player.getRobot(), Game.playerList.get(i).getRobot())) {
                        Game.playerList.get(i).addCardToHand(Game.virusDeck.popCardFromDeck());
                    }
                }
            }
            case "Worm" -> reboot(player);
        }
    }

        //Helper method for virus card
    private boolean isInRangeOfVirus(Robot robot1, Robot robot2){
        if(robot1.getCurrentPosition().equals(robot2.getCurrentPosition())){ //if the condition is true then robot1 == robot2
            return false;
        } else {
            return Math.abs(robot1.getCurrentPosition().getValue0() - robot2.getCurrentPosition().getValue0()) <= 6
                    || Math.abs(robot1.getCurrentPosition().getValue1() - robot2.getCurrentPosition().getValue1()) <= 6;
        }
    }

    private boolean tileTakenByRobot(Pair<Integer, Integer> position){
        boolean result = false;
        for(int i = 0; i < playerList.size(); i++){
            if(position.equals(playerList.get(i).getRobot().getCurrentPosition())){
                result = true;
            }
        }
        return result;
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

    /**
     * Finds a robot id that is not used.
     *
     * @return Returns an int that describes an unused robot id
     */
    public int findUnusedRobotId() {
        int unusedId = 1;
        Set<Integer> set = new HashSet<>();
        for(Player player: playerList.getPlayerList()){
            set.add(player.getRobot().getFigure());
        }
        for (int i = 1; i <= 6; i++) {
            if (!set.contains(i)) {
                return i;
            }
        }
        return unusedId;
    }

    private void runSetupPhase() {
        logger.debug("Setup phase starts");
        server.sendActivePhase(0);
        logger.debug("Server sent the active phase (value=0) to the players");

        setServerForPlayers();
        logger.debug("Server set the server for all players and robots");
        try {
            Thread.sleep(100);
            for (int i = 0; i < readyList.size(); i++) {
                activePlayer = playerList.getPlayerFromList(readyList.get(i));
                if(activePlayer instanceof AI_Player){
                    ((AI_Player) activePlayer).sendPlayerAddedMessage();
                    logger.debug("AI player choose starting point.");
                    ((AI_Player) activePlayer).chooseStartingPoint(this);
                    setRobotSet(true);
                }
                logger.debug("Get active player " + i);
                server.sendCurrentPlayer(readyList.get(i));
                logger.debug("Server sent currentPlayer message");
                Thread.sleep(100);
                while (!robotSet) {
                    Thread.sleep(100);
                }
                this.robotSet = false;
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setStartDirectionForRobot(currentMap);
        setUpDone = true;
        logger.debug("all robots are set");
    }

    private void runUpgradePhase() {
        logger.info("This game is running the Upgrade Phase now");
        server.sendActivePhase(1);
        logger.debug("Server sent the active phase (value=1) to the players");
        setCurrentGamePhase(GamePhase.UPGRADE_PHASE);
        playerList.determinePriority(board.getAntenna());
        try {
            Thread.sleep(100);
            refreshUpgradeShop();
            for (int i = 0; i < playerList.size(); i++) {
                Thread.sleep(100);
                server.sendCurrentPlayer(playerList.get(i).getId());
                Thread.sleep(100);
                while (!isShopping) {
                    Thread.sleep(100);
                }
            }
            isShopping = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        if(!(activePlayer instanceof AI_Player) && activePlayer.isBuying()){
//            server.sendUpgradeBought(activePlayer, upgradeShop.get(cardIndex).getCard());
//            activePlayer.purchaseUpgrade(cardIndex);
//        }
    }

    private void runProgrammingPhase(PlayerList playerList) throws InterruptedException {
        server.sendActivePhase(2);
        logger.debug("Server sent the active phase (value=2) to the players");
        timerIsRunning = false;
        setCurrentGamePhase(GamePhase.PROGRAMMING_PHASE);
        try {
            CustomTimer customTimer = new CustomTimer(server);
            Thread.sleep(100);
            for (Player player : playerList.getPlayerList()) {
                logger.debug("Player " + player.getUsername() + " draws now.");
                player.drawFullHand();
                logger.debug("Player " + player.getUsername() + " has drawn.");
                for (int i = 0; i < playerList.size(); i++) {

                    playerList.get(i).drawFullHand();
                    Thread.sleep(100);
                    if (!(player instanceof AI_Player)) {
                        server.sendYourCards(player);
                        logger.debug("Server sent hand cards to " + player.getUsername());
                    }
                }
                playerList.setPlayersPlaying(true);
                while (!playerList.playersAreReady()) {
                    if (playerList.getAmountOfReadyPlayers() >= 1) {
                        Thread.sleep(100);
                        if (!timerIsRunning && !playerList.playersAreReady()) {
                            customTimer.runTimer();
                        }
                    }
                    for (Player aiPlayer : playerList.getPlayerList()) {
                        if (aiPlayer instanceof AI_Player && !aiPlayer.isReady()) {
                            ((AI_Player) aiPlayer).playProgrammingPhase();
                        }
                    }
                }
                if (timerIsRunning) {
                    customTimer.cancel();
                    timerIsRunning = false;
                }

            }
            playerList.setPlayerReadiness(false);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    private void runActivationPhase() throws Exception {
        logger.info("This game is running the Activation Phase now");
        server.sendActivePhase(3);
        setCurrentGamePhase(GamePhase.ACTIVATION_PHASE);
        try {
            Thread.sleep(100);
        ArrayList<Card> cardList = new ArrayList<>();
        while(currentRegister < 5 && gameIsRunning) {
            logger.debug("Current register = " + currentRegister);
            playerList.determinePriority(board.getAntenna());
            for(int i = 0; i < playerList.size(); i++) {
                Thread.sleep(1000);
                    cardList.add(playerList.get(i).getCardFromRegister(currentRegister));
                    activateRegister(playerList.get(i));
                playerList.get(i).getCardFromRegister(currentRegister).setClientID(playerList.get(i).getId());
            }
            server.sendCurrentCards(cardList);
            Thread.sleep(100);
            cardList.clear();
            Thread.sleep(1000);
            //checks if all registers have been activated
            if(currentRegister == 4) {
                for(int i = 0; i < playerList.size(); i++) {
                    playerList.get(i).emptyAllCardRegisters();
                }
            }
            Thread.sleep(1000);
            logger.info("Applying tile effects");
            applyAllTileEffects();
            ++currentRegister;
        } } catch (InterruptedException e) {
            e.printStackTrace();
        }
        currentRegister = 0;
    }
    public void setServer(Server server) {
        this.server = server;
    }

    private void activateRegister(Player player) {
        if (player.getCardFromRegister(currentRegister) instanceof NullCard) {
            logger.debug("No card in register" + currentRegister);
        } else {
            try {
                applyCardEffect(player, player.getCardFromRegister(currentRegister));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

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
            this.collisionCalculator = new CollisionCalculator(board, playerList, this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkIfStartTileIsTaken(int x, int y) {
        logger.debug("Check if starttile (" + x + "," + y + ") is taken");
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
        logger.info("Reboot under way");
        player.getRobot().increaseDamageCount();
        player.getRobot().increaseDamageCount();
        drawDamageCards(player);
        player.discardEntireHand();
        player.emptyAllCardRegisters();
        player.getRobot().setCurrentPosition(board.getRebootTile().getPosition());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        server.sendReboot(player);
//        for (int i = 0; i < playerList.size(); i++) {
//            if(player.getRobot().getCurrentPosition().equals(playerList.get(i).getRobot().getCurrentPosition())) {
//                Pair<Integer, Integer> newPosition = new Pair<>(player.getRobot().getCurrentPosition().getValue0()
//                , player.getRobot().getCurrentPosition().getValue1());
//                board.getRebootTile().getDirection()
//                collisionCalculator.moveRobot(playerList.get(i).getRobot(), newPosition);
//            }
//        }
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
            if(!setUpDone){
                runSetupPhase();
            }
            try {
                runUpgradePhase();
                runProgrammingPhase(playerList);
                Thread.sleep(100);
                runActivationPhase();
            } catch (Exception e) {
                logger.warn("An error occurred :" + e);
                throw new RuntimeException(e);
            }
        }
    }
}
