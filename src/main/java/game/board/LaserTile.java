package game.board;

/**
 * @author Antoine
 * @version 1.0
 */
public class LaserTile extends Tile{

    public LaserTile() {
        this.isDanger = true;
        this.isBlocking = false;
    }
}
