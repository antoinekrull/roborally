package game.board;

import game.player.*;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class LaserTile extends Tile{
    private Direction los;

    public LaserTile(int xCoordinate, int yCoordinate, Direction LineOfSight) {
        super(xCoordinate, yCoordinate, "/textures/gameboard/foerderbandGeradeAnimated.gif");
        this.path = getClass().getResource("/textures/gameboard/foerderbandGeradeAnimated.gif").toString();
        isDanger = true;
        isBlocking = false;
        this.los = LineOfSight;
    }

    public Direction getLos() {
        return los;
    }
    public void setLos(Direction los) {this.los = los;}
    @Override
    public void applyEffect(Player player) throws Exception {
        player.addCard(game.Game.spamDeck.popCardFromDeck());
    }
}
