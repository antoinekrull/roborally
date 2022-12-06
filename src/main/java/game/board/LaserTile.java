package game.board;

import game.robot.Robot;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class LaserTile extends Tile{
    private Direction los;
    public int getRebootTileIndex() {
        return rebootTileIndex;
    }
    public void setRebootTileIndex(int rebootTileIndex) {
        this.rebootTileIndex = rebootTileIndex;
    }
    private int rebootTileIndex;

    public LaserTile(Direction los) {
        this.isDanger = true;
        this.isBlocking = false;
        setLos(los);
    }

    public Direction getLos() {
        return los;
    }
    public void setLos(Direction los) {this.los = los;}
    @Override
    public void applyEffect(Robot robot) throws Exception {
        robot.reboot(this.getRebootTileIndex());
    }
    public void fireLaser() {
        
    }

}
