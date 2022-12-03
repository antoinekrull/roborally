package game.board;

import com.fasterxml.jackson.databind.util.JSONPObject;

import java.io.BufferedReader;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Iterator;

/**
 * @author Antoine
 * @version 1.0
 */
public class GameBoard extends Board{

    public void createBoard(JSONPObject JsonMap) {

        for(int x = 0; x <= 10; x++){
            for(int y = 0; y <= 10; y++){

                //board[x][y] = JsonMap.getValue();
            }
        }
    }

}
