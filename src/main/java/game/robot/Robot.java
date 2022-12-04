package game.robot;

import game.board.Direction;
import game.board.Tile;
import game.player.Player;

/**
 * @author Moritz, Antoine, Firas
 * @version 1.0
 */
public class Robot {

    private Direction direction;
    private int currentObjective;

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    private Player owner;

    public int[] getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int[] currentPosition) {
        this.currentPosition = currentPosition;
    }

    private int[] currentPosition = new int[2];

    public int getCurrentObjective() {
        return currentObjective;
    }

    public void setCurrentObjective(int currentObjective) {
        this.currentObjective = currentObjective;
    }

    public void determineTileEffect(Tile tile){}
    public void checkCollision(){}

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }
    public void reboot(int rebootTileIndex){

    }
}
