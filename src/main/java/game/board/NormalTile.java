package game.board;

import game.player.Player;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class NormalTile extends Tile{

    public NormalTile(int xCoordinate, int yCoordinate) {
        super(xCoordinate, yCoordinate, "/textures/gameboard/foerderbandGeradeAnimated.gif");
        this.path = getClass().getResource("/textures/gameboard/foerderbandGeradeAnimated.gif").toString();
        isDanger = false;
        isBlocking = false;
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        super.applyEffect(player);
    }
}
