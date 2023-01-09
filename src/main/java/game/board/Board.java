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


    protected static ArrayList<Tile>[][] board = new ArrayList[13][10];

    //Lists of used tiles on the board, would be iterated on during the activation phase
    public static ArrayList<ConveyorBeltTile> conveyorBelt2List = new ArrayList<>();
    public static ArrayList<ConveyorBeltTile> conveyorBelt1List = new ArrayList<>();
    public static ArrayList<PushPanelTile> pushPanelList = new ArrayList<>();
    public static ArrayList<GearTile> gearTileList = new ArrayList<>();
    public static ArrayList<LaserTile> laserTileList = new ArrayList<>();
    public static ArrayList<CheckpointTile> checkpointList = new ArrayList<>();
    public static ArrayList<EnergySpaceTile> energySpaceList = new ArrayList<>();
    public static ArrayList<Tile> robotLaserList = new ArrayList<>();
    ArrayList<RebootTile> rebootTileList = new ArrayList<>();

    public void setTile(int column, int row, Tile tile){
        board[column][row].add(tile);
    }
    public static ArrayList<Tile> getTile(Pair<Integer, Integer> position){
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
    public ArrayList<Tile>[][] getBoard() {
        return board;
    }

    //for testing purposes
    public void testBoard() {
        for(int x = 0; x < board.length; x++){
            for(int y = 0; y < 2; y++) {
                System.out.println(board[x][y].get(0));
            }
        }
    }

    public void createBoard(String jsonMap) throws JsonProcessingException {
        //initializes board
        for(int x = 0; x < 13; x++) {
            for(int y = 0; y < 10; y++) {
                board[x][y] = new ArrayList<>();
            }
        }


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
                                    case "ConveyorBelt" -> {
                                        ArrayList<Direction> directionIn = new ArrayList<>();
                                        int velocity = tile.getSpeed();
                                        ArrayList<String> orientations = tile.getOrientations();
                                        for(int i = 1; i < orientations.size(); i++) {
                                            directionIn.add(parseDirection(orientations.get(i)));
                                        }
                                        orientations.remove(orientations.size() - 1);
                                        Direction directionOut = parseDirection(orientations.get(orientations.size() - 1));
                                        setTile(x, y, new ConveyorBeltTile(x, y, velocity, directionIn, directionOut));

                                        switch (velocity){
                                            case 1: conveyorBelt1List.add(new ConveyorBeltTile(x, y, velocity, directionIn, directionOut));
                                            case 2: conveyorBelt2List.add(new ConveyorBeltTile(x, y, velocity, directionIn, directionOut));
                                        }
                                    }
                                    case "Wall" -> {
                                        ArrayList<Direction> directionList = new ArrayList<>();
                                        ArrayList<String> orientations = tile.orientations;
                                        for(int i = 1; i < directionList.size(); i++) {
                                            directionList.add(parseDirection(orientations.get(i)));
                                        }
                                        setTile(x, y, new WallTile(x, y, directionList));
                                    }
                                    case "Laser" -> {
                                        String directionLaser = tile.getOrientations().get(0);
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
                        TileElement tile1 = temp.get(x).get(y).get(0);
                        String type1 = tile1.getType();
                        TileElement tile2 = temp.get(x).get(y).get(1);
                        String type2 = tile2.getType();

                        switch (type1) {
                            case "Wall" -> {
                                ArrayList<Direction> directionList = new ArrayList<>();
                                ArrayList<String> orientations = tile1.orientations;
                                for(int i = 1; i < directionList.size(); i++) {
                                    directionList.add(parseDirection(orientations.get(i)));
                                }
                                setTile(x, y, new WallTile(x, y, directionList));
                            }
                            case "ConveyorBelt" -> {
                                ArrayList<Direction> directionIn = new ArrayList<>();
                                int velocity = tile1.getSpeed();
                                ArrayList<String> orientations = tile1.getOrientations();
                                for(int i = 1; i < orientations.size(); i++) {
                                    directionIn.add(parseDirection(orientations.get(i)));
                                }
                                orientations.remove(orientations.size() - 1);
                                Direction directionOut = parseDirection(orientations.get(orientations.size() - 1));
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
                                String directionLaser = tile2.getOrientations().get(0);
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


