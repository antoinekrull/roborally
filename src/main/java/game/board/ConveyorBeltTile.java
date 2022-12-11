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
        isDanger = false;
        isBlocking = false;
        this.velocity = velocity;
        this.directionIn = directionIn;
        this.directionOut = directionOut;
    }

    public int getVelocity() { return velocity; }
    public void setVelocity(int velocity) { this.velocity = velocity; }
    public Direction getDirectionIn() { return directionIn; }
    public void setDirectionIn(Direction directionIn) { this.directionIn = directionIn; }

    public Direction getDirectionOut() {
        return directionOut;
    }

    public void setDirectionOut(Direction directionOut) {
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
