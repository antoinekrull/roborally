package game.card;

import game.board.Direction;
import game.board.RotationType;
import game.player.Player;

public class TurnLeftCard extends Card {
    private final RotationType rotationType = RotationType.LEFT;

    public TurnLeftCard(){
        setCardName("Left Turn");
    }

    public RotationType getRotationType(){
        return rotationType;
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        switch (player.getRobot().getDirection()){
            case NORTH -> player.getRobot().setDirection(Direction.WEST);
            case WEST -> player.getRobot().setDirection(Direction.SOUTH);
            case SOUTH -> player.getRobot().setDirection(Direction.EAST);
            case EAST -> player.getRobot().setDirection(Direction.NORTH);
            default -> throw new Exception("Invalid direction");
        }
    }
}
