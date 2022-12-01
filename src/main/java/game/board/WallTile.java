package game.board;

/**
 * @author Antoine
 * @version 1.0
 */
public class WallTile extends Tile{

    public WallTile() {
        this.isDanger = false;
        this.isBlocking = true;
    }
}
