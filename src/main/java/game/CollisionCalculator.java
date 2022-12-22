package game;

import game.player.Player;
import org.javatuples.Pair;

public class CollisionCalculator {
    public static boolean checkRobotCollision(Player player){
        Pair<Integer, Integer> nextPosition = player.getRobot().getCurrentPosition();
        /*switch(player.getRobot().getDirection()){
            case NORTH -> return ;
            case SOUTH -> newPosition.setAt0(newPosition.getValue0() - velocity);
            case EAST -> newPosition.setAt1(newPosition.getValue1() + velocity);
            case WEST -> newPosition.setAt1(newPosition.getValue1() - velocity);
        }*/
        return false;
    }
    public static boolean checkLaserCollision(Player player){return false;}
    public static boolean checkPushPanelCollision(Player player){return false;}
}
