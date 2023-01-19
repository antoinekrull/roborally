package game.board;


public class Antenna extends Tile{
    public Antenna(int xCoordinate, int yCoordinate){
        super(xCoordinate, yCoordinate, "/textures/gameboard/StartAntenne.png", "Antenna");
        this.path = getClass().getResource("/textures/gameboard/StartAntenne.png").toString();
        this.imageFXid = "Antenna";
        setType("Antenna");
        isDanger = false;
        isBlocking = true;
    }
}
