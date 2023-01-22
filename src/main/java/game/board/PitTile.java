package game.board;

import game.player.Player;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class PitTile extends Tile {

    public PitTile(int xCoordinate, int yCoordinate) {
        super(xCoordinate, yCoordinate, "/textures/gameboard/pit.png", "PitTile");
        this.path = getClass().getResource("/textures/gameboard/pit.png").toString();
        this.imageFXid = "PitTile";
        setType("Pit");
        isDanger = true;
        isBlocking = false;
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        //player.getRobot().reboot();
    }
}
