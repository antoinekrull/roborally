package game.board;

import java.util.ArrayList;

public class TileElement {
    private ArrayList<String> orientations;

    private String type;
    private String isOnBoard;
    private int speed;
    private int count;
    private ArrayList<Integer> registers;

    public String getType() {
        return type;
    }
    public String getIsOnBoard() {
        return isOnBoard;
    }
    public ArrayList<String> getOrientations() {
        return orientations;
    }
    public int getSpeed() {
        return speed;
    }
    public int getCount() {
        return count;
    }
    public ArrayList<Integer> getRegisters() {
        return registers;
    }
    public void setRegisters(ArrayList<Integer> registers) {
        this.registers = registers;
    }
}
