package game.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import communication.JsonSerializer;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Integer.parseInt;

/**
 * @author Antoine, Moritz, Firas
 * @version 1.0
 */
public class Board {
    protected int columns;
    protected int rows;

    private int checkPointCount;
    protected Tile[][] board = new Tile[13][10];
    public void loadBoard(){}
    public void setTile(int column, int row, Tile tile){
        board[column][row] = tile;
    }

    public Tile getTile(Pair<Integer, Integer> position){
        return board[position.getValue0()][position.getValue1()];
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
    public Tile[][] getBoard() {
        return board;
    }

    public void createBoard(Object jsonMap) throws JsonProcessingException {
        HashMap<String, String> convertedMap = JsonSerializer.deserializeJson(jsonMap.toString(), HashMap.class);
        var entrySet = convertedMap.entrySet();
        try {
            for(int x = 0; x <= 13; x++){
                for(int y = 0; y <= 10; y++){
                    for(var entry: entrySet) {
                        if(entry.getKey().equals("type")) {
                            String input = entry.getValue();
                            switch(input) {
                                case "Empty", "tbd" ->  setTile(x, y, new NormalTile(x, y));
                                case "EnergySpace" ->  setTile(x, y, new EnergySpaceTile(x, y));
                                case "ConveyorBelt" -> {
                                    ArrayList<Direction> directionIn = new ArrayList<>();
                                    entrySet.iterator().next();
                                    entrySet.iterator().next();
                                    int velocity = parseInt(entry.getValue());
                                    entrySet.iterator().next();
                                    String[] directionArray = entry.getValue().split(",");
                                    for(int i = 1; i < directionArray.length; i++) {
                                        directionIn.add(parseDirection(directionArray[i]));
                                    }
                                    Direction directionOut = parseDirection(directionArray[0]);
                                    setTile(x, y, new ConveyorBeltTile(x, y, velocity, directionIn, directionOut));
                                }
                                case "Wall" -> {
                                    ArrayList<Direction> directionList = new ArrayList<>();
                                    entrySet.iterator().next();
                                    entrySet.iterator().next();
                                    String[] directionArray = entry.getValue().split(",");
                                    for(int i = 1; i < directionArray.length; i++) {
                                        directionList.add(parseDirection(directionArray[i]));
                                    }
                                    setTile(x, y, new WallTile(x, y, directionList));
                                }
                                case "Laser" -> {
                                    entrySet.iterator().next();
                                    entrySet.iterator().next();
                                    String directionLaser = entry.getValue();
                                    setTile(x, y, new LaserTile(x, y, parseDirection(entry.getValue())));
                                }
                                case "RestartPoint" -> {
                                    entrySet.iterator().next();
                                    entrySet.iterator().next();
                                    setTile(x, y, new RebootTile(x, y, parseDirection(entry.getValue())));
                                }
                                case "CheckPoint" -> {
                                    setTile(x, y, new CheckpointTile(x, y));
                                    increaseCheckPointCount();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Direction parseDirection(String direction) {
        Direction parsedDirection = null;
        switch (direction) {
            case "left" -> {parsedDirection = Direction.WEST;}
            case "right" -> {parsedDirection = Direction.EAST;}
            case "top" -> {parsedDirection = Direction.NORTH;}
            case "bottom" -> {parsedDirection = Direction.SOUTH;}
        }
        return parsedDirection;
    }

}


