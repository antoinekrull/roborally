package game;

import game.card.Card;
import server.PlayerList;

public class Game {
    private GamePhase currentGamePhase;
    private PlayerList playerList;
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
