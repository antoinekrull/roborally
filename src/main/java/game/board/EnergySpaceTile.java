package game.board;

import game.robot.Robot;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class EnergySpaceTile extends Tile{

    public EnergySpaceTile(int xCoordinate, int yCoordinate) {
        super(xCoordinate, yCoordinate);
        isDanger = false;
        isBlocking = false;
    }

    @Override
    public void applyEffect(Robot robot) throws Exception {
        robot.setEnergyCubes(robot.getEnergyCubes() + 1);
    }
}
