package game.board;

/**
 * @author Antoine
 * @version 1.0
 */
public abstract class Board {
    protected int columns;
    protected int rows;

    private int checkPointCount;
    protected Tile[][] board = new Tile[13][10];
    public void loadBoard(){}
    public void setTile(int column, int row, Tile tile){
        board[column][row] = tile;
    }
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


