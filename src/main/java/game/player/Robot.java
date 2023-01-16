package game.player;
import game.board.Tile;
import game.board.Direction;
import game.card.ProgrammingDeck;
import org.javatuples.Pair;

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
    public void reboot(int rebootTileIndex){}
    public boolean getRebootStatus() {return isRebooted;}
    public void setRebootStatus(boolean rebooted) {isRebooted = rebooted;}
    public int getActiveRegister() {return activeRegister;}
    public void setActiveRegister(int activeRegister) {this.activeRegister = activeRegister;}

    public void shootLaser(Player player) throws Exception {
        //checkRobotLaserCollision(player);
    }

}
