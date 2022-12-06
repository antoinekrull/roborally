package game.board;

import game.robot.Robot;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class PitTile extends Tile{
    public int getRebootTileIndex() {
        return rebootTileIndex;
    }
    public void setRebootTileIndex(int rebootTileIndex) {
        this.rebootTileIndex = rebootTileIndex;
    }

    private int rebootTileIndex;

    public PitTile() {
        setDanger(true);
        setBlocking(false);
    }

    @Override
    public void applyEffect(Robot robot) throws Exception {
        robot.reboot(this.getRebootTileIndex());
    }
}
