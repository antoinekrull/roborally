package game.robot;

import game.board.Direction;
import game.board.Tile;
import game.card.Card;
import game.card.ProgrammingDeck;
import org.javatuples.Pair;

/**
 * @author Moritz, Antoine, Firas
 * @version 1.0
 */
public class Robot {

    private int figure;
    private int energyCubes;
    private int currentObjective;
    //currently only for DizzyHighWay, initialize position method needs to be implemented to set parameters
    private Direction direction;
    private Pair<Integer, Integer> currentPosition;
    private ProgrammingDeck deck;
    private boolean isRebooted = false;
    private Card[] register = new Card[5];
    private int activeRegister;

    public Robot(int figure) {
        this.figure = figure;
        deck.createDeck();
        energyCubes = 0;
        currentObjective = 1;
    }
    public void setRegister(int registerNumber, Card card){
        register[registerNumber] = card;
    }
    public void executeRegister(int registerNumber) throws Exception {
        register[registerNumber].applyEffect(this);
    }

    public Pair<Integer, Integer> getCurrentPosition() {
        return currentPosition;
    }
    public void setCurrentPosition(Pair<Integer, Integer> currentPosition) {
        this.currentPosition = currentPosition;
    }
    public int getCurrentObjective() {
        return currentObjective;
    }
    public void setCurrentObjective(int currentObjective) {
        this.currentObjective = currentObjective;
    }
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    public Direction getDirection() {
        return direction;
    }
    public int getFigure() {return figure;}
    public void setFigure(int figure) {this.figure = figure;}
    public int getEnergyCubes() {
        return energyCubes;
    }
    public void setEnergyCubes(int energyCubes) {
        this.energyCubes = energyCubes;
    }
    public void increaseEnergyCubes() {
        energyCubes++;
    }
    public ProgrammingDeck getDeck(){
        return deck;
    }
    public void setDeck(ProgrammingDeck deck){
        this.deck = deck;
    }
    public void determineTileEffect(Tile tile){}
    public void checkCollision(){}
    public void reboot(int rebootTileIndex){}
    public boolean getRebootStatus() {return isRebooted;}
    public void setRebootStatus(boolean rebooted) {isRebooted = rebooted;}
    public int getActiveRegister() {return activeRegister;}
    public void setActiveRegister(int activeRegister) {this.activeRegister = activeRegister;}

}
