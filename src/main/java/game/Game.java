package game;

import game.board.GameBoard;
import game.board.Tile;
import game.card.Card;
import game.robot.Robot;
import javafx.util.Pair;
import server.PlayerList;

public class Game {
    private GamePhase currentGamePhase;
    private PlayerList playerList;
    private GameBoard board;
    private Robot activeRobot;
    //applyTileEffect would be called after the programming register is executed
    public void applyTileEffect() throws Exception {
        board.getTile(activeRobot.getCurrentPosition()).applyEffect(activeRobot);
    }
    public GamePhase getCurrentGamePhase() {
        return currentGamePhase;
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
}
