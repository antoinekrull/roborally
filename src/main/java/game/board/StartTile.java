package game.board;


public class StartTile extends Tile{

    private boolean isTaken = false;
    public StartTile(int xCoordinate, int yCoordinate){
        super(xCoordinate, yCoordinate, "/textures/gameboard/StartTile.png", "StartTile");
        this.path = getClass().getResource("/textures/gameboard/StartTile.png").toString();
        this.imageFXid = "StartTile";
        setType("StartPoint");
        isDanger = false;
        isBlocking = false;
    }
    public boolean isTaken() {
        return isTaken;
    }
    public void setTaken(boolean taken) {
        isTaken = taken;
    }

}
