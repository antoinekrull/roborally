package game.board;

import game.player.Player;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class CheckpointTile extends Tile {

    private int number;
    public CheckpointTile(int xCoordinate, int yCoordinate, int number) {
        super(xCoordinate, yCoordinate, "/textures/gameboard/checkpoint.png", "Checkpoint");
        this.path = getClass().getResource("/textures/gameboard/checkpoint.png").toString();
        this.imageFXid = "Checkpoint";
        setType("Checkpoint");
        isDanger = false;
        isBlocking = false;
        this.number = number;
    }

    @Override
    public void applyEffect(Player player) {
        player.getRobot().setCurrentObjective(player.getRobot().getCurrentObjective() + 1);
    }
    public int getNumber(){ return number; }
}
