package game;

import game.board.Board;
import game.board.CheckpointTile;
import game.board.PushPanelTile;
import game.player.Player;
import game.card.*;
import server.PlayerList;

import java.util.ArrayList;

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

    private ArrayList<CheckpointTile> checkpointTileArrayList = null;

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
            //TODO: Add tile effects;
            applyAllTileEffects();

        }
    }

    private void activateRegister(Player player) throws Exception {
        player.getCardFromRegister(currentRegister).applyEffect(player);
    }

    //maybe implement with chosen deck as input value
    public Card drawDamageCard(Player player) {
        return null;
    }

}
