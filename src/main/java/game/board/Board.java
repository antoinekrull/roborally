package game.board;

import client.model.ModelGame;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Point2D;
import org.javatuples.Pair;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**0
 * @author Antoine, Moritz, Firas
 * @version 1.0
 */
public class Board {
    protected static int columns;
    protected static int rows;

    private int checkPointCount;


    protected static ArrayList<ArrayList<ArrayList<Tile>>> board = new ArrayList<ArrayList<ArrayList<Tile>>>();

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
    public static ArrayList<StartTile> startTileList = new ArrayList<>();
    public static ArrayList<Antenna> antennaTileList = new ArrayList<>();

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
        int count = 0;
        for(int x = 0; x < board.size(); x++){
            for(int y = 0; y < board.get(x).size(); y++) {
                System.out.println(board.get(x).get(y).get(0).getClass() +" on Coordinates: (" + x+" ,"+y+")");
            }
        }
    }

    public void createBoard(String jsonMap) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        board = objectMapper.readValue(jsonMap, new TypeReference<ArrayList<ArrayList<ArrayList<Tile>>>>() {
        });

            try {
            for(int x = 0; x < 13; x++){
                for(int y = 0; y < 10; y++){
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
                            //TODO: Wall und Laser sollte normalerweise nicht einzeln vorkommen können
                            case "Wall" -> {
                                ArrayList<Direction> directionList = new ArrayList<>();
                                ArrayList<String> orientations = tile.getOrientations();
                                for (int t = 0; t < orientations.size(); t++) {
                                    directionList.add(parseDirection(orientations.get(t)));
                                }
                                replaceTileInMap(board, x, y, tile, new WallTile(x, y, directionList));
                            }
                            case "Laser" -> {
                                String directionLaser = tile.getOrientations().get(0);
                                if(board.get(x).get(y).get(0).getType().equals("Wall")){
                                    LaserTile laser = new LaserTile(x,y, parseDirection(directionLaser), true);
                                    replaceTileInMap(board,x,y,tile, laser);
                                    laserTileList.add(laser);

                                }
                                else {
                                    LaserTile laser = new LaserTile(x,y, parseDirection(directionLaser), false);
                                    replaceTileInMap(board, x, y, tile, laser);
                                    laserTileList.add(laser);
                                }


                            }
                            //TODO: needs to work with directions, once they have been added to json
                            case "RestartPoint" -> {
                                replaceTileInMap(board, x, y, tile, new RebootTile(x, y));
                                rebootTileList.add(new RebootTile(x, y));
                            }
                            case "CheckPoint" -> {
                                replaceTileInMap(board, x, y, tile, new CheckpointTile(x, y));
                                increaseCheckPointCount();
                                checkpointList.add(new CheckpointTile(x, y));
                            }
                            case "StartPoint" -> {
                                replaceTileInMap(board, x, y, tile, new StartTile(x, y));
                            }
                            case "Antenna" -> {
                                replaceTileInMap(board, x, y, tile, new Antenna(x, y));
                                antennaTileList.add(new Antenna(x, y));
                            }
                            //TODO: PushPanels need registers
                            case "PushPanel" -> {
                                String directionPushPanel = tile.getOrientations().get(0);
                                //replaceTileInMap(map,x,y,tile, new NormalTile(x,y, parseDirection(directionPushPanel));
                                //pushPanelList.add(new PushPanelTile(x, y, parseDirection(directionPushPanel)));
                            }
                            case "Pit" -> {
                                replaceTileInMap(board, x, y, tile, new PitTile(x, y));
                            }
                            case "Gear" -> {
                                replaceTileInMap(board, x, y, tile, new GearTile(x, y));
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
    private void replaceTileInMap (ArrayList<ArrayList<ArrayList<Tile>>> map, int x, int y, Tile tile, Object object) {
        if (object instanceof Tile) {
            int index = map.get(x).get(y).indexOf(tile);
            map.get(x).get(y).remove(tile);
            map.get(x).get(y).add(index, (Tile) object);
        } else {
            System.out.println("something went wrong with replacing the TileElement");
        }
    }

}


