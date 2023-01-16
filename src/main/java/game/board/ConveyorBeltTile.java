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
        STRAIGHT, CURVE, TSECTION
    }
    private Variant variant;


    public ConveyorBeltTile(int xCoordinate, int yCoordinate, int velocity, ArrayList<Direction> directionIn, Direction directionOut) {
        super(xCoordinate, yCoordinate, "/textures/gameboard/foerderbandGeradeAnimated.gif");
        setType("ConveyorBelt");
        isDanger = false;
        isBlocking = false;
        this.velocity = velocity;
        this.directionIn = directionIn;
        this.directionOut = directionOut;
        setVariant(this.directionIn, this.directionOut, velocity);
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
    public Variant getVariant() {return variant;}
    private final String pathToStraight = "/textures/gameboard/foerderbandGeradeAnimated.gif";
    private final String pathToFastStraight = "/textures/gameboard/foerderbandGeradeAnimatedROT_Schnell.gif";
    private final String pathToCurve ="/textures/gameboard/foerderbandAnimatedKurve.gif";
    private final String pathToFastCurve = "/textures/gameboard/foerderbandAnimatedKurveROT.gif";
    private final String pathToTSection = "/textures/gameboard/AnimatedConveyorBelt_T.gif";
    private final String pathToFastTSection = "/textures/gameboard/AnimatedConveyorBelt_T_ROT.gif";


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
                    case NORTH -> rot = 0;
                    case EAST -> rot = 90;
                    case SOUTH -> rot = 180;
                    case WEST -> rot = 270;
                }
            }
            case CURVE -> {
                switch (directionOut) {
                    case NORTH -> {
                        if (directionIn.get(0) != Direction.EAST) {
                            img.setScaleX(-1);
                            rot = 90;
                        } else rot = 270;
                    }
                    case EAST -> {
                        if (directionIn.get(0) != Direction.SOUTH) {
                            img.setScaleY(-1);
                        }
                    }
                    case SOUTH -> {
                        if (directionIn.get(0) != Direction.WEST) {
                            img.setScaleX(-1);
                            rot = 270;
                        } else rot = 90;
                    }
                    case WEST -> {
                        if (directionIn.get(0) != Direction.NORTH) {
                            img.setScaleX(-1);
                        }else rot = 180;
                    }
                }
            }
            case TSECTION -> {
                switch (directionOut) {
                    case NORTH -> {
                        if(directionIn.contains(Direction.WEST)) {
                            img.setScaleX(-1);
                        }
                    }
                    case EAST -> {
                        rot = 90;
                        if(directionIn.contains(Direction.NORTH)) {
                            img.setScaleX(-1);
                        }
                    }
                    case SOUTH -> {
                        if(directionIn.contains(Direction.EAST)) {
                            img.setScaleY(-1);
                        } else rot = 180;
                    }
                    case WEST -> {
                        rot = 270;
                        if(directionIn.contains(Direction.SOUTH)){
                            img.setScaleX(-1);
                        }
                    }
                }
            }
        }
        img.setRotate(rot);
        tiles.add(img, this.xCoordinate, this.yCoordinate);
    }

    private void setVariant(ArrayList<Direction> in, Direction out, int velocity){
        if (in.size() == 1){
            switch (in.get(0)) {
                case NORTH -> {
                    if(out == Direction.SOUTH){
                        this.variant = Variant.STRAIGHT;
                    }
                    else {this.variant = Variant.CURVE;}


                }
                case EAST -> {
                    if(out == Direction.WEST){this.variant = Variant.STRAIGHT;}
                    else {this.variant = Variant.CURVE;}

                }
                case SOUTH -> {
                    if(out == Direction.NORTH){this.variant = Variant.STRAIGHT;}
                    else {this.variant = Variant.CURVE;}

                }
                case WEST -> {
                    if(out == Direction.EAST){this.variant = Variant.STRAIGHT;}
                    else {this.variant = Variant.CURVE;}
                }
            }
            switch(variant){
                case STRAIGHT ->{
                    switch (velocity){
                        case 1 -> this.path = getClass().getResource(pathToStraight).toString();
                        case 2 -> this.path = getClass().getResource(pathToFastStraight).toString();
                    }
                }
                case CURVE -> {
                    switch (velocity){
                        case 1 -> this.path = getClass().getResource(pathToCurve).toString();
                        case 2 -> this.path = getClass().getResource(pathToFastCurve).toString();
                    }
                }
            }
        }
        else if (in.size() == 2) {
            //path to T-Section conveyor belt
            this.variant = Variant.TSECTION;
            switch (velocity) {
                case 1 ->this.path = getClass().getResource(pathToTSection).toString();
                case 2 ->this.path = getClass().getResource(pathToFastTSection).toString();
            }
        }
        else {
            System.out.println("irgendwas stimmt mit Conveyorbelt nicht :/");
        }
    }
}
