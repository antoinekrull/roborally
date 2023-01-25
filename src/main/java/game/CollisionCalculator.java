package game;

import com.fasterxml.jackson.core.JsonProcessingException;
import game.board.Board;
import game.board.PushPanelTile;
import game.board.Direction;
import game.board.Tile;
import game.player.Player;
import org.javatuples.Pair;

import java.util.ArrayList;

public class CollisionCalculator {

    static Board board = new Board();
    public static boolean checkRobotCollision(Player player){
        boolean result = false;
        Pair<Integer, Integer> nextPosition = player.getRobot().getCurrentPosition();
        switch(player.getRobot().getDirection()){
            case NORTH -> {
                nextPosition.setAt1(nextPosition.getValue1() - 1);
            }
            case SOUTH -> {
                nextPosition.setAt1(nextPosition.getValue1() + 1);
            }
            case EAST -> {
                nextPosition.setAt0(nextPosition.getValue0() + 1);
            }
            case WEST -> {
                nextPosition.setAt0(nextPosition.getValue0() - 1);
            }
        }
        if(tileIsBlocking(board.getTile(nextPosition))) {
            result = true;
        }
        return result;
    }

    public static boolean checkReverseRobotCollision(Player player){
        boolean result = false;
        Pair<Integer, Integer> nextPosition = player.getRobot().getCurrentPosition();
        switch(player.getRobot().getDirection()){
            case NORTH -> {
                nextPosition.setAt1(nextPosition.getValue1() + 1);
            }
            case SOUTH -> {
                nextPosition.setAt1(nextPosition.getValue1() - 1);
            }
            case EAST -> {
                nextPosition.setAt0(nextPosition.getValue0() - 1);
            }
            case WEST -> {
                nextPosition.setAt0(nextPosition.getValue0() + 1);
            }
        }
        if(tileIsBlocking(board.getTile(nextPosition))) {
            result = true;
        }
        return result;
    }

    public static boolean checkLaserCollision(Player player){
        Pair<Integer, Integer> playerPosition = player.getRobot().getCurrentPosition();

        for(int i = 0; i < board.getLaserTileList().size(); i++){
            Pair<Integer, Integer> laserEndpoint = new Pair<>(board.getLaserTileList().get(i).getXCoordinate(),
                    board.getLaserTileList().get(i).getYCoordinate());
            Direction laserDirection = board.getLaserTileList().get(i).getLos();
            switch(laserDirection){
                case NORTH-> {
                    for(int j = board.getLaserTileList().get(i).getYCoordinate(); j >= 0; j--){
                        laserEndpoint.setAt1(j);
                        if(tileIsBlocking(board.getTile(laserEndpoint))){
                            break;
                        }
                         return (playerPosition.getValue0().equals(board.getLaserTileList().get(i).getXCoordinate())
                                && player.getRobot().getCurrentPosition().getValue1() >= laserEndpoint.getValue1()
                                && player.getRobot().getCurrentPosition().getValue1() <= board.getLaserTileList().get(i).getYCoordinate());
                    }
                }
                case SOUTH -> {
                    for(int j = board.getLaserTileList().get(i).getYCoordinate(); j <= board.getRows(); j++){
                    laserEndpoint.setAt1(j);
                    if(tileIsBlocking(board.getTile(laserEndpoint))){
                        break;
                    }
                    return (playerPosition.getValue0().equals(board.getLaserTileList().get(i).getXCoordinate())
                            && player.getRobot().getCurrentPosition().getValue1() <= laserEndpoint.getValue1()
                            && player.getRobot().getCurrentPosition().getValue1() >= board.getLaserTileList().get(i).getYCoordinate());
                    }
                }
                case EAST -> {
                    for(int j = board.getLaserTileList().get(i).getXCoordinate(); j <= board.getColumns(); j++){
                        laserEndpoint.setAt0(j);
                        if(tileIsBlocking(board.getTile(laserEndpoint))){
                            break;
                        }
                        return (playerPosition.getValue1().equals(board.getLaserTileList().get(i).getYCoordinate())
                                && player.getRobot().getCurrentPosition().getValue0() <= laserEndpoint.getValue0()
                                && player.getRobot().getCurrentPosition().getValue0() >= board.getLaserTileList().get(i).getXCoordinate());
                    }
                }
                case WEST -> {
                    for(int j = board.getLaserTileList().get(i).getXCoordinate(); j >= 0; j--){
                        laserEndpoint.setAt0(j);
                        if(tileIsBlocking(board.getTile(laserEndpoint))){
                            break;
                        }
                        return (playerPosition.getValue1().equals(board.getLaserTileList().get(i).getYCoordinate())
                                && player.getRobot().getCurrentPosition().getValue0() >= laserEndpoint.getValue0()
                                && player.getRobot().getCurrentPosition().getValue0() <= board.getLaserTileList().get(i).getXCoordinate());
                    }
                }
            }
        }
        return false;
    }

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
        if(tileIsBlocking(board.getTile(nextPosition))) {
            result = true;
        }
        return result;
    }

    private static boolean tileIsBlocking(ArrayList<Tile> tileList) {
        boolean result = false;
        if(tileList.size() == 1) {
            result = tileList.get(0).isBlocking();
        } else {
            result = tileList.get(0).isBlocking() || tileList.get(1).isBlocking();
        }
        return result;
    }

    public static void setBoard(Board newBoard) {
        board = newBoard;
    }

    public static void createBoard(String jsonMap) throws JsonProcessingException {
        board.createBoard(jsonMap);
    }
}
