package game;

import game.board.*;
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


        if(!checkWallCollision(currentPosition,target)){
            canMove=true;
            if(robot2!=null){
                canMove=false;
                Pair<Integer, Integer> pushedPos = new Pair<>(robot2.getCurrentPosition().getValue0()+xMove, robot2.getCurrentPosition().getValue1()+yMove);
                if(!checkWallCollision(target, pushedPos)) {
                    if(moveRobot(robot2, pushedPos));
                        canMove=true;
                    }
                }
            }
        if (canMove&&checkFallFromMap(target)){
            game.reboot(playerList.getPlayerFromList(robot1));
        } else if (canMove){
            harvestEnergyCubes(target, robot1);
            robot1.setCurrentPosition(target);
        }
        return canMove;
    }

    private boolean checkWallCollision(Pair<Integer,Integer> currentPosition, Pair<Integer,Integer> target){
        boolean result = false;
        boolean onWall = false;
        boolean toWall = false;
        ArrayList<Direction> onBlocked = new ArrayList<>();
        ArrayList<Direction> toBlocked = new ArrayList<>();
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
        if(!checkFallFromMap(target)) {
            ArrayList<Tile> targetTile = board.getTile(target);
            for (Tile tile : targetTile) {
                if (tile.getType().equals("ConveyorBelt")) {
                    conveyorBelt = true;
                    outTarget = tile.getDirectionOut();
                }
            }


            if (conveyorBelt) {
                if (outTarget != outCurrent) {
                    if (outTarget == Direction.NORTH) {
                        if (outCurrent == Direction.WEST) {
                            robot.rotateRobot(Direction.RIGHT);
                        } else {
                            robot.rotateRobot(Direction.LEFT);
                        }
                    } else if (outTarget == Direction.EAST) {
                        if (outCurrent == Direction.NORTH) {
                            robot.rotateRobot(Direction.RIGHT);
                        } else {
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
            }
        }else if (checkFallFromMap(target)){
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
        if(target.getValue0()<0 || target.getValue0() >= boardSize.getValue0()){
            return true;
        }else if (target.getValue1()<0 || target.getValue1() >= boardSize.getValue1()) {
            return true;
            //could also be try catch block
        } else if (board.getTile(target).get(0).getType().equals("Pit")) {
            return true;
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
    public void shootMapLasers() {
        boolean shooting = true;
        Pair<Integer, Integer> shot;
        ArrayList<LaserTile> lasers = board.getLaserTileList();
        for (int i = 0; i < lasers.size(); i++) {
            LaserTile laser = lasers.get(i);
            Pair<Integer,Integer> pos = laser.getPosition();
            Direction direction = laser.getLos();
            switch (direction) {
                case NORTH -> shot = new Pair<>(0, -1);
                case EAST -> shot = new Pair<>(1, 0);
                case SOUTH -> shot = new Pair<>(0, 1);
                case WEST -> shot = new Pair<>(-1, 0);
                default -> shot = new Pair<>(0, 0);
            }
            while (shooting){
                Pair<Integer, Integer> nextPos = new Pair<>(pos.getValue0()+shot.getValue0(), pos.getValue1()+shot.getValue1());
                Robot robot = checkForRobot(pos);
                if(robot != null){
                    robot.increaseDamageCount();
                    game.drawDamageCards(playerList.getPlayerFromList(robot));
                    shooting = false;
                } else if (checkWallCollision(pos, nextPos)) {
                    shooting = false;
                }
                pos = nextPos;
            }
        }
    }
    public void shootRobotLasers(){
        boolean shooting = true;
        Pair<Integer, Integer> shot;
        ArrayList<Robot> robots = playerList.getAllRobots();
        for (int i = 0; i < robots.size(); i++) {
            Robot robot = robots.get(i);
            Pair<Integer,Integer> pos = robot.getCurrentPosition();
            Direction direction = robot.getDirection();
            switch (direction) {
                case NORTH -> shot = new Pair<>(0, -1);
                case EAST -> shot = new Pair<>(1, 0);
                case SOUTH -> shot = new Pair<>(0, 1);
                case WEST -> shot = new Pair<>(-1, 0);
                default -> shot = new Pair<>(0, 0);
            }
            while (shooting) {
                Pair<Integer, Integer> nextPos = new Pair<>(pos.getValue0() + shot.getValue0(), pos.getValue1() + shot.getValue1());
                if (board.isPositionOnBoard(nextPos)) {
                    Robot robot2 = checkForRobot(nextPos);
                    if (robot2 != null) {
                        robot2.increaseDamageCount();
                        game.drawDamageCards(playerList.getPlayerFromList(robot2));
                        shooting = false;
                    } else if (checkWallCollision(pos, nextPos)) {
                        shooting = false;
                    }
                    pos = nextPos;
                } else {
                    shooting = false;
                }
            }
        }

    }

    private void harvestEnergyCubes(Pair<Integer, Integer> target, Robot robot) {
        for(Tile tile: board.getTile(target)) {
            if(tile instanceof EnergySpaceTile) {
                if(tile.hasEnergyCube()) {
                    robot.increaseEnergyCubes("EnergySpace");
                    tile.setEnergyCube(false);
                }
            }
        }
    }

    public void setBoard(Board newBoard) {
        board = newBoard;
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
