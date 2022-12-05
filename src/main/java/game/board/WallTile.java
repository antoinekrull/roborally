package game.board;

import game.robot.Robot;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class WallTile extends Tile{
    public Direction[] getBlockedDirections() {
        return blockedDirections;
    }

    public void setBlockedDirections(Direction[] blockedDirections) {
        this.blockedDirections = blockedDirections;
    }

    private Direction[] blockedDirections;
    public WallTile(Direction[] blockedDirections) {
        this.isDanger = false;
        this.isBlocking = true;
        setBlockedDirections(blockedDirections);
    }

    @Override
    public void applyEffect(Robot robot) throws Exception {
        //has no effect, the robot movement is what detects collisions
    }
}
