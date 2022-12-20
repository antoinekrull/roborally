package game.board;

import game.player.Player;
import game.robot.Robot;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class PitTile extends Tile{

    private int rebootTileIndex;

    public PitTile(int xCoordinate, int yCoordinate) {
        super(xCoordinate, yCoordinate"/textures/gameboard/foerderbandGeradeAnimated.gif");
        this.path = getClass().getResource("/textures/gameboard/foerderbandGeradeAnimated.gif").toString();
        isDanger = true;
        isBlocking = false;
    }

    public int getRebootTileIndex() {
        return rebootTileIndex;
    }
    public void setRebootTileIndex(int rebootTileIndex) {
        this.rebootTileIndex = rebootTileIndex;
    }
    @Override
    public void applyEffect(Player player) throws Exception {
        player.getRobot().reboot(this.getRebootTileIndex());
    }
}
