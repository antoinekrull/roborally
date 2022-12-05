package game.board;

import game.robot.Robot;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class CheckpointTile extends Tile{

    public CheckpointTile() {
        this.isDanger = false;
        this.isBlocking = false;
    }

    @Override
    public void applyEffect(Robot robot) {
        robot.setCurrentObjective(robot.getCurrentObjective() + 1);
    }
}
