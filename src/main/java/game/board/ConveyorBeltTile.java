package game.board;

import game.robot.Robot;
/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class ConveyorBeltTile extends Tile{
    public int velocity;
    public Direction direction;
    public ConveyorBeltTile(int velocity, Direction direction) {
        setDanger(false);
        setBlocking(false);
        this.velocity = velocity;
        this.direction = direction;
    }

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
