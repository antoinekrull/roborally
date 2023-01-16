package game.card;

import game.board.Direction;
import game.player.Player;

public class TurnLeftCard extends Card {
    private final Direction rotationType = Direction.LEFT;

    public TurnLeftCard(){
        cardType = CardType.PROGRAMMING_CARD;
        setCardName("Left Turn");
    }

    public Direction getRotationType(){
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
