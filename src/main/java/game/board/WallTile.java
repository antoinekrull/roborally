package game.board;

import game.player.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class WallTile extends Tile {

    private ArrayList<Direction> blockedDirections;
    public WallTile(int xCoordinate, int yCoordinate, ArrayList<Direction> blockedDirections) {
        super(xCoordinate, yCoordinate, "/textures/gameboard/wall.png");
        this.path = getClass().getResource("/textures/gameboard/wall.png").toString();
        isDanger = false;
        isBlocking = true;
        this.blockedDirections = blockedDirections;
    }

    public ArrayList<Direction> getBlockedDirections() {
        return blockedDirections;
    }
    public void setBlockedDirections(ArrayList<Direction> blockedDirections) {
        this.blockedDirections = blockedDirections;
    }
    @Override
    public void applyEffect(Player player) throws Exception {
        //has no effect, the robot movement is what detects collisions
    }
    @Override
    public void makeImage(GridPane tiles){
        ImageView img = new ImageView();
        Image im = new Image(path,(double) height, 70,true,false);
        img.setImage(im);
        for (int i = 0; i < this.blockedDirections.size(); i++) {
            Direction dir = this.blockedDirections.get(i);
            int rot = 0;
            switch (dir) {
                case NORTH -> rot = 180;
                case EAST -> rot = 270;
                case SOUTH -> rot = 0;
                case WEST -> rot = 90;
            }
            img.setRotate(rot);
            tiles.add(img,this.xCoordinate,this.yCoordinate);
        }

    }
}
