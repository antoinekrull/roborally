package game.board;


public class Antenna extends Tile{
    public Direction getDirection() {
        return direction;
    }

    private Direction direction;
    public Antenna(int xCoordinate, int yCoordinate, Direction direction){
        super(xCoordinate, yCoordinate, "/textures/gameboard/StartAntenne.png", "Antenna");
        this.path = getClass().getResource("/textures/gameboard/StartAntenne.png").toString();
        this.imageFXid = "Antenna";
        this.direction=direction;
        setType("Antenna");
        isDanger = false;
        isBlocking = false;
    }
}
