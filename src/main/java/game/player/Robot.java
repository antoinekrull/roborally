package game.player;

import game.Game;
import game.board.Direction;
import game.board.RebootTile;
import game.card.ProgrammingDeck;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;
import server.connection.Server;

/**
 * @author Moritz, Antoine, Firas
 * @version 1.0
 */
public class Robot {

    private int figure;
    private int energyCubes;
    private int currentObjective;
    //currently only for DizzyHighWay, initialize position method needs to be implemented to set parameters
    private Direction direction;
    //note that the board has no normal coordinates, 0/0 is on the top left
    private Pair<Integer, Integer> currentPosition;
    private ProgrammingDeck deck = new ProgrammingDeck();
    private boolean isRebooted = false;
    private int activeRegister;
    private int id;
    private Server server;
    private final Logger logger = LogManager.getLogger(Robot.class);

    public Robot(int figure) {
        this.figure = figure;
        deck.createDeck();
        energyCubes = 0;
        currentObjective = 1;
    }

    public Pair<Integer, Integer> getCurrentPosition() {
        return currentPosition;
    }
    public void setCurrentPosition(Pair<Integer, Integer> currentPosition) {
        this.currentPosition = currentPosition;
        if(currentPosition.getValue0() < 13) {
            this.currentPosition = currentPosition.setAt0(13);
        } else if(currentPosition.getValue0() > 0) {
            this.currentPosition = currentPosition.setAt0(0);
        } else if(currentPosition.getValue1() < 10) {
            this.currentPosition = currentPosition.setAt1(10);
        } else if(currentPosition.getValue1() > 0) {
            this.currentPosition = currentPosition.setAt1(0);
        }
        server.sendMovement(this);
    }

    public void setServer(Server server) {
        this.server = server;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public int getCurrentObjective() {
        return currentObjective;
    }
    public void setCurrentObjective(int currentObjective) {
        this.currentObjective = currentObjective;
    }
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    public void rotateRobot(Direction direction) {
        try{
            server.sendPlayerTurning(this, direction);
            if(direction == Direction.RIGHT){
                switch (this.getDirection()){
                    case NORTH -> this.setDirection(Direction.EAST);
                    case EAST -> this.setDirection(Direction.SOUTH);
                    case SOUTH -> this.setDirection(Direction.WEST);
                    case WEST -> this.setDirection(Direction.NORTH);
                }
            } else if(direction == Direction.LEFT){
                switch (this.getDirection()){
                    case NORTH -> this.setDirection(Direction.WEST);
                    case WEST -> this.setDirection(Direction.SOUTH);
                    case SOUTH -> this.setDirection(Direction.EAST);
                    case EAST -> this.setDirection(Direction.NORTH);
                }
            } else {
                logger.warn("Invalid location");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Direction getDirection() {
        return direction;
    }
    public int getFigure() {return figure;}
    public void setFigure(int figure) {this.figure = figure;}
    public int getEnergyCubes() {
        return energyCubes;
    }
    public void setEnergyCubes(int energyCubes) {
        this.energyCubes = energyCubes;
    }
    public void increaseEnergyCubes() {
        energyCubes++;
    }
    public ProgrammingDeck getDeck(){
        return deck;
    }
    public void setDeck(ProgrammingDeck deck){
        this.deck = deck;
    }
    //TODO: Implement this
    public boolean getRebootStatus() {return isRebooted;}
    public void setRebootStatus(boolean rebooted) {isRebooted = rebooted;}
    public int getActiveRegister() {return activeRegister;}
    public void setActiveRegister(int activeRegister) {this.activeRegister = activeRegister;}

    public void shootLaser(Player player) throws Exception {
        //checkRobotLaserCollision(player);
    }

    public void makeImage(GridPane tiles){
        ImageView img = new ImageView();
        String path = "/textures/robots/Robot_" + figure + "_bunt.png";
        Image im = new Image(path,70, 70,true,false);
        img.setImage(im);
        tiles.add(img,currentPosition.getValue0(), currentPosition.getValue1());
    }

}
