package game.board;

import game.robot.Robot;
import org.javatuples.Pair;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class LaserTile extends Tile{
    private Direction los;
    private int rebootTileIndex;

    public LaserTile(int xCoordinate, int yCoordinate, Direction LineOfSight) {
        super(xCoordinate, yCoordinate);
        isDanger = true;
        isBlocking = false;
        this.los = LineOfSight;
    }

    public int getRebootTileIndex() {
        return rebootTileIndex;
    }
    public void setRebootTileIndex(int rebootTileIndex) {
        this.rebootTileIndex = rebootTileIndex;
    }
    public Direction getLos() {
        return los;
    }
    public void setLos(Direction los) {this.los = los;}
    @Override
    public void applyEffect(Robot robot) throws Exception {
        robot.reboot(this.getRebootTileIndex());
    }
}
