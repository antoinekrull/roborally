package game.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.javatuples.Pair;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 * @author Antoine, Moritz, Firas
 * @version 1.0
 */
public class Board {
    protected static int columns;
    protected static int rows;

    private int checkPointCount;


    protected static ArrayList<ArrayList<ArrayList<Tile>>> board;

    //Lists of used tiles on the board, would be iterated on during the activation phase
    public static ArrayList<ConveyorBeltTile> conveyorBelt2List;
    public static ArrayList<ConveyorBeltTile> conveyorBelt1List;
    public static ArrayList<PushPanelTile> pushPanelList;
    public static ArrayList<GearTile> gearTileList;
    public static ArrayList<LaserTile> laserTileList;
    public static ArrayList<CheckpointTile> checkpointList;
    public static ArrayList<EnergySpaceTile> energySpaceList;
    public static ArrayList<Tile> robotLaserList;

    ArrayList<RebootTile> rebootTileList;
    public void setTile(int column, int row, Tile tile){
        board.get(column).get(row).add(tile);
    }
    public static ArrayList<Tile> getTile(Pair<Integer, Integer> position){
            return board.get(position.getValue0()).get(position.getValue1());
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
    public ArrayList<ArrayList<ArrayList<Tile>>> getBoard() {
        return board;
    }

    //for testing purposes
    public void testBoard() {
        for(int x = 0; x < board.size(); x++){
            for(int y = 0; y < 2; y++) {
                System.out.println(board.get(x).get(y).get(0));
            }
        }
    }

    // Old implementation of board using arrays instead of arrayList
    //public Tile[][] getBoard() {return board;}
    //public void setTile(int column, int row, Tile tile){board[column][row] = tile;}
    //protected static Tile[][] board = new Tile[13][10];

    public void createBoard(String jsonMap) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<ArrayList<ArrayList<TileElement>>> temp = objectMapper.readValue(jsonMap, new TypeReference<ArrayList<ArrayList<ArrayList<TileElement>>>>() {
        });
        System.out.println(temp.get(0).get(0).get(0).getType());


            try {
            for(int x = 0; x < 13; x++){
                for(int y = 0; y < 10; y++){
                    if(temp.get(x).get(y).size() == 1) {
                                TileElement tile = temp.get(x).get(y).get(0);
                                String type = tile.getType();
                                switch(type) {
                                    case "Empty", "tbd" ->  setTile(x, y, new NormalTile(x, y));
                                    case "EnergySpace" ->  {
                                        setTile(x, y, new EnergySpaceTile(x, y));
                                        energySpaceList.add(new EnergySpaceTile(x, y));
                                    }
                                    //TODO: directions fixen
                                    case "ConveyorBelt" -> {
                                        ArrayList<Direction> directionIn = new ArrayList<>();
                                        int velocity = tile.getSpeed();
                                        ArrayList<String> orientations = tile.getOrientations();
                                        for(int i = 1; i < orientations.size(); i++) {
                                            directionIn.add(parseDirection(orientations.get(i)));
                                        }
                                        /*Direction directionOut = parseDirection(directionArray[0]);
                                        setTile(x, y, new ConveyorBeltTile(x, y, velocity, directionIn, directionOut));
                                        switch (velocity){
                                            case 1: conveyorBelt1List.add(new ConveyorBeltTile(x, y, velocity, directionIn, directionOut));
                                            case 2: conveyorBelt2List.add(new ConveyorBeltTile(x, y, velocity, directionIn, directionOut));
                                        }*/
                                    }
                                    /*case "Wall" -> {
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
                                    }*/
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

                        switch (type1) {
                            case "Wall" -> {
                                ArrayList<Direction> directionList = new ArrayList<>();
                                String[] directionArray = tileValues1[2].split(",");
                                for(int i = 1; i < directionArray.length; i++) {
                                    directionList.add(parseDirection(directionArray[i]));
                                }
                                setTile(x, y, new WallTile(x, y, directionList));
                            }
                            case "ConveyorBelt" -> {
                                ArrayList<Direction> directionIn = new ArrayList<>();
                                int velocity = Integer.parseInt(getValueFromString(tileValues1[2]));
                                String[] directionArray = tileValues1[3].split(",");
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
                            case "Empty" -> {setTile(x, y, new NormalTile(x, y));}
                        }
                        switch (type2) {
                            case "Laser" -> {
                                String directionLaser = getValueFromString(tileValues2[2]);
                                setTile(x, y, new LaserTile(x, y, parseDirection(directionLaser)));
                                laserTileList.add(new LaserTile(x, y, parseDirection(directionLaser)));
                            }
                            case "Empty" -> {
                                setTile(x, y, new NormalTile(x, y));
                            }
                            case "EnergySpace" -> {
                                setTile(x, y, new EnergySpaceTile(x, y));
                                energySpaceList.add(new EnergySpaceTile(x, y));
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

    private String getValueFromString(String input) {
        int typeIndexStartOff = input.indexOf("=");
        //int typeIndexCutoff = input.indexOf(",");
        String value = input.substring(typeIndexStartOff);
        value.replaceAll("\\[", "");
        value.replaceAll("]", "");
        value.replaceAll("\\{", "");
        value.replaceAll("}", "");
        return value;
    }

}


