package game.board;

/**
 * @author Antoine
 * @version 1.0
 */
public class ConveyorBeltTile extends Tile{
    public int velocity;
    public Direction direction;
    public ConveyorBeltTile(int velocity, Direction direction) {
        this.isDanger = false;
        this.isBlocking = false;
        this.velocity = velocity;
        this.direction = direction;
    }
}
