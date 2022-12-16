package game.board;

import game.robot.Robot;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class CheckpointTile extends Tile{

    public CheckpointTile(int xCoordinate, int yCoordinate) {
        super(xCoordinate, yCoordinate);
        isDanger = false;
        isBlocking = false;
    }

    @Override
    public void applyEffect(Robot robot) {
        robot.setCurrentObjective(robot.getCurrentObjective() + 1);
    }
}
