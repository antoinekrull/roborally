package game.board;

import game.player.Player;

import java.util.ArrayList;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class WallTile extends Tile {

    private ArrayList<Direction> blockedDirections;
    public WallTile(int xCoordinate, int yCoordinate, ArrayList<Direction> blockedDirections) {
        super(xCoordinate, yCoordinate, "/textures/gameboard/wall.png");
        this.path = getClass().getResource("/textures/gameboard/wall.png").toString();
        isDanger = false;
        isBlocking = true;
        this.blockedDirections = blockedDirections;
    }

    public ArrayList<Direction> getBlockedDirections() {
        return blockedDirections;
    }
    public void setBlockedDirections(ArrayList<Direction> blockedDirections) {
        this.blockedDirections = blockedDirections;
    }
    @Override
    public void applyEffect(Player player) throws Exception {
        //has no effect, the robot movement is what detects collisions
    }
}
