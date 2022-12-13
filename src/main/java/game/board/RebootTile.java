package game.board;

import game.robot.Robot;

/**
 * @author Firas
 * @version 1.0
 */
public class RebootTile extends Tile {
    private static int rebootTileIndex = 0;
    public Direction direction;

    public RebootTile(int xCoordinate, int yCoordinate, Direction direction){
        super(xCoordinate, yCoordinate);
        isDanger = false;
        isBlocking = false;
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
        robot.setCurrentPosition(getPosition());
        robot.setDirection(this.direction);
    }
}
