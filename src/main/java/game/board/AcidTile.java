package game.board;

/**
 * @author Antoine
 * @version 1.0
 */
public class AcidTile extends Tile{

    public AcidTile() {
        this.isDanger = true;
        this.isBlocking = true;
    }
}
