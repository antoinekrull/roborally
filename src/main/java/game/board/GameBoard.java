package game.board;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Antoine, Moritz
 * @version 1.0
 */
public class GameBoard extends Board{

    public void createBoard(Object JsonMap) {
        int i;
        ArrayList<String> list = new ArrayList<String>();
        try {
            // create object mapper instance
            ObjectMapper objectMapper = new ObjectMapper();
            for(int x = 0; x <= 10; x++){
                for(int y = 0; y <= 10; y++){
                    objectMapper.readValue((String) JsonMap, Tile.class);

                    //board[x][y] = JsonMap.getValue();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
