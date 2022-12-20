package game;

import game.board.Board;
import game.player.Player;
import game.card.*;
import server.PlayerList;

public class Game {
    private GamePhase currentGamePhase;
    public static PlayerList playerList;
    private Board board;
    private Player activePlayer;
    public static SpamDeck spamDeck = new SpamDeck();
    public static VirusDeck virusDeck = new VirusDeck();
    public static TrojanDeck trojanDeck = new TrojanDeck();
    public static WormDeck wormDeck = new WormDeck();
    public static int currentRegister;
    //applyTileEffect would be called after the programming register is executed
    public void applyTileEffect() throws Exception {
        board.getTile(activePlayer.getRobot().getCurrentPosition()).applyEffect(activePlayer);
    }
    public GamePhase getCurrentGamePhase() {
        return currentGamePhase;
    }

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
        player.getCardFromRegister(currentRegister).applyEffect(player);
    }

}
