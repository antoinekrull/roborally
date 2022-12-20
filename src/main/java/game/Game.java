package game;

import game.player.Player;
import game.robot.Robot;
import game.board.GameBoard;
import game.board.Tile;
import game.card.*;
import javafx.util.Pair;
import server.PlayerList;

public class Game {
    private GamePhase currentGamePhase;
    public static PlayerList playerList;
    private GameBoard board;
    private Robot activeRobot;
    public static SpamDeck spamDeck = new SpamDeck();
    public static VirusDeck virusDeck = new VirusDeck();
    public static TrojanDeck trojanDeck = new TrojanDeck();
    public static WormDeck wormDeck = new WormDeck();
    //applyTileEffect would be called after the programming register is executed
    public void applyTileEffect() throws Exception {
        board.getTile(activeRobot.getCurrentPosition()).applyEffect(activeRobot);
    }
    public GamePhase getCurrentGamePhase() {
        return currentGamePhase;
    }

    int currentRegister;

    public void startGame(PlayerList playerList) {

    }
    public void setCurrentGamePhase(GamePhase currentGamePhase) {
        this.currentGamePhase = currentGamePhase;
    }

    public void runUpgradePhase(){

    }
    public void runProgrammingPhase(){

    }

    public void runActivationPhase(){

    }

    public void activateRegister(Player player) throws Exception {
        player.getCardFromRegister(currentRegister).applyEffect(player.getRobot());
    }

}
