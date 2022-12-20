package game;

import game.board.Board;
import game.board.PushPanelTile;
import game.player.Player;
import game.card.*;
import server.PlayerList;
import java.util.LinkedList;


public class Game {
    private GamePhase currentGamePhase;
    public static PlayerList playerList;
    private Board board;
    private Player activePlayer;
    public static SpamDeck spamDeck = new SpamDeck();
    public static VirusDeck virusDeck = new VirusDeck();
    public static TrojanDeck trojanDeck = new TrojanDeck();
    public static WormDeck wormDeck = new WormDeck();
    public static int currentRegister = 0;
    private LinkedList<Integer> readyList = new LinkedList<>();
    private String[] maps = {"DizzyHighway"};
    public Game(){
    }

    //applyTileEffect would be called after the programming register is executed
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
    private void activatePushPanels() throws Exception {
        for (int i = 0; i < playerList.size(); i++) {
            if(board.getTile(playerList.getPlayerFromList(i).getRobot().getCurrentPosition()) instanceof PushPanelTile){
                board.getTile(playerList.getPlayerFromList(i).getRobot().getCurrentPosition()).applyEffect(playerList.getPlayerFromList(i));
            }
        }
    }

    private PlayerList determinePriority() {
        PlayerList priorityList = null;
        return  priorityList;
    }

    public GamePhase getCurrentGamePhase() {
        return currentGamePhase;
    }

    public void startGame(PlayerList playerList) {
        if(playerList.playersAreReady()) {
            playerList.setPlayerReadiness(false);
            //start game
        }
    }
    private void setCurrentGamePhase(GamePhase currentGamePhase) {
        this.currentGamePhase = currentGamePhase;
    }

    private void runUpgradePhase(){

    }
    private void runProgrammingPhase(PlayerList playerList){
        playerList.setPlayersPlaying(true);
        while(!playerList.playersAreReady()) {
            //wait
        }
        //start next phase
    }

    private void runActivationPhase() throws Exception {
        while(!playerList.allPlayerRegistersActivated()) {
            for(int i = 0; i < playerList.size(); i++) {
                activateRegister(playerList.get(i));
                playerList.get(i).setStatusRegister(true, currentRegister);
            }
            currentRegister++;
            //TODO: Add tile effects;
        }
    }

    private void activateRegister(Player player) throws Exception {
        player.getCardFromRegister(currentRegister).applyEffect(player);
    }
    public String[] getMaps(){return this.maps;}

    //maybe implement with chosen deck as input value
    public Card drawDamageCard(Player player) {
        return null;
    }
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

}
