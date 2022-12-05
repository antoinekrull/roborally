package game.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Antoine, Moritz
 * @version 1.0
 */
public class GameBoard extends Board{
    ObjectMapper objectMapper = new ObjectMapper();

    public HashMap convertJson(Object JsonMap) throws JsonProcessingException {
        HashMap<String, String> convertedMap = new HashMap<String, String>();
        convertedMap = objectMapper.readValue((String)JsonMap, HashMap.class);
        return convertedMap;
    }

    public void createBoard(HashMap<String, String> jsonMap) {
        var entrySet = jsonMap.entrySet();
        try {
            for(int x = 0; x <= 10; x++){
                for(int y = 0; y <= 10; y++){
                    for(var entry: entrySet) {
                        if(entry.getKey().equals("type"));
                            //TODO: Implement constructor for TileType using String as input

                            //    setTile(x, y, Tile(entry.getValue()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
