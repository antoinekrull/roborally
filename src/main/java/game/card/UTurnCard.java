package game.card;

import game.board.Direction;
import game.player.Player;

public class UTurnCard extends Card {

    public UTurnCard(){
        cardType = CardType.PROGRAMMING_CARD;
        setCardName("U-Turn");
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        switch (player.getRobot().getDirection()) {
            case NORTH -> player.getRobot().setDirection(Direction.SOUTH);
            case EAST -> player.getRobot().setDirection(Direction.WEST);
            case SOUTH -> player.getRobot().setDirection(Direction.NORTH);
            case WEST -> player.getRobot().setDirection(Direction.EAST);
            default -> throw new Exception("Invalid direction");
        }
    }
}


