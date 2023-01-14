package game;

import com.fasterxml.jackson.core.JsonProcessingException;
import game.board.*;
import game.card.*;
import game.player.Player;
import game.player.Robot;
import org.javatuples.Pair;
import server.connection.PlayerList;

import java.util.*;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static game.board.Board.robotLaserList;

public class Game implements Runnable {
    private GamePhase currentGamePhase;
    private Timer timer = new Timer();
    public static PlayerList playerList;
    public Board board = new Board();
    private Player activePlayer;
    public static SpamDeck spamDeck = new SpamDeck();
    public static VirusDeck virusDeck = new VirusDeck();
    public static TrojanDeck trojanDeck = new TrojanDeck();
    public static WormDeck wormDeck = new WormDeck();
    public static int currentRegister = 0;

    //TODO: Remove this?
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
        applyPushPanelEffects();
        for(int x = 0; x < Board.gearTileList.size(); x++) {
            for(int y = 0; y < playerList.size(); y++) {
                if(playerList.get(y).getRobot().getCurrentPosition().equals(Board.gearTileList.get(x).getPosition())) {
                    Board.gearTileList.get(x).applyEffect(playerList.get(y));
                }
            }
        }
        //TODO: needs to be changed
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

    //TODO: Implement this
    public void runTimer() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                PlayerList unreadyPlayers = playerList.getUnreadyPlayers();
                for (int i = 0; i < unreadyPlayers.size(); i++) {
                    unreadyPlayers.get(i).fillRegisterWithRandomCards();

                    // for testing purposes
                    unreadyPlayers.get(i).printRegisters();
                    System.out.println("Time ran through");
                }
            }
        };
        timer.schedule(timerTask, 30000);
    }

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
            System.out.println("Waiting for players to be ready");
            Thread.sleep(10000);
        }
    }

    //TODO: Make this private once testing in console is done
    public void runActivationPhase() throws Exception {
        int playerRegisterLength = 5;
        while(!playerList.allPlayerRegistersActivated()) {
            for(int i = 0; i < playerList.size(); i++) {
                System.out.println("Activating registers");
                activateRegister(playerList.get(i));
                playerList.get(i).setStatusRegister(true, currentRegister);
            }
            currentRegister++;
            //checks if all registers have been activated
            if(currentRegister == playerRegisterLength) {
                for(int i = 0; i < playerList.size(); i++) {
                    System.out.println("Emptying card registers");
                    playerList.get(i).emptyAllCardRegisters();
                }
            }
            System.out.println("Applying tile effects");
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

    //TEST!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void createBoard(String map) throws JsonProcessingException {
        //board.createBoard(map);
        //board.testBoard();
    }

    @Override
    public void run() {
        System.out.println("This game is running");
        playerList.setPlayerReadiness(false);
        while(true) {
            System.out.println("This game is running the Upgrade Phase now");
            runUpgradePhase();
            try {
                System.out.println("This game is running the Programming Phase now");
                runProgrammingPhase(playerList);
                System.out.println("This game is running the Activation Phase now");
                runActivationPhase();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
