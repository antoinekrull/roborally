package game.board;

import java.util.ArrayList;

public class TileElement {
    public String getType() {
        return type;
    }

    public String getIsOnBoard() {
        return isOnBoard;
    }

    public ArrayList<String> getOrientations() {
        return orientations;
    }

    public ArrayList<String> orientations;

    public String type;
    public String isOnBoard;

    public int getSpeed() {
        return speed;
    }

    public int speed;

    public int getCount() {
        return count;
    }

    public int count;
}
