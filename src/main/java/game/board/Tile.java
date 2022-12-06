package game.board;

import game.robot.Robot;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public abstract class Tile {
    private boolean isDanger;
    private boolean isBlocking;
    private int[][] position;

    public boolean isDanger() {
        return isDanger;
    }
    public void setDanger(boolean danger) {
        isDanger = danger;
    }
    public boolean isBlocking() {
        return isBlocking;
    }
    public void setBlocking(boolean blocking) {
        isBlocking = blocking;
    }
    public void applyEffect(Robot robot) throws Exception{}
}
