package game.board;


public class Antenna extends Tile{
    public Antenna(int xCoordinate, int yCoordinate){
        super(xCoordinate, yCoordinate, "/textures/gameboard/StartAntenne.png");
        this.path = getClass().getResource("/textures/gameboard/StartAntenne.png").toString();
        setType("Antenna");
        isDanger = false;
        isBlocking = true;
    }
}
