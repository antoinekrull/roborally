package game.board;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.javatuples.Pair;
import game.player.Player;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class ConveyorBeltTile extends Tile {
    private int velocity;
    private ArrayList<Direction> directionIn;
    private Direction directionOut;
    private enum Variant {
        STRAIGHT, CURVE, TSECTION, CROSS
    }
    private Variant variant;


    public ConveyorBeltTile(int xCoordinate, int yCoordinate, int velocity, ArrayList<Direction> directionIn, Direction directionOut) {
        super(xCoordinate, yCoordinate, "/textures/gameboard/foerderbandGeradeAnimated.gif");
        isDanger = false;
        isBlocking = false;
        this.velocity = velocity;
        this.directionIn = directionIn;
        this.directionOut = directionOut;
        setVariant(this.directionIn, this.directionOut);


    }

    public int getVelocity() { return velocity; }
    public void setVelocity(int velocity) { this.velocity = velocity; }
    public ArrayList<Direction> getDirectionIn() { return directionIn; }
    public void setDirectionIn(ArrayList<Direction> directionIn) { this.directionIn = directionIn; }

    public Direction getDirectionOut() {
        return directionOut;
    }

    public void setDirectionOut(Direction directionOut) {
        this.directionOut = directionOut;
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        player.getRobot().setDirection(this.directionOut);
        Pair<Integer, Integer> newPosition = new Pair<>(player.getRobot().getCurrentPosition().getValue0(), player.getRobot().getCurrentPosition().getValue1());
        switch(this.directionOut){
            case NORTH -> newPosition.setAt1(newPosition.getValue1() + velocity);
            case SOUTH -> newPosition.setAt1(newPosition.getValue1() - velocity);
            case EAST -> newPosition.setAt0(newPosition.getValue0() + velocity);
            case WEST -> newPosition.setAt0(newPosition.getValue0() - velocity);
            default -> throw(new Exception("Invalid direction"));
        }
        player.getRobot().setCurrentPosition(newPosition);
    }
    @Override
    public void makeImage(GridPane tiles) {
        ImageView img = new ImageView();
        Image im = new Image(path, (double) height, 70, true, false);
        img.setImage(im);
        int rot = 0;
        switch (variant) {
            case STRAIGHT -> {
                switch (directionOut) {
                    case NORTH -> rot = 270;
                    case EAST -> rot = 0;
                    case SOUTH -> rot = 90;
                    case WEST -> rot = 180;
                }
            }
            case CURVE -> {
                switch (directionOut) {
                    case NORTH -> {
                        rot = 270;
                        if (directionIn.get(0) != Direction.EAST) {
                            img.setScaleX(-1);
                        }
                    }
                    case EAST -> {
                        rot = 0;
                        if (directionIn.get(0) != Direction.SOUTH) {
                            img.setScaleX(-1);
                        }
                    }
                    case SOUTH -> {
                        rot = 90;
                        if (directionIn.get(0) != Direction.WEST) {
                            img.setScaleX(-1);
                        }
                    }
                    case WEST -> {
                        rot = 180;
                        if (directionIn.get(0) != Direction.NORTH) {
                            img.setScaleX(-1);
                        }
                    }
                }

                img.setRotate(rot);
                tiles.add(img, this.xCoordinate, this.yCoordinate);

            }
        }
    }
    private void setVariant(ArrayList<Direction> in, Direction out){
        if (in.size() == 1){
            switch (in.get(0)) {
                case NORTH -> {
                    if(out == Direction.SOUTH){
                        this.variant = Variant.STRAIGHT;
                        this.path = getClass().getResource("/textures/gameboard/foerderbandGeradeAnimated.gif").toString();
                    }
                    else {this.variant = Variant.CURVE;
                        this.path = getClass().getResource("/textures/gameboard/foerderbandAnimatedKurve.gif").toString();}


                }
                case EAST -> {
                    if(out == Direction.WEST){
                        this.variant = Variant.STRAIGHT;
                        this.path = getClass().getResource("/textures/gameboard/foerderbandGeradeAnimated.gif").toString();
                    }
                    else {this.variant = Variant.CURVE;
                        this.path = getClass().getResource("/textures/gameboard/foerderbandAnimatedKurve.gif").toString();
                    }

                }
                case SOUTH -> {
                    if(out == Direction.NORTH){
                        this.variant = Variant.STRAIGHT;
                        this.path = getClass().getResource("/textures/gameboard/foerderbandGeradeAnimated.gif").toString();
                    }
                    else {this.variant = Variant.CURVE;
                        this.path = getClass().getResource("/textures/gameboard/foerderbandAnimatedKurve.gif").toString();}

                }
                case WEST -> {
                    if(out == Direction.EAST){
                        this.variant = Variant.STRAIGHT;
                        this.path = getClass().getResource("/textures/gameboard/foerderbandGeradeAnimated.gif").toString();
                    }
                    else {this.variant = Variant.CURVE;
                        this.path = getClass().getResource("/textures/gameboard/foerderbandAnimatedKurve.gif").toString();}

                }
            }
        }
        else if (in.size() == 2) {
            //path to T-Section conveyor belt
            //this.path = getClass().getResource("").toString();
            //TODO: create right rotation for t-section coveyor belt
        }
        else if (in.size()==3) {
            //path to Cross conveyor belt
            //this.path = getClass().getResource("").toString();
            //TODO: create right rotation for cross version of conveyor belt
        }
    }
}
