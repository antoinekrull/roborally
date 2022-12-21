package game.board;

import game.Game;
import game.player.Player;
import org.javatuples.Pair;

import java.util.ArrayList;

public class PushPanelTile extends Tile {

    Direction pushDirection;
    ArrayList<Integer> registers;
    Pair<Integer, Integer> tileLocation;
    public PushPanelTile(int xCoordinate, int yCoordinate, Direction pushDirection, ArrayList<Integer> registers) {
        super(xCoordinate, yCoordinate, "/textures/gameboard/foerderbandGeradeAnimated.gif");
        this.path = getClass().getResource("/textures/gameboard/foerderbandGeradeAnimated.gif").toString();
        isBlocking = true;
        isDanger = false;
        this.pushDirection = pushDirection;
        this.registers = registers;
        tileLocation = new Pair<>(xCoordinate, yCoordinate);
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        //TODO: Ask GUI team if they need extra info for animations
        if(registers.contains(Game.currentRegister)){
            Pair<Integer, Integer> newPosition = new Pair<>(player.getRobot().getCurrentPosition().getValue0(),
                    player.getRobot().getCurrentPosition().getValue1());
            if(player.getRobot().getCurrentPosition().equals(tileLocation)){
                switch(this.pushDirection){
                    case NORTH -> newPosition.setAt1(newPosition.getValue1() + 1);
                    case SOUTH -> newPosition.setAt1(newPosition.getValue1() - 1);
                    case EAST -> newPosition.setAt0(newPosition.getValue0() + 1);
                    case WEST -> newPosition.setAt0(newPosition.getValue0() - 1);
                    default -> throw(new Exception("Invalid direction"));
                }
                player.getRobot().setCurrentPosition(newPosition);
            }
        }
    }
}
