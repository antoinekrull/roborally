package game.board;

import game.robot.Robot;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class GearTile extends Tile{
    private RotationType rotationType;
    public GearTile(int xCoordinate, int yCoordinate) {
        super(xCoordinate, yCoordinate);
        isDanger = false;
        isBlocking = false;
    }
    public void setRotationType(RotationType rotationType) {
        this.rotationType = rotationType;
    }

    @Override
    public void applyEffect(Robot robot) throws Exception {
        if(rotationType == RotationType.RIGHT){
            switch (robot.getDirection()){
                case NORTH -> robot.setDirection(Direction.EAST);
                case EAST -> robot.setDirection(Direction.SOUTH);
                case SOUTH -> robot.setDirection(Direction.WEST);
                case WEST -> robot.setDirection(Direction.NORTH);
                default -> throw new Exception("Invalid direction");
            }
        } else if(rotationType == RotationType.LEFT){
            switch (robot.getDirection()){
                case NORTH -> robot.setDirection(Direction.WEST);
                case WEST -> robot.setDirection(Direction.SOUTH);
                case SOUTH -> robot.setDirection(Direction.EAST);
                case EAST -> robot.setDirection(Direction.NORTH);
                default -> throw new Exception("Invalid direction");
            }
        } else {
            throw new Exception("Invalid rotation type");
        }
    }
}
