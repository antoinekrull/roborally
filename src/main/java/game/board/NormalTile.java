package game.board;

import game.robot.Robot;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class NormalTile extends Tile{

    public NormalTile() {
        setDanger(false);
        setBlocking(false);
    }

    @Override
    public void applyEffect(Robot robot) throws Exception {
        super.applyEffect(robot);
    }
}
