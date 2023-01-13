package game.board;

import game.player.Player;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class PitTile extends Tile {

    private int rebootTileIndex;

    public PitTile(int xCoordinate, int yCoordinate) {
        super(xCoordinate, yCoordinate, "/textures/gameboard/pit.png");
        this.path = getClass().getResource("/textures/gameboard/pit.png").toString();
        setType("Pit");
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
