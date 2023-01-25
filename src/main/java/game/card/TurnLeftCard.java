package game.card;

import game.board.Direction;
import game.player.Player;

public class TurnLeftCard extends Card {
    private final Direction rotationType = Direction.LEFT;

    public TurnLeftCard(){
        cardType = CardType.PROGRAMMING_CARD;
        setCard("TurnLeft");
    }

    public Direction getRotationType(){
        return rotationType;
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        player.getRobot().rotateRobot(rotationType);
    }
}
