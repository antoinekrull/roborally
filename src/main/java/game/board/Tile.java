package game.board;

import game.robot.Robot;

/**
 * @author Antoine
 * @version 1.0
 */
public abstract class Tile {
    public boolean isDanger;
    public boolean isBlocking;

    public void applyEffect(Robot robot) throws Exception{}
}
