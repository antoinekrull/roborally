package game.board;

import game.player.Player;

/**
 * @author Firas
 * @version 1.0
 */
public class RebootTile extends Tile {
    private static int rebootTileIndex = 0;
    public Direction direction;

    public RebootTile(int xCoordinate, int yCoordinate, Direction direction){
        super(xCoordinate, yCoordinate"/textures/gameboard/foerderbandGeradeAnimated.gif");
        this.path = getClass().getResource("/textures/gameboard/foerderbandGeradeAnimated.gif").toString();
        isDanger = false;
        isBlocking = false;
        this.direction = direction;
        setRebootTileIndex(rebootTileIndex++);
    }

    public void setRebootTileIndex(int rebootTileIndex) {
        this.rebootTileIndex = rebootTileIndex;
    }
    public static int getRebootTileIndex() {
        return rebootTileIndex;
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
