package game.board;

import game.player.Player;
import game.robot.Robot;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class GearTile extends Tile{
    private RotationType rotationType;
    public GearTile(int xCoordinate, int yCoordinate) {
        super(xCoordinate, yCoordinate"/textures/gameboard/foerderbandGeradeAnimated.gif");
        this.path = getClass().getResource("/textures/gameboard/foerderbandGeradeAnimated.gif").toString();
        isDanger = false;
        isBlocking = false;
    }
    public void setRotationType(RotationType rotationType) {
        this.rotationType = rotationType;
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        if(rotationType == RotationType.RIGHT){
            switch (player.getRobot().getDirection()){
                case NORTH -> player.getRobot().setDirection(Direction.EAST);
                case EAST -> player.getRobot().setDirection(Direction.SOUTH);
                case SOUTH -> player.getRobot().setDirection(Direction.WEST);
                case WEST -> player.getRobot().setDirection(Direction.NORTH);
                default -> throw new Exception("Invalid direction");
            }
        } else if(rotationType == RotationType.LEFT){
            switch (player.getRobot().getDirection()){
                case NORTH -> player.getRobot().setDirection(Direction.WEST);
                case WEST -> player.getRobot().setDirection(Direction.SOUTH);
                case SOUTH -> player.getRobot().setDirection(Direction.EAST);
                case EAST -> player.getRobot().setDirection(Direction.NORTH);
                default -> throw new Exception("Invalid direction");
            }
        } else {
            throw new Exception("Invalid rotation type");
        }
    }
}
