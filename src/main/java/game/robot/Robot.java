package game.robot;

import game.board.Direction;
import game.board.Tile;
import game.card.Card;
import game.player.Player;

/**
 * @author Moritz, Antoine, Firas
 * @version 1.0
 */
public class Robot {

    private Direction direction;
    private Player owner;
    private int figure;
    private int energyCubes;
    private int currentObjective;
    //currently only for DizzyHighWay, initialize position method needs to be implemented to set parameters
    private int[] currentPosition = new int[2];

    public Player getOwner() {
        return owner;
    }
    public void setOwner(Player owner) {
        this.owner = owner;
    }
    public int[] getCurrentPosition() {
        return currentPosition;
    }
    public void setCurrentPosition(int[] currentPosition) {
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


    public void determineTileEffect(Tile tile){}
    public void checkCollision(){}
    public void reboot(int rebootTileIndex){}



}
