package game;

import game.board.Board;
import game.board.Direction;
import game.player.Player;
import org.javatuples.Pair;

import java.util.ArrayList;

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
    public static boolean checkLaserCollision(Player player){
        Pair<Integer, Integer> playerPosition = player.getRobot().getCurrentPosition();

        for(int i = 0; i < Board.laserTileList.size(); i++){
            Pair<Integer, Integer> laserEndpoint = new Pair<>(Board.laserTileList.get(i).getXCoordinate(),
                    Board.laserTileList.get(i).getYCoordinate());
            Direction laserDirection = Board.laserTileList.get(i).getLos();
            switch(laserDirection){
                case NORTH-> {
                    for(int j = Board.laserTileList.get(i).getYCoordinate(); j >= 0; j--){
                        laserEndpoint.setAt1(j);
                        if(Board.getTile(laserEndpoint).isBlocking()){
                            break;
                        }
                         return (playerPosition.getValue0().equals(Board.laserTileList.get(i).getXCoordinate())
                                && player.getRobot().getCurrentPosition().getValue1() >= laserEndpoint.getValue1()
                                && player.getRobot().getCurrentPosition().getValue1() <= Board.laserTileList.get(i).getYCoordinate());
                    }
                }
                case SOUTH -> {
                    for(int j = Board.laserTileList.get(i).getYCoordinate(); j <= Board.getRows(); j++){
                    laserEndpoint.setAt1(j);
                    if(Board.getTile(laserEndpoint).isBlocking()){
                        break;
                    }
                    return (playerPosition.getValue0().equals(Board.laserTileList.get(i).getXCoordinate())
                            && player.getRobot().getCurrentPosition().getValue1() <= laserEndpoint.getValue1()
                            && player.getRobot().getCurrentPosition().getValue1() >= Board.laserTileList.get(i).getYCoordinate());
                    }
                }
                case EAST -> {
                    for(int j = Board.laserTileList.get(i).getXCoordinate(); j <= Board.getColumns(); j++){
                        laserEndpoint.setAt0(j);
                        if(Board.getTile(laserEndpoint).isBlocking()){
                            break;
                        }
                        return (playerPosition.getValue1().equals(Board.laserTileList.get(i).getYCoordinate())
                                && player.getRobot().getCurrentPosition().getValue0() <= laserEndpoint.getValue0()
                                && player.getRobot().getCurrentPosition().getValue0() >= Board.laserTileList.get(i).getXCoordinate());
                    }
                }
                case WEST -> {
                    for(int j = Board.laserTileList.get(i).getXCoordinate(); j >= 0; j--){
                        laserEndpoint.setAt0(j);
                        if(Board.getTile(laserEndpoint).isBlocking()){
                            break;
                        }
                        return (playerPosition.getValue1().equals(Board.laserTileList.get(i).getYCoordinate())
                                && player.getRobot().getCurrentPosition().getValue0() >= laserEndpoint.getValue0()
                                && player.getRobot().getCurrentPosition().getValue0() <= Board.laserTileList.get(i).getXCoordinate());
                    }
                }
            }
        }
        return false;
    }
    public static boolean checkPushPanelCollision(Player player){return false;}
}
