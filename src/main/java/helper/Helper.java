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

    /**
     * Returns the position of the current objective.
     *
     * @param currentObjective The current objective of the player
     * @param board The board on wich is played
     * @return A pair of integers representing the x and y coordinates of the current objective
     */
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

    /**
     * Checks if a robot stands on a tile.
     *
     * @param tile The selected tile.
     * @param robot The selected robot.
     * @return Either true of false depending on the robot standing on the tile or not.
     */
    public static boolean isRobotOnTile(Robot robot, Tile tile){
        return robot.getCurrentPosition().getValue0() == tile.getXCoordinate() &&
                robot.getCurrentPosition().getValue1() == tile.getYCoordinate();
    }
}
