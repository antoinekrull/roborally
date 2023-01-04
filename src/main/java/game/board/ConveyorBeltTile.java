package game.board;

import game.player.*;
import java.util.ArrayList;
import org.javatuples.Pair;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class ConveyorBeltTile extends Tile{
    private int velocity;
    private ArrayList<Direction> directionIn;
    private Direction directionOut;

    public ConveyorBeltTile(int xCoordinate, int yCoordinate, int velocity, ArrayList<Direction> directionIn, Direction directionOut) {
        super(xCoordinate, yCoordinate, "/textures/gameboard/foerderbandGeradeAnimated.gif");
        this.path = getClass().getResource("/textures/gameboard/foerderbandGeradeAnimated.gif").toString();
        isDanger = false;
        isBlocking = false;
        this.velocity = velocity;
        this.directionIn = directionIn;
        this.directionOut = directionOut;
    }

    public int getVelocity() { return velocity; }
    public void setVelocity(int velocity) { this.velocity = velocity; }
    public ArrayList<Direction> getDirectionIn() { return directionIn; }
    public void setDirectionIn(ArrayList<Direction> directionIn) { this.directionIn = directionIn; }

    public Direction getDirectionOut() {
        return directionOut;
    }

    public void setDirectionOut(Direction directionOut) {
        this.directionOut = directionOut;
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        player.getRobot().setDirection(this.directionOut);
        Pair<Integer, Integer> newPosition = new Pair<>(player.getRobot().getCurrentPosition().getValue0(), player.getRobot().getCurrentPosition().getValue1());
        switch(this.directionOut){
            case NORTH -> newPosition.setAt1(newPosition.getValue1() + velocity);
            case SOUTH -> newPosition.setAt1(newPosition.getValue1() - velocity);
            case EAST -> newPosition.setAt0(newPosition.getValue0() + velocity);
            case WEST -> newPosition.setAt0(newPosition.getValue0() - velocity);
            default -> throw(new Exception("Invalid direction"));
        }
        player.getRobot().setCurrentPosition(newPosition);
    }
}
