package game.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import communication.JsonSerializer;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

/**
 * @author Antoine, Moritz, Firas
 * @version 1.0
 */
public class Board {
    protected static int columns;
    protected static int rows;

    private int checkPointCount;

    protected static Tile[][][] board = new Tile[13][10][2];
    //protected static  ArrayList<Tile>[][] board = (ArrayList<Tile>[][]) new ArrayList[13][10];
    //protected static ArrayList<ArrayList<ArrayList<Tile>>> board = new ArrayList<ArrayList<ArrayList<Tile>>>();

    //Lists of used tiles on the board, would be iterated on during the activation phase
    public static ArrayList<ConveyorBeltTile> conveyorBelt2List = new ArrayList<>();
    public static ArrayList<ConveyorBeltTile> conveyorBelt1List = new ArrayList<>();
    public static ArrayList<PushPanelTile> pushPanelList = new ArrayList<>();
    public static ArrayList<GearTile> gearTileList = new ArrayList<>();
    public static ArrayList<LaserTile> laserTileList = new ArrayList<>();
    public static ArrayList<CheckpointTile> checkpointList = new ArrayList<>();
    public static ArrayList<EnergySpaceTile> energySpaceList = new ArrayList<>();
    public static ArrayList<Tile> robotLaserList = new ArrayList<>();
    public static ArrayList<RebootTile> rebootTileList = new ArrayList<>();

    public void setTile(int column, int row, Tile tile){
        //board[column][row].add(tile);
        if(board[column][row][0] == null) {
            board[column][row][0] = tile;
        } else {
            board[column][row][1] = tile;
        }
    }
    public static Tile[] getTile(Pair<Integer, Integer> position){
        return board[position.getValue0()][position.getValue1()];
    }

    public boolean tileIsBlocking(ArrayList<Tile> tileList) {
        boolean result = false;
        if(tileList.size() == 1) {
            result = tileList.get(0).isBlocking();
        } else {
            result = tileList.get(0).isBlocking() || tileList.get(1).isBlocking();
        }
        return result;
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
    public Tile[][][] getBoard() {
        return board;
    }

    //for testing purposes
    public void testBoard() {
        for(int x = 0; x < 13; x++){
            for(int y = 0; y < 10; y++) {
                System.out.println(board[x][y]);
            }
        }
    }




    public void createBoard(String jsonMap) throws JsonProcessingException {

        ArrayList<ArrayList<ArrayList<Object>>> temp = JsonSerializer.deserializeJson(jsonMap, ArrayList.class);


        try {
            for(int x = 0; x < 13; x++){
                for(int y = 0; y < 10; y++){
                    if(temp.get(x).get(y).size() == 1) {
                                String[] tileValues = temp.get(x).get(y).get(0).toString().split(",");
                                String type = getValueFromString(tileValues[0]);
                                switch(type) {
                                    case "Empty", "tbd" ->  setTile(x, y, new NormalTile(x, y));
                                    case "EnergySpace" ->  {
                                        setTile(x, y, new EnergySpaceTile(x, y));
                                        energySpaceList.add(new EnergySpaceTile(x, y));
                                    }
                                    //TODO: directions fixen
                                    case "ConveyorBelt" -> {
                                        ArrayList<Direction> directionIn = new ArrayList<>();
                                        int velocity = Integer.parseInt(getValueFromString(tileValues[2]));
                                        tileValues[3] = tileValues[3].substring(tileValues[3].indexOf("=") + 2);
                                        tileValues[4] = tileValues[4].substring(0, tileValues[4].indexOf("]"));
                                        directionIn.add(parseDirection(tileValues[4]));
                                        Direction directionOut = parseDirection(tileValues[3]);
                                        setTile(x, y, new ConveyorBeltTile(x, y, velocity, directionIn, directionOut));
                                        switch (velocity){
                                            case 1: conveyorBelt1List.add(new ConveyorBeltTile(x, y, velocity, directionIn, directionOut));
                                            case 2: conveyorBelt2List.add(new ConveyorBeltTile(x, y, velocity, directionIn, directionOut));
                                        }
                                    }
                                    //TODO: directions fixen
                                    case "Wall" -> {
                                        ArrayList<Direction> directionList = new ArrayList<>();
                                        String[] directionArray = tileValues[2].split(",");
                                        for(int i = 1; i < directionArray.length; i++) {
                                            directionList.add(parseDirection(directionArray[i]));
                                        }
                                        setTile(x, y, new WallTile(x, y, directionList));
                                    }
                                    case "Laser" -> {
                                        String directionLaser = getValueFromString(tileValues[2]);
                                        System.out.println(directionLaser.substring(1, directionLaser.length() - 1));
                                        setTile(x, y, new LaserTile(x, y, parseDirection(directionLaser)));
                                        laserTileList.add(new LaserTile(x, y, parseDirection(directionLaser)));
                                    }
                                    //TODO: needs to work with directions, once they have been added to json
                                    case "RestartPoint" -> {
                                        setTile(x, y, new RebootTile(x, y));
                                        rebootTileList.add(new RebootTile(x, y));
                                    }
                                    case "CheckPoint" -> {
                                        setTile(x, y, new CheckpointTile(x, y));
                                        increaseCheckPointCount();
                                        checkpointList.add(new CheckpointTile(x, y));
                                    }
                                }
                    } else if(temp.get(x).get(y).size() == 2) {
                        String[] tileValues1 = temp.get(x).get(y).get(0).toString().split(",");
                        String[] tileValues2 = temp.get(x).get(y).get(1).toString().split(",");
                        String type1 = getValueFromString(tileValues1[0]);
                        String type2 = getValueFromString(tileValues2[0]);
                        String type = getValueFromString(tileValues1[0]) + getValueFromString(tileValues2[0]);
                        switch (type) {
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
    private String getValueFromString(String input) {
        int typeIndexStartOff = input.indexOf("=") + 1;
        String value = input.substring(typeIndexStartOff);
        value.replaceAll("\\[", "");
        value.replaceAll("]", "");
        value.replaceAll("\\{", "");
        value.replaceAll("}", "");
        return value;
    }
}


