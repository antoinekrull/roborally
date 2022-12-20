package game;

import game.card.Card;
import game.player.Player;
import game.robot.Robot;
import server.PlayerList;

public class Game {
    private GamePhase currentGamePhase;
    private PlayerList playerList;
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
