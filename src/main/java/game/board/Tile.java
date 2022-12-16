package game.board;

import game.robot.Robot;
import org.javatuples.Pair;
import org.javatuples.Tuple;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public abstract class Tile {
    protected boolean isDanger;
    protected boolean isBlocking;
    private Pair<Integer, Integer> position;

    public Tile(int xCoordinate, int yCoordinate) {
        position = new Pair<>(xCoordinate, yCoordinate);
    }

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
    public Pair<Integer, Integer> getPosition() {
        return position;
    }
    public void setPosition(Pair<Integer, Integer> position) {
        this.position = position;
    }
    public int getXCoordinate() {
        return position.getValue0();
    }
    public int getYCoordinate(){
        return position.getValue1();
    }
    public void applyEffect(Robot robot) throws Exception{}
}
