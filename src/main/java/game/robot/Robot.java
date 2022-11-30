package game.robot;

import game.board.Direction;
import game.board.Tile;

public class Robot {

    private Direction direction;
    public void determineTileEffect(Tile tile){}
    public void checkCollision(){}

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }
}
