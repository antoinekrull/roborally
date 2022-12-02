package game.robot;

import game.board.Direction;
import game.board.Tile;

public class Robot {

    private Direction direction;
    private int currentObjective;

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
}
