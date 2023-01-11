package game.board;

//TODO: Apply the correct texture file

public class Antenna extends Tile{
    public Antenna(int xCoordinate, int yCoordinate){
        super(xCoordinate, yCoordinate, "/textures/gameboard/StartAntenne.png");
        this.path = getClass().getResource("/textures/gameboard/StartAntenne.png").toString();
        isDanger = false;
        isBlocking = true;
    }
}
