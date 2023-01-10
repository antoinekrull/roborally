package game.board;

import game.player.Player;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class CheckpointTile extends Tile {

    public CheckpointTile(int xCoordinate, int yCoordinate) {
        super(xCoordinate, yCoordinate, "/textures/gameboard/checkpoint.png");
        this.path = getClass().getResource("/textures/gameboard/checkpoint.png").toString();
        isDanger = false;
        isBlocking = false;
    }

    @Override
    public void applyEffect(Player player) {
        player.getRobot().setCurrentObjective(player.getRobot().getCurrentObjective() + 1);
    }
}
