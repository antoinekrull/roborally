package game.board;

import game.player.Player;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class EnergySpaceTile extends Tile {

    public EnergySpaceTile(int xCoordinate, int yCoordinate) {
        super(xCoordinate, yCoordinate, "/textures/gameboard/energySpace.png");
        this.path = getClass().getResource("/textures/gameboard/energySpace.png").toString();
        setType("EnergySpace");
        isDanger = false;
        isBlocking = false;
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        player.getRobot().setEnergyCubes(player.getRobot().getEnergyCubes() + 1);
    }
}
