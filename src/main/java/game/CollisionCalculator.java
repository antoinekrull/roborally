package game;

import com.fasterxml.jackson.core.JsonProcessingException;
import game.board.*;
import game.player.Player;
import game.player.Robot;
import org.javatuples.Pair;
import server.connection.PlayerList;

import java.util.ArrayList;

public class CollisionCalculator {
    private Board board;
    private PlayerList playerList;
    private Game game;
    public CollisionCalculator(Board board, PlayerList playerList, Game game) {
        this.board = board;
        this.playerList = playerList;
        this.game = game;
    }

    public boolean moveRobot(Robot robot1, Pair<Integer, Integer> target) {
        boolean canMove = false;
        Pair<Integer, Integer> currentPosition = robot1.getCurrentPosition();
        Pair<Integer, Integer> movement = new Pair<>(target.getValue0()-currentPosition.getValue0(),target.getValue1()-currentPosition.getValue1());
        int xMove = movement.getValue0();
        int yMove = movement.getValue1();
        Robot robot2 = checkForRobot(target);


        if(!checkWallCollision(robot1,target)){
            canMove=true;
            if(robot2!=null){
                canMove=false;
                Pair<Integer, Integer> pushedPos = new Pair<>(robot2.getCurrentPosition().getValue0()+xMove, robot2.getCurrentPosition().getValue1()+yMove);
                if(!checkWallCollision(robot2, pushedPos)) {
                    if(moveRobot(robot2, pushedPos));
                        canMove=true;
                    }
                }
            }

        if (canMove&&checkFallFromMap(target)){
            game.reboot(playerList.getPlayerFromList(robot1));
        }else if (canMove){
            robot1.setCurrentPosition(target);
        }

        return canMove;
    }

    private boolean checkWallCollision(Robot robot, Pair<Integer,Integer> target){
        boolean result = false;
        boolean onWall = false;
        boolean toWall = false;
        ArrayList<Direction> onBlocked = new ArrayList<>();
        ArrayList<Direction> toBlocked = new ArrayList<>();
        Pair<Integer, Integer> currentPosition = robot.getCurrentPosition();
        ArrayList<Tile> currentTile = board.getTile(currentPosition);
        ArrayList<Tile> targetTile = board.getTile(target);
        if(targetTile != null) {

            //checks if robot is currently on a tile, if so onWall is true
            for (int i = 0; i < currentTile.size(); i++) {
                if (currentTile.get(i).getType().equals("Wall")) {
                    onWall = true;
                    onBlocked = getBlockingDirections(currentTile);
                }
            }
            for (int i = 0; i < targetTile.size(); i++) {
                if (targetTile.get(i).getType().equals("Wall")) {
                    toWall = true;
                    toBlocked = getBlockingDirections(targetTile);
                }
            }

            if (targetTile.get(0).getType().equals("Antenna")) {
                result = true;
            }

            //if onWall true it checks which direction is blocked
            if (onWall) {
                if (target.getValue0() < currentPosition.getValue0()) {
                    if (onBlocked.contains(Direction.WEST)) {
                        result = true;
                    }
                } else if (target.getValue0() > currentPosition.getValue0()) {
                    if (onBlocked.contains(Direction.EAST)) {
                        result = true;
                    }
                } else if (target.getValue1() < currentPosition.getValue1()) {
                    if (onBlocked.contains(Direction.NORTH)) {
                        result = true;
                    }
                } else if (target.getValue1() > currentPosition.getValue1()) {
                    if (onBlocked.contains(Direction.SOUTH)) {
                        result = true;
                    }
                }
            }
            if (toWall) {
                if (target.getValue0() < currentPosition.getValue0()) {
                    if (toBlocked.contains(Direction.EAST)) {
                        result = true;
                    }
                } else if (target.getValue0() > currentPosition.getValue0()) {
                    if (toBlocked.contains(Direction.WEST)) {
                        result = true;
                    }
                } else if (target.getValue1() < currentPosition.getValue1()) {
                    if (toBlocked.contains(Direction.SOUTH)) {
                        result = true;
                    }
                } else if (target.getValue1() > currentPosition.getValue1()) {
                    if (toBlocked.contains(Direction.NORTH)) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }
    public boolean moveConveyorBelt(Robot robot){
        boolean result = false;
        boolean conveyorBelt = false;
        boolean pit = false;
        int xMove = 0;
        int yMove = 0;
        Direction outCurrent = null;
        Direction outTarget = null;

        Pair<Integer, Integer> currentPosition = robot.getCurrentPosition();
        ArrayList<Tile> currentTile = board.getTile(currentPosition);

        for(int i = 0; i < currentTile.size(); i++){
            if (currentTile.get(i).getType().equals("ConveyorBelt")){
                outCurrent=currentTile.get(i).getDirectionOut();
                switch (outCurrent) {
                    case NORTH -> {
                        xMove = 0;
                        yMove = -1;
                    }
                    case EAST -> {
                        xMove = 1;
                        yMove = 0;
                    }
                    case SOUTH -> {
                        xMove = 0;
                        yMove = 1;
                    }
                    case WEST -> {
                        xMove = -1;
                        yMove = 0;
                    }
                }
            }
        }

        Pair<Integer, Integer> target = new Pair<>(currentPosition.getValue0()+xMove,currentPosition.getValue1()+yMove);
        ArrayList<Tile> targetTile = board.getTile(target);
        for (Tile tile : targetTile) {
            if (tile.getType().equals("ConveyorBelt")) {
                conveyorBelt = true;
                outTarget = tile.getDirectionOut();
            } else if (tile.getType().equals("Pit")) {
                pit = true;
            }
        }


        if(conveyorBelt){
            if(outTarget!=outCurrent){
                if(outTarget == Direction.NORTH){
                    if (outCurrent == Direction.WEST){
                        robot.rotateRobot(Direction.RIGHT);
                    }else{
                        robot.rotateRobot(Direction.LEFT);
                    }
                } else if (outTarget == Direction.EAST) {
                    if (outCurrent == Direction.NORTH) {
                        robot.rotateRobot(Direction.RIGHT);
                    }else{
                        robot.rotateRobot(Direction.LEFT);
                    }
                } else if (outTarget == Direction.SOUTH) {
                    if (outCurrent == Direction.EAST) {
                        robot.rotateRobot(Direction.RIGHT);
                    } else {
                        robot.rotateRobot(Direction.LEFT);
                    }
                } else {
                    if (outCurrent == Direction.NORTH) {
                        robot.rotateRobot(Direction.RIGHT);
                    } else {
                        robot.rotateRobot(Direction.LEFT);
                    }
                }
            }
            moveRobot(robot, target);
            result = true;
        } else if (checkFallFromMap(target)){
            game.reboot(playerList.getPlayerFromList(robot));
        }
        else{
            if(checkForRobot(target) == null){
                result = true;
                moveRobot(robot, target);
            }
        }
        return result;
    }
    private boolean checkFallFromMap(Pair<Integer, Integer> target){
        boolean result = false;
        Pair<Integer,Integer> boardSize = board.getDimension();
        System.out.println("target = " + target);
        System.out.println("boardsize = " + boardSize);
        if(target.getValue0()<0 || target.getValue0() > boardSize.getValue0()){
            result = true;
        }else if (target.getValue1()<0 || target.getValue1() > boardSize.getValue1()){
            result = true;
            //could also be try catch block
        } else if(board.getTile(target) == null) {
            result = true;
        } else if (board.getTile(target).get(0).getType().equals("Pit")) {
            result = true;
        }
        return result;
    }
    private Robot checkForRobot(Pair<Integer, Integer> target){
        ArrayList<Robot> robots = playerList.getAllRobots();
        for (Robot robot: robots){
            if(robot.getCurrentPosition().equals(target)){
                return robot;
            }
        }
        return null;
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
