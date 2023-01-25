package game.card;

import game.board.Direction;
import game.player.Player;

public class TurnRightCard extends Card {
    private final Direction rotationType = Direction.RIGHT;

    public TurnRightCard(){
        cardType = CardType.PROGRAMMING_CARD;
        setCard("TurnRight");
    }

    public Direction getRotationType(){
        return rotationType;
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        player.getRobot().rotateRobot(rotationType);
    }
}
