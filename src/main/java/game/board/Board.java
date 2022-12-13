package game.board;

/**
 * @author Antoine
 * @version 1.0
 */
public abstract class Board {
    protected int columns;
    protected int rows;

    private int checkPointCount;
    protected Tile[][] board;
    public void loadBoard(){}
    public void setTile(int colum, int row, Tile tile){}
    public int getCheckPointCount() {
        return checkPointCount;
    }
    public void increaseCheckPointCount() {
        checkPointCount++;
    }
    public void setCheckPointCount(int checkPointCount) {
        this.checkPointCount = checkPointCount;
    }
}


