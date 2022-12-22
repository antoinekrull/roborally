package game;

import game.board.Board;
import game.board.CheckpointTile;
import game.board.PushPanelTile;
import game.player.Player;
import game.card.*;
import server.PlayerList;

import java.util.ArrayList;

public class Game implements Runnable {
    private GamePhase currentGamePhase;
    public static PlayerList playerList;
    private Board board;
    private Player activePlayer;
    public static SpamDeck spamDeck = new SpamDeck();
    public static VirusDeck virusDeck = new VirusDeck();
    public static TrojanDeck trojanDeck = new TrojanDeck();
    public static WormDeck wormDeck = new WormDeck();
    public static int currentRegister = 0;

    private static Game INSTANCE;

    private ArrayList<CheckpointTile> checkpointTileArrayList = null;

    private Game() {}

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

    public void applyTileEffect() throws Exception {
        board.getTile(activePlayer.getRobot().getCurrentPosition()).applyEffect(activePlayer);
    }

    //might not be necessary
    private void applyAllTileEffects() throws Exception{
        for (int i = 0; i < playerList.size(); i++) {
            board.getTile(playerList.getPlayerFromList(i).getRobot().getCurrentPosition()).applyEffect(playerList.getPlayerFromList(i));
        }
    }
    //Might be unnecessary
    private void applyPushPanelEffects() throws Exception {
        for (int i = 0; i < playerList.size(); i++) {
            if(board.getTile(playerList.getPlayerFromList(i).getRobot().getCurrentPosition()) instanceof PushPanelTile){
                if(((PushPanelTile) board.getTile(playerList.getPlayerFromList(i).getRobot().getCurrentPosition()))
                        .getActiveRegisterList().contains(currentRegister)) {
                    board.getTile(playerList.getPlayerFromList(i).getRobot().getCurrentPosition()).applyEffect(playerList.getPlayerFromList(i));
                }
            }
        }
    }

    //TODO: Implement this
    private PlayerList determinePriority() {
        PlayerList priorityList = null;
        return  priorityList;
    }

    //TODO: Implement this
    private void runTimer(){}

    public GamePhase getCurrentGamePhase() {
        return currentGamePhase;
    }

    private void setCurrentGamePhase(GamePhase currentGamePhase) {
        this.currentGamePhase = currentGamePhase;
    }

    private void runUpgradePhase(){

    }
    private void runProgrammingPhase(PlayerList playerList) throws InterruptedException {
        playerList.setPlayersPlaying(true);
        while(!playerList.playersAreReady()) {
            Thread.sleep(10000);
        }
    }

    private void runActivationPhase() throws Exception {
        int playerRegisterLength = 5;
        while(!playerList.allPlayerRegistersActivated()) {
            for(int i = 0; i < playerList.size(); i++) {
                activateRegister(playerList.get(i));
                playerList.get(i).setStatusRegister(true, currentRegister);
            }
            currentRegister++;
            //checks if all registers have been activated
            if(currentRegister == playerRegisterLength) {
                for(int i = 0; i < playerList.size(); i++) {
                    playerList.get(i).emptyAllCardRegisters();
                }
            }
            applyAllTileEffects();
            //TODO: check if players get damaged
        }
    }

    private void activateRegister(Player player) throws Exception {
        player.getCardFromRegister(currentRegister).applyEffect(player);
    }

    //TODO: Add checks to see if player gets damaged
    public void drawDamageCard(Player player, Deck deck) {
        player.getPersonalDiscardPile().addCard(deck.popCardFromDeck());
        player.setDamaged(false);
    }

    @Override
    public void run() {
        playerList.setPlayerReadiness(false);
        while(true) {
            runUpgradePhase();
            try {
                runProgrammingPhase(playerList);
                runActivationPhase();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
