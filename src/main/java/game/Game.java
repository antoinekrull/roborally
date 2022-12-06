package game;

import game.card.Card;

public class Game {
    GamePhase currentGamePhase;

    public GamePhase getCurrentGamePhase() {
        return currentGamePhase;
    }
    public void setCurrentGamePhase(GamePhase currentGamePhase) {
        this.currentGamePhase = currentGamePhase;
    }
}
