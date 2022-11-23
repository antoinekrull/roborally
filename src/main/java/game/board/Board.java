package game.board;

/**
 * @author Antoine
 * @version 1.0
 */
public abstract class Board {
    protected int columns;
    protected int rows;
    protected Tile[][] board;

    public void loadBoard(){}
    public void setTile(int colum, int row, Tile tile){}
}
