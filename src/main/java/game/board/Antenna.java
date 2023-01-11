package game.board;

//TODO: Apply the correct texture file

public class Antenna extends Tile{
    public Antenna(int xCoordinate, int yCoordinate){
        super(xCoordinate, yCoordinate, "/textures/gameboard/foerderbandGeradeAnimated.gif");
        this.path = getClass().getResource("/textures/gameboard/foerderbandGeradeAnimated.gif").toString();
        isDanger = false;
        isBlocking = true;
    }
}
