package game;

import com.fasterxml.jackson.core.JsonProcessingException;
import game.board.*;
import game.player.Player;
import javafx.geometry.Orientation;
import org.javatuples.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CollisionCalculator {
    private Board board;
    public CollisionCalculator(Board board) {
        this.board = board;

    }

    public boolean checkRobotCollision(Player player, Pair<Integer,Integer> target){
        boolean result = false;
        boolean onWall = false;
        boolean toWall = false;
        ArrayList<Direction> onBlocked = new ArrayList<>();
        ArrayList<Direction> toBlocked = new ArrayList<>();
        Pair<Integer, Integer> currentPosition = player.getRobot().getCurrentPosition();
        ArrayList<Tile> currentTile = board.getTile(currentPosition);
        ArrayList<Tile> targetTile = board.getTile(target);
        System.out.println(target.getValue0() + " / "+ currentPosition.getValue0());

        //checks if robot is currently on a tile, if so onWall is true
        for(int i = 0; i < currentTile.size();i++){
            if(currentTile.get(i).getType().equals("Wall")){
                onWall=true;
                System.out.println("on wall");
                onBlocked = getBlockingDirections(currentTile);
            }
        }
        for(int i=0; i<targetTile.size();i++){
            if(targetTile.get(i).getType().equals("Wall")){
                toWall=true;
                System.out.println("to wall");
                toBlocked = getBlockingDirections(targetTile);
            }
        }

        if(targetTile.get(0).getType().equals("Antenna")){
           result = true;
        }

//        for (int i = 0; i < currentTile.size(); i++){
//            if(currentTile.get(i).getType().equals("Wall")){
//                onWall=true;
//                blockedDirections= currentTile.get(i).getOrientations();
//            }
//        }

        //if onWall true it checks which direction is blocked
        if(onWall){
            if(target.getValue0()<currentPosition.getValue0()){
                if(onBlocked.contains(Direction.WEST)){
                    result = true;
                }
            }else if(target.getValue0()>currentPosition.getValue0()){
                if(onBlocked.contains(Direction.EAST)){
                    result = true;
                }
            }else if(target.getValue1()<currentPosition.getValue1()){
                if(onBlocked.contains(Direction.NORTH)){
                    result = true;
                }
            }else if(target.getValue1()>currentPosition.getValue1()){
                if(onBlocked.contains(Direction.SOUTH)){
                    result = true;
                }
            }
        }
        if(toWall){
            if (target.getValue0()<currentPosition.getValue0()){
                if(toBlocked.contains(Direction.EAST)){
                    result = true;
                }
            }else if(target.getValue0()>currentPosition.getValue0()){
                if(toBlocked.contains(Direction.WEST)){
                    result = true;
                }
            }else if(target.getValue1()<currentPosition.getValue1()){
                if(toBlocked.contains(Direction.SOUTH)){
                    result = true;
                }
            }else if(target.getValue1()>currentPosition.getValue1()){
                if(toBlocked.contains(Direction.NORTH)){
                    result = true;
                }
            }
        }
        System.out.println(result);
        return result;
    }

    public boolean checkReverseRobotCollision(Player player){
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

    public boolean checkLaserCollision(Player player){
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
    public boolean checkPushPanelCollision(PushPanelTile pushPanel){
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

    private boolean tileIsBlocking(ArrayList<Tile> tileList) {
        boolean result = false;
        if(tileList.size() == 1) {
            result = tileList.get(0).isBlocking();
        } else {
            result = tileList.get(0).isBlocking() || tileList.get(1).isBlocking();
        }
        return result;
    }

    public void setBoard(Board newBoard) {
        board = newBoard;
    }

    public void createBoard(String jsonMap) throws JsonProcessingException {
        board.createBoard(jsonMap);
    }

    private ArrayList<Direction> getBlockingDirections(ArrayList<Tile> tiles){
        for (int i = 0; i < tiles.size();i++){
            if(tiles.get(i).getType().equals("Wall")){
                return tiles.get(i).getBlockedDirections();
            }
        }
        return null;
    }
}
