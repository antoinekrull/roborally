package game.board;

/**
 * @author Antoine
 * @version 1.0
 */
public class PitTile extends Tile{

    public PitTile() {
        this.isDanger = true;
        this.isBlocking = false;
    }
}
