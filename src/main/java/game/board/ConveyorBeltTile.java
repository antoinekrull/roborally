package game.board;

import game.robot.Robot;
/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class ConveyorBeltTile extends Tile{
    private int velocity;
    private Direction directionIn;
    private Direction directionOut;
    public ConveyorBeltTile(int velocity, Direction directionIn, Direction directionOut) {
        setDanger(false);
        setBlocking(false);
        this.velocity = velocity;
        this.directionIn = directionIn;
        this.directionOut = directionOut;
    }

    @Override
    public void applyEffect(Robot robot) throws Exception {
        robot.setDirection(this.directionOut);
        int[] newPosition = {robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]};
        switch(this.directionOut){
            case NORTH -> newPosition[1] += velocity;
            case SOUTH -> newPosition[1]-= velocity ;
            case EAST -> newPosition[0] += velocity;
            case WEST -> newPosition[0] -= velocity;
            default -> throw(new Exception("Invalid direction"));
        }
        robot.setCurrentPosition(newPosition);
    }
}
