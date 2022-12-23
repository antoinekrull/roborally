package game;

import game.board.Board;
import game.board.PushPanelTile;
import game.player.Player;
import org.javatuples.Pair;

public class CollisionCalculator {

    static Board board;
    public static boolean checkRobotCollision(Player player){
        boolean result = false;
        Pair<Integer, Integer> nextPosition = player.getRobot().getCurrentPosition();
        switch(player.getRobot().getDirection()){
            case NORTH -> {
                nextPosition.setAt0(nextPosition.getValue0() + 1);
            }
            case SOUTH -> {
                nextPosition.setAt0(nextPosition.getValue0() - 1);
            }
            case EAST -> {
                nextPosition.setAt1(nextPosition.getValue1() + 1);
            }
            case WEST -> {
                nextPosition.setAt1(nextPosition.getValue1() - 1);
            }
        }
        if(board.getTile(nextPosition).isBlocking()) {
            result = true;
        }
        return result;
    }
    public static boolean checkLaserCollision(Player player){return false;}

    //might be unnecessary
    public static boolean checkPushPanelCollision(PushPanelTile pushPanel){
        boolean result = false;
        Pair<Integer, Integer> nextPosition = pushPanel.getPosition();
        switch(pushPanel.getPushDirection()){
            case NORTH -> {
                nextPosition.setAt0(nextPosition.getValue0() + 1);
            }
            case SOUTH -> {
                nextPosition.setAt0(nextPosition.getValue0() - 1);
            }
            case EAST -> {
                nextPosition.setAt1(nextPosition.getValue1() + 1);
            }
            case WEST -> {
                nextPosition.setAt1(nextPosition.getValue1() - 1);
            }
        }
        if(board.getTile(nextPosition).isBlocking()) {
            result = true;
        }
        return result;
    }
}
