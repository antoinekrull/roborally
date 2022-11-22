package game.board;

public abstract class Board {
    protected int columns;
    protected int rows;
    protected Tile[][] board;

    public void loadBoard(){}
    public void setTile(int colum, int row, Tile tile){}
}
