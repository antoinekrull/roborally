package game.board;

import game.player.Player;

/**
 * @author Firas
 * @version 1.0
 */
public class RebootTile extends Tile {
    private static int rebootTileIndex = 0;
    public Direction direction;

    //TODO: Direction needs to be added to constructor
    public RebootTile(int xCoordinate, int yCoordinate/*, Direction direction*/){
        super(xCoordinate, yCoordinate, "/textures/gameboard/reboot.png");
        this.path = getClass().getResource("/textures/gameboard/reboot.png").toString();
        //this.direction = direction;
        isDanger = false;
        isBlocking = false;
        setRebootTileIndex(rebootTileIndex++);
    }

    public void setRebootTileIndex(int rebootTileIndex) {
        this.rebootTileIndex = rebootTileIndex;
    }
    public static int getRebootTileIndex() {
        return rebootTileIndex;
    }

    public void setDirection(Direction newDirection) {
        direction = newDirection;
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        player.getRobot().setCurrentPosition(getPosition());
        player.getRobot().setDirection(this.direction);
        player.getRobot().setRebootStatus(true);
        //TODO:Implement this
        //robot.drawSpam(2);
        //robot.discardSpam(2);
    }

}
