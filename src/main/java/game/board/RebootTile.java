package game.board;

public class RebootTile extends Tile {
    public boolean isRebootTile;
    public Direction direction;

    public RebootTile(Direction direction){
        this.isRebootTile = true;
        this.isDanger = false;
        this.isBlocking = false;
        this.direction = direction;
    }
}
