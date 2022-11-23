package game.board;

/**
 * @author Antoine
 * @version 1.0
 */
public class ConveyorBeltTile extends Tile{
    public int velocity;
    public Direction direction;
    public ConveyorBeltTile(int velocity, Direction direction) {
        this.isDanger = true;
        this.isBlocking = true;
        this.velocity = velocity;
        this.direction = direction;
    }
}
