package game.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import communication.JsonSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static java.lang.Integer.parseInt;

/**
 * @author Antoine, Moritz
 * @version 1.0
 */
public class GameBoard extends Board{

    //TODO: Implement PushPanels
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
