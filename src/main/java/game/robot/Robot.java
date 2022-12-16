package game.robot;

import game.board.Direction;
import game.board.Tile;
import game.card.Deck;
import game.card.ProgrammingDeck;
import game.player.Player;
import org.javatuples.Pair;

/**
 * @author Moritz, Antoine, Firas
 * @version 1.0
 */
public class Robot {

    private Player owner;
    private int figure;
    private int energyCubes;
    private int currentObjective;
    //currently only for DizzyHighWay, initialize position method needs to be implemented to set parameters
    private Direction direction;
    private Pair<Integer, Integer> currentPosition;
    private ProgrammingDeck deck;
    private boolean isRebooted = false;

    public Robot(int figure, Player owner) {
        this.figure = figure;
        this.owner = owner;
        deck.createDeck();
        energyCubes = 0;
        currentObjective = 1;
    }

    public Player getOwner() {
        return owner;
    }
    public void setOwner(Player owner) {
        this.owner = owner;
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

}
