package game.board;

import game.robot.Robot;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public abstract class Tile {
    //TODO: make this private (moritz)
    public boolean isDanger;
    public boolean isBlocking;
    private int[][] position;

    public void applyEffect(Robot robot) throws Exception{}
}
