package game.board;

import game.player.Player;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class GearTile extends Tile {
    private Direction rotation;
    public GearTile(int xCoordinate, int yCoordinate, Direction rotation) {
        super(xCoordinate, yCoordinate, "/textures/gameboard/gearsNachLinksDrehen.png", "GearTile");
        this.rotation = rotation;
        if (rotation == this.rotation.LEFT) {
            this.path = getClass().getResource("/textures/gameboard/gearsNachLinksDrehen.png").toString();
            this.imageFXid = "GearNachLinks";
        }
        else {
            this.path = getClass().getResource("/textures/gameboard/gearsNachRechtsDrehen.png").toString();
            this.imageFXid = "GearNachRechts";
        }
        setType("Gear");
        isDanger = false;
        isBlocking = false;
    }
    public void setRotationType(Direction rotation) {
        this.rotation = rotation;
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        player.getRobot().rotateRobot(rotation);
    }
}
