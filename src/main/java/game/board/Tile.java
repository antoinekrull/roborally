package game.board;

import client.viewmodel.ViewModelGameWindow;
import game.player.Player;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.javatuples.Pair;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class Tile {

    private ArrayList<String> orientations;

    private String type;
    private String isOnBoard;
    private int speed;
    private int count;
    private ArrayList<Integer> registers;

    public void setOrientations(ArrayList<String> orientations) {
        this.orientations = orientations;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIsOnBoard(String isOnBoard) {
        this.isOnBoard = isOnBoard;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }
    public String getIsOnBoard() {
        return isOnBoard;
    }
    public ArrayList<String> getOrientations() {
        return orientations;
    }
    public int getSpeed() {
        return speed;
    }
    public int getCount() {
        return count;
    }
    public ArrayList<Integer> getRegisters() {
        return registers;
    }
    public void setRegisters(ArrayList<Integer> registers) {
        this.registers = registers;
    }

    String path;
    String imageFXid;
    protected int xCoordinate;
    protected int yCoordinate;
    protected int height;
    protected boolean isDanger;
    protected boolean isBlocking;
    private Pair<Integer, Integer> position;


    public Tile(){}

    public Tile(int xCoordinate, int yCoordinate, String path, String imageFXid) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        //position = new Pair<>(xCoordinate, yCoordinate);
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
        img.setId(imageFXid);
        ViewModelGameWindow vm = new ViewModelGameWindow();
        double width = vm.getGameboardTileWidth();
        Image im = new Image(path,width,(double) height, true,false);
        img.setImage(im);
        tiles.add(img,this.xCoordinate,this.yCoordinate);
    }
}
