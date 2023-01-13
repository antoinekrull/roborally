package game.board;

//TODO: Apply the correct texture file

public class StartTile extends Tile{
    public StartTile(int xCoordinate, int yCoordinate){
        super(xCoordinate, yCoordinate, "/textures/gameboard/reboot.png");
        this.path = getClass().getResource("/textures/gameboard/reboot.png").toString();
        setType("StartPoint");
        isDanger = false;
        isBlocking = false;
    }
}
