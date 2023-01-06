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
    protected static int columns;
    protected static int rows;

    private int checkPointCount;


    protected static ArrayList<ArrayList<Tile>> board = new ArrayList<ArrayList<Tile>>();

    //Lists of used tiles on the board, would be iterated on during the activation phase
    public static ArrayList<ConveyorBeltTile> conveyorBelt2List;
    public static ArrayList<ConveyorBeltTile> conveyorBelt1List;
    public static ArrayList<PushPanelTile> pushPanelList;
    public static ArrayList<GearTile> gearTileList;
    public static ArrayList<LaserTile> laserTileList;
    public static ArrayList<CheckpointTile> checkpointList;
    public static ArrayList<EnergySpaceTile> energySpaceList;
    public static ArrayList<Tile> robotLaserList;
    public void setTile(int column, int row, Tile tile){
        board.get(column).add(row, tile);
    }
    public static Tile getTile(Pair<Integer, Integer> position){
        return board.get(position.getValue0()).get(position.getValue1());
    }
    public static int getColumns() {return columns;}
    public static int getRows() {return rows;}
    public int getCheckPointCount() {
        return checkPointCount;
    }
    public void increaseCheckPointCount() {
        checkPointCount++;
    }
    public void setCheckPointCount(int checkPointCount) {
        this.checkPointCount = checkPointCount;
    }
    public ArrayList<ArrayList<Tile>> getBoard() {
        return board;
    }

    //for testing purposes
    public void testBoard() {
        for(int x = 0; x <= board.size(); x++){
            for(int y = 0; y <= board.get(x).size(); y++) {
                System.out.println(board.get(x).get(y).getClass());
            }
        }
    }

    // Old implementation of board using arrays instead of arrayList
    //public Tile[][] getBoard() {return board;}
    //public void setTile(int column, int row, Tile tile){board[column][row] = tile;}
    //protected static Tile[][] board = new Tile[13][10];

    public void createBoard(String jsonMap) throws JsonProcessingException {
        jsonMap.replaceAll("gameMap=", "");
        jsonMap.replaceAll("\\{" , "");
        jsonMap.replaceAll("}", "");
        jsonMap.replaceAll("\\[", "");
        jsonMap.replaceAll("]", "");
        HashMap<String, String> convertedMap = JsonSerializer.deserializeJson(jsonMap, HashMap.class);

        var entrySet = convertedMap.entrySet();
        try {
            for(int x = 0; x < 13; x++){
                for(int y = 0; y < 10; y++){
                    for(var entry: entrySet) {
                        if(entry.getKey().equals("type")) {
                            entrySet.iterator().next();
                            String input = entry.getValue();
                            switch(input) {
                                case "Empty", "tbd" ->  setTile(x, y, new NormalTile(x, y));
                                case "EnergySpace" ->  {
                                    setTile(x, y, new EnergySpaceTile(x, y));
                                    energySpaceList.add(new EnergySpaceTile(x, y));
                                }
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
                                    switch (velocity){
                                        case 1: conveyorBelt1List.add(new ConveyorBeltTile(x, y, velocity, directionIn, directionOut));
                                        case 2: conveyorBelt2List.add(new ConveyorBeltTile(x, y, velocity, directionIn, directionOut));
                                    }
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
                                    laserTileList.add(new LaserTile(x, y, parseDirection(entry.getValue())));
                                }
                                case "RestartPoint" -> {
                                    entrySet.iterator().next();
                                    entrySet.iterator().next();
                                    setTile(x, y, new RebootTile(x, y, parseDirection(entry.getValue())));
                                }
                                case "CheckPoint" -> {
                                    setTile(x, y, new CheckpointTile(x, y));
                                    increaseCheckPointCount();
                                    checkpointList.add(new CheckpointTile(x, y));
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


