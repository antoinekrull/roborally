package helper;

import game.board.Board;
import game.board.CheckpointTile;
import game.board.Tile;
import game.player.Robot;
import org.javatuples.Pair;

public class Helper {

    /**
     * Private constructor to prevent anyone of making an instance of the helper class.
     */
    private Helper(){}
    public static Pair<Integer, Integer> getPositionOfCurrentObjective(int currentObjective, Board board){
        for(int x = 0; x < board.getBoard().size(); x++){
            for(int y = 0; y < board.getBoard().get(x).size(); y++) {
                if (board.getBoard().get(x).get(y).get(0) instanceof CheckpointTile ct){
                    if(ct.getNumber() == currentObjective){
                        return new Pair<>(ct.getXCoordinate(), ct.getYCoordinate());
                    }
                }
            }
        }
        return null;
    }

    public static boolean isRobotOnTile(Robot robot, Tile tile){
        return robot.getCurrentPosition().getValue0() == tile.getXCoordinate() &&
                robot.getCurrentPosition().getValue1() == tile.getYCoordinate();
    }
}
