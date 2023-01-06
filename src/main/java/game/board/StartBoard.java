package game.board;


import java.util.ArrayList;

/**
 * @author Antoine
 * @version 1.0
 */
public class StartBoard extends Board{

    public StartBoard(int rows, int columns){
        this.columns = columns;
        this.rows = rows;
        this.board = new ArrayList<ArrayList<Tile>>();
    }
}
