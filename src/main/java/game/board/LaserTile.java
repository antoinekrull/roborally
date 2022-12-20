package game.board;

import game.card.SpamDeck;
import game.player.Player;
import game.robot.Robot;
import org.javatuples.Pair;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class LaserTile extends Tile{
    private Direction los;

    public LaserTile(int xCoordinate, int yCoordinate, Direction los) {
        super(xCoordinate, yCoordinate);
        isDanger = true;
        isBlocking = false;
        this.los = los;
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
