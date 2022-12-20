package game.board;

import game.player.Player;

public class PushPanelTile extends Tile {

    Direction pushDirection;

    public PushPanelTile(int xCoordinate, int yCoordinate, Direction pushDirection) {
        super(xCoordinate, yCoordinate);
        isBlocking = true;
        isDanger = false;
        this.pushDirection = pushDirection;
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        //TODO: Implement this once we have working register logic
    }
}
