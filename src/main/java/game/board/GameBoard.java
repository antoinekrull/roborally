package game.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import communication.JsonSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Antoine, Moritz
 * @version 1.0
 */
public class GameBoard extends Board{

    public void createBoard(Object jsonMap) throws JsonProcessingException {
        HashMap<String, String> convertedMap = JsonSerializer.convertJsonToHashMap(jsonMap);
        var entrySet = convertedMap.entrySet();
        try {
            Direction[] directions = new Direction[4];
            for(int x = 0; x <= 13; x++){
                for(int y = 0; y <= 10; y++){
                    for(var entry: entrySet) {
                        if(entry.getKey().equals("type")) {
                            String input = entry.getValue();
                            switch(input) {
                                case "Empty", "tbd" ->  setTile(x, y, new NormalTile());
                                case "EnergySpace" ->  setTile(x, y, new EnergySpaceTile());
                                case "ConveyorBelt" -> setTile(x, y, new CheckpointTile());
                                case "Wall" -> {
                                    int index = 0;
                                    entrySet.iterator().next();
                                    entrySet.iterator().next();
                                    String directionWall = entry.getValue();
                                    switch (directionWall) {
                                        case "left" -> directions[index] = Direction.WEST;
                                        case "right" -> directions[index] = Direction.EAST;
                                        case "top" -> directions[index] = Direction.NORTH;
                                        case "bottom" -> directions[index] = Direction.SOUTH;
                                    }
                                    setTile(x, y, new WallTile(directions));
                                }
                                case "Laser" -> {
                                    entrySet.iterator().next();
                                    entrySet.iterator().next();
                                    String directionLaser = entry.getValue();
                                    switch (directionLaser) {
                                        case "left" -> setTile(x, y, new LaserTile(Direction.WEST));
                                        case "right" -> setTile(x, y, new LaserTile(Direction.EAST));
                                        case "top" -> setTile(x, y, new LaserTile(Direction.NORTH));
                                        case "bottom" -> setTile(x, y, new LaserTile(Direction.SOUTH));
                                    }
                                }
                                case "RestartPoint" -> setTile(x, y, new RebootTile());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
