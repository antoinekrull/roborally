package game.board;

import game.robot.Robot;

/**
 * @author Firas
 * @version 1.0
 */
public class RebootTile extends Tile {
    public boolean isRebootTile;
    private static int rebootTileIndex = 0;
    public Direction direction;

    public RebootTile(Direction direction){
        this.isRebootTile = true;
        this.isDanger = false;
        this.isBlocking = false;
        this.direction = direction;
        setRebootTileIndex(rebootTileIndex++);
    }

    public void setRebootTileIndex(int rebootTileIndex) {
        this.rebootTileIndex = rebootTileIndex;
    }
    public static int getRebootTileIndex() {
        return rebootTileIndex;
    }

    @Override
    public void applyEffect(Robot robot) throws Exception {
        //robot.setCurrentPosition(rebootTileList.getByIndex(this.getRebootTileIndex().getLocation); //get position from the reboot tile list
        robot.setDirection(this.direction);
    }
}
