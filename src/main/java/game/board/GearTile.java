package game.board;

import game.player.Player;
import javafx.scene.layout.GridPane;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class GearTile extends Tile {
    private Direction rotation;
    public GearTile(int xCoordinate, int yCoordinate, Direction rotation) {
        super(xCoordinate, yCoordinate, "/textures/gameboard/gearsNachLinksDrehen.png");
        this.rotation = rotation;
        if (rotation == this.rotation.LEFT) {
            this.path = getClass().getResource("/textures/gameboard/gearsNachLinksDrehen.png").toString();
        }
        else {
            this.path = getClass().getResource("/textures/gameboard/gearsNachRechtsDrehen.png").toString();
        }
        setType("Gear");
        isDanger = false;
        isBlocking = false;
    }
    public void setRotationType(Direction rotation) {
        this.rotation = rotation;
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        if(rotation == Direction.RIGHT.RIGHT){
            switch (player.getRobot().getDirection()){
                case NORTH -> player.getRobot().setDirection(Direction.EAST);
                case EAST -> player.getRobot().setDirection(Direction.SOUTH);
                case SOUTH -> player.getRobot().setDirection(Direction.WEST);
                case WEST -> player.getRobot().setDirection(Direction.NORTH);
                default -> throw new Exception("Invalid direction");
            }
        } else if(rotation == Direction.LEFT){
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
