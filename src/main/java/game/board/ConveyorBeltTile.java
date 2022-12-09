package game.board;

import game.robot.Robot;
/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class ConveyorBeltTile extends Tile{
    private int velocity;
    private Direction direction;
    public ConveyorBeltTile(int velocity, Direction direction) {
        isDanger = false;
        isBlocking = false;
        this.velocity = velocity;
        this.direction = direction;
    }

    public int getVelocity() { return velocity; }
    public void setVelocity(int velocity) { this.velocity = velocity; }
    public Direction getDirection() { return direction; }
    public void setDirection(Direction direction) { this.direction = direction; }

    @Override
    public void applyEffect(Robot robot) throws Exception {
        robot.setDirection(this.direction);
        int[] newPosition = {robot.getCurrentPosition()[0], robot.getCurrentPosition()[1]};
        switch(this.direction){
            case NORTH -> newPosition[1] += velocity;
            case SOUTH -> newPosition[1]-= velocity ;
            case EAST -> newPosition[0] += velocity;
            case WEST -> newPosition[0] -= velocity;
            default -> throw(new Exception("Invalid direction"));
        }
        robot.setCurrentPosition(newPosition);
    }
}
