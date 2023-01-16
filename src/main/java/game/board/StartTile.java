package game.board;


public class StartTile extends Tile{
    public StartTile(int xCoordinate, int yCoordinate){
        super(xCoordinate, yCoordinate, "/textures/gameboard/StartTile.png");
        this.path = getClass().getResource("/textures/gameboard/StartTile.png").toString();
        setType("StartPoint");
        isDanger = false;
        isBlocking = false;
    }
}
