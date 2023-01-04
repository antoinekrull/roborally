package game.board;

import game.player.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.javatuples.Pair;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public abstract class Tile {
    String path;
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean isDanger;
    protected boolean isBlocking;
    private Pair<Integer, Integer> position;

    public Tile(int xCoordinate, int yCoordinate, String path) {
        position = new Pair<>(xCoordinate, yCoordinate);
    }

    public boolean isDanger() {
        return isDanger;
    }
    public void setDanger(boolean danger) {
        isDanger = danger;
    }
    public boolean isBlocking() {
        return isBlocking;
    }
    public void setBlocking(boolean blocking) {
        isBlocking = blocking;
    }
    public Pair<Integer, Integer> getPosition() {
        return position;
    }
    public void setPosition(Pair<Integer, Integer> position) {
        this.position = position;
    }
    public int getXCoordinate() {
        return position.getValue0();
    }
    public int getYCoordinate(){
        return position.getValue1();
    }
    public void applyEffect(Player player) throws Exception{}

    public void makeImage(GridPane tiles){
        ImageView img = new ImageView();
        Image im = new Image(path,(double) width,(double) height,false,false);
        img.setImage(im);
        tiles.add(img,x,y);
    }
}
