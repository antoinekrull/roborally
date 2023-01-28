package game.board;

import game.player.Player;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class EnergySpaceTile extends Tile {

    public EnergySpaceTile(int xCoordinate, int yCoordinate, boolean single) {
        super(xCoordinate, yCoordinate, "/textures/gameboard/energySpace.png", "EnergySpace");
        this.path = getClass().getResource("/textures/gameboard/energySpace.png").toString();
        if (single){
            this.path = getClass().getResource("/textures/gameboard/energySpaceWithTile.png").toString();
        }
        this.imageFXid = "EnergySpace";
        setType("EnergySpace");
        isDanger = false;
        isBlocking = false;
        setEnergyCube(true);
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        player.getRobot().increaseEnergyCubes("EnergySpace");
    }

}
