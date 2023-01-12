package game.board;

import game.player.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class LaserTile extends Tile {
    private Direction los;
    private Boolean onWall;

    public LaserTile(int xCoordinate, int yCoordinate, Direction LineOfSight, Boolean onWall) {
        super(xCoordinate, yCoordinate, "/textures/gameboard/laser.png");
        if (!onWall){
            this.path = getClass().getResource("/textures/gameboard/laserShot.png").toString();
        }
        else {
            this.path = getClass().getResource("/textures/gameboard/laser.png").toString();
        }
        this.onWall = onWall;
        isDanger = true;
        isBlocking = false;
        this.los = LineOfSight;
    }

    public Direction getLos() {
        return los;
    }
    public void setLos(Direction los) {this.los = los;}
    @Override
    public void applyEffect(Player player) throws Exception {
        player.addCard(game.Game.spamDeck.popCardFromDeck());
    }
    @Override
    public void makeImage(GridPane tiles) {
        ImageView img = new ImageView();
        Image im = new Image(path,(double) height, 70,true,false);
        img.setImage(im);
            int rot = 0;
            switch (los) {
                case NORTH -> rot = 0;
                case EAST -> rot = 90;
                case SOUTH -> rot = 180;
                case WEST -> rot = 270;
            }
            img.setRotate(rot);
            tiles.add(img,this.xCoordinate,this.yCoordinate);

    }
}
