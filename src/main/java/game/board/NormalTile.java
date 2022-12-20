package game.board;

import game.player.Player;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class NormalTile extends Tile{

    public NormalTile(int xCoordinate, int yCoordinate) {
        super(xCoordinate, yCoordinate);
        isDanger = false;
        isBlocking = false;
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        super.applyEffect(player);
    }
}
