package game;

import com.fasterxml.jackson.core.JsonProcessingException;
import game.board.Board;
import game.board.CheckpointTile;
import game.board.PushPanelTile;
import game.board.Tile;
import game.card.*;
import game.player.Player;
import org.javatuples.Pair;
import server.PlayerList;
import java.util.LinkedList;


import java.util.ArrayList;

import static game.board.Board.robotLaserList;

public class Game implements Runnable {
    private GamePhase currentGamePhase;
    public static PlayerList playerList;
    public Board board = new Board();
    private Player activePlayer;
    public static SpamDeck spamDeck = new SpamDeck();
    public static VirusDeck virusDeck = new VirusDeck();
    public static TrojanDeck trojanDeck = new TrojanDeck();
    public static WormDeck wormDeck = new WormDeck();
    public static int currentRegister = 0;
    private LinkedList<Integer> readyList = new LinkedList<>();
    private String[] maps = {"DizzyHighway"};

    private static Game INSTANCE;

    private ArrayList<CheckpointTile> checkpointTileArrayList = null;

    public Game() {}

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
        //applyPushPanelEffects();
        for(int x = 0; x < Board.gearTileList.size(); x++) {
            for(int y = 0; y < playerList.size(); y++) {
                if(playerList.get(y).getRobot().getCurrentPosition().equals(Board.gearTileList.get(x).getPosition())) {
                    Board.gearTileList.get(x).applyEffect(playerList.get(y));
                }
            }
        }
        //TODO: Might need to be changed
        for(int x = 0; x < Board.laserTileList.size(); x++) {
            for(int y = 0; y < playerList.size(); y++) {
                if(playerList.get(y).getRobot().getCurrentPosition().equals(Board.laserTileList.get(x).getPosition())) {
                    Board.laserTileList.get(x).applyEffect(playerList.get(y));
                }
            }
        }

        for(int x = 0; x < robotLaserList.size(); x++) {
            for(int y = 0; y < playerList.size(); y++) {
                if(playerList.get(y).getRobot().getCurrentPosition().equals(robotLaserList.get(x).getPosition())) {
                    playerList.get(y).addCard(game.Game.spamDeck.popCardFromDeck());
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
    }

    /*
    private void applyPushPanelEffects() throws Exception {
        for (int i = 0; i < playerList.size(); i++) {
            if((pushPanelInTile(board.getTile(playerList.getPlayerFromList(i).getRobot().getCurrentPosition())).getValue0())){
                int index = pushPanelInTile(board.getTile(playerList.getPlayerFromList(i).getRobot().getCurrentPosition())).getValue1();
                if(((PushPanelTile) board.getTile(playerList.getPlayerFromList(i).getRobot().getCurrentPosition()).get(index))
                        .getActiveRegisterList().contains(currentRegister)) {
                    applyTileEffects(board.getTile(playerList.getPlayerFromList(i).getRobot().getCurrentPosition()), playerList.getPlayerFromList(i));
                }
            }
        }
    }
     */

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
        }
    }

    private void activateRegister(Player player) throws Exception {
        player.getCardFromRegister(currentRegister).applyEffect(player);
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

    private void applyTileEffects(ArrayList<Tile> tileList, Player player) throws Exception {
        for(int i = 0; i < tileList.size(); i++) {
            tileList.get(i).applyEffect(player);
        }
    }
    //TEST!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void createBoard(String map) throws JsonProcessingException {
        board.createBoard(map);
        board.testBoard();
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
