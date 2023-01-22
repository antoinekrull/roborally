package game.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;

import java.util.ArrayList;

/**0
 * @author Antoine, Moritz, Firas
 * @version 1.0
 */
public class Board {
    protected static int columns;
    protected static int rows;
    private int checkPointCount = 0;
    protected static ArrayList<ArrayList<ArrayList<Tile>>> board = new ArrayList<ArrayList<ArrayList<Tile>>>();
    //Lists of used tiles on the board, would be iterated on during the activation phase
    public static ArrayList<ConveyorBeltTile> conveyorBelt2List = new ArrayList<>();
    public static ArrayList<ConveyorBeltTile> conveyorBelt1List = new ArrayList<>();
    public static ArrayList<PushPanelTile> pushPanelList = new ArrayList<>();
    public static ArrayList<GearTile> gearTileList = new ArrayList<>();
    public static ArrayList<LaserTile> laserTileList = new ArrayList<>();
    public static ArrayList<CheckpointTile> checkpointList = new ArrayList<>();
    public static ArrayList<EnergySpaceTile> energySpaceList = new ArrayList<>();
    public static ArrayList<RebootTile> rebootTileList = new ArrayList<>();
    public static ArrayList<StartTile> startTileList = new ArrayList<>();
    public static Antenna antenna;
    private final static Logger logger = LogManager.getLogger(Board.class);

    public static ArrayList<Tile> getTile(Pair<Integer, Integer> position){
        try {
            return board.get(position.getValue0()).get(position.getValue1());
        } catch(IndexOutOfBoundsException e) {
            logger.warn("This tile is invalid " + e);
            return null;
        }
    }

    public boolean tileIsBlocking(ArrayList<Tile> tileList) {
        boolean result;
        if(tileList.size() == 1) {
            result = tileList.get(0).isBlocking();
        } else {
            result = tileList.get(0).isBlocking() || tileList.get(1).isBlocking();
        }
        return !result;
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
        int count = 0;
        for(int x = 0; x < board.size(); x++){
            for(int y = 0; y < board.get(x).size(); y++) {
                logger.debug(board.get(x).get(y).get(0).getClass() +" on Coordinates: (" + x+" ,"+y+")");
            }
        }
    }

    public void createBoard(String jsonMap) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        MapDeserializer map = objectMapper.readValue(jsonMap, MapDeserializer.class);
        board = map.getGameMap();
        //board = objectMapper.readValue(jsonMap, new TypeReference<ArrayList<ArrayList<ArrayList<Tile>>>>() {
        logger.debug("Board mit größe ("+board.size()+", "+board.get(0).size()+") erstellt!");

            try {
            for(int x = 0; x < board.size(); x++){
                for(int y = 0; y < board.get(x).size(); y++){
                    for(int i = 0; i < board.get(x).get(y).size(); i++) {
                        Tile tile = board.get(x).get(y).get(i);
                        String type = tile.getType();
                        switch (type) {
                            case "Empty", "tbd" -> replaceTileInMap(board, x, y, tile, new NormalTile(x, y));
                            case "EnergySpace" -> {
                                replaceTileInMap(board, x, y, tile, new EnergySpaceTile(x, y));
                                energySpaceList.add(new EnergySpaceTile(x, y));
                            }
                            case "ConveyorBelt" -> {
                                ArrayList<Direction> in = new ArrayList<>();
                                int velocity = tile.getSpeed();
                                ArrayList<String> orientations = tile.getOrientations();
                                Direction out = parseDirection(orientations.get(0));
                                for (int t = 1; t < orientations.size(); t++) {
                                    in.add(parseDirection(orientations.get(t)));
                                }
                                ConveyorBeltTile conveyor = new ConveyorBeltTile(x, y, velocity, in, out);
                                replaceTileInMap(board, x, y, tile, conveyor);

                                switch (velocity) {
                                    case 1:
                                        conveyorBelt1List.add(conveyor);
                                    case 2:
                                        conveyorBelt2List.add(conveyor);
                                }
                            }
                            case "Wall" -> {
                                ArrayList<Direction> directionList = new ArrayList<>();
                                ArrayList<String> orientations = tile.getOrientations();
                                for (int t = 0; t < orientations.size(); t++) {
                                    directionList.add(parseDirection(orientations.get(t)));
                                }
                                WallTile wall = new WallTile(x, y, directionList);
                                wall.setOrientations(orientations);
                                replaceTileInMap(board, x, y, tile, wall);
                            }
                            case "Laser" -> {
                                Direction directionLaser = parseDirection(tile.getOrientations().get(0));
                                LaserTile laser;
                                boolean onWall = false;
                                if(board.get(x).get(y).get(0).getType().equals("Wall")){
                                    ArrayList<Direction> directionsWall = new ArrayList<>();
                                    ArrayList<String> orientations = board.get(x).get(y).get(0).getOrientations();
                                    for (String orientation : orientations) {
                                        directionsWall.add(parseDirection(orientation));
                                    }
                                    for (Direction direction : directionsWall) {
                                        if (angelCalculation(directionLaser, direction) == 180) {
                                            onWall = true;
                                        }
                                    }
                                }
                                laser = new LaserTile(x, y, directionLaser, onWall);

                                replaceTileInMap(board,x,y,tile, laser);
                                laserTileList.add(laser);
                            }
                            //TODO: needs to work with directions, once they have been added to json
                            case "RestartPoint" -> {
                                replaceTileInMap(board, x, y, tile, new RebootTile(x, y));
                                rebootTileList.add(new RebootTile(x, y));
                            }
                            case "CheckPoint" -> {
                                increaseCheckPointCount();
                                replaceTileInMap(board, x, y, tile, new CheckpointTile(x, y, checkPointCount));
                                checkpointList.add(new CheckpointTile(x, y, checkPointCount));
                            }
                            case "StartPoint" -> {
                                replaceTileInMap(board, x, y, tile, new StartTile(x, y));
                            }
                            case "Antenna" -> {
                                replaceTileInMap(board, x, y, tile, new Antenna(x, y));
                                antenna = new Antenna(x, y);
                            }
                            case "PushPanel" -> {
                                String directionPushPanel = tile.getOrientations().get(0);
                                ArrayList<Integer> registers = tile.getRegisters();
                                PushPanelTile pushTile = new PushPanelTile(x,y, parseDirection(directionPushPanel),registers);
                                replaceTileInMap(board,x,y,tile, pushTile);
                                pushPanelList.add(pushTile);
                            }
                            case "Pit" -> {
                                replaceTileInMap(board, x, y, tile, new PitTile(x, y));
                            }
                            case "Gear" -> {
                                replaceTileInMap(board, x, y, tile, new GearTile(x, y, parseDirection(tile.getOrientations().get(0))));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("An error occurred: " + e);
        }
    }

    public Direction parseDirection(String direction) {
        Direction parsedDirection = null;
        switch (direction) {
            case "left" -> {parsedDirection = Direction.WEST;}
            case "right" -> {parsedDirection = Direction.EAST;}
            case "top" -> {parsedDirection = Direction.NORTH;}
            case "bottom" -> {parsedDirection = Direction.SOUTH;}
            case "clockwise" -> {parsedDirection = Direction.RIGHT;}
            case "counterclockwise" -> {parsedDirection = Direction.LEFT;}
        }
        return parsedDirection;

    }
    private void replaceTileInMap (ArrayList<ArrayList<ArrayList<Tile>>> map, int x, int y, Tile tile, Object object) {
        if (object instanceof Tile) {
            int index = map.get(x).get(y).indexOf(tile);
            map.get(x).get(y).remove(tile);
            map.get(x).get(y).add(index, (Tile) object);
        } else {
            logger.warn("something went wrong with replacing the TileElement");
        }
    }
    public int angelCalculation(Direction direct1, Direction direct2){
        int angel1 = 0;
        int angel2 = 0;
        switch (direct1){
            case NORTH -> angel1 =0;
            case EAST -> angel1 = 90;
            case SOUTH -> angel1 = 180;
            case WEST -> angel1 = 270;
        }
        switch (direct2) {
            case NORTH -> angel2 =0;
            case EAST -> angel2 = 90;
            case SOUTH -> angel2 = 180;
            case WEST -> angel2 = 270;
        }
        return Math.abs(angel1 - angel2);
    }

}


