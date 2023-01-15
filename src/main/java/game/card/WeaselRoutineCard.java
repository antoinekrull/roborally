package game.card;

import game.board.Direction;
import game.player.Player;

public class WeaselRoutineCard extends Card{
    public WeaselRoutineCard(){
        cardType = CardType.SPECIAL_PROGRAMMING_CARD;
        setCardName("Weasel Routine");
    }

    public void applyEffect(Player player, String cardName) throws Exception {
        switch (cardName){
            case "Left Turn" -> {
                switch (player.getRobot().getDirection()){
                    case NORTH -> player.getRobot().setDirection(Direction.WEST);
                    case WEST -> player.getRobot().setDirection(Direction.SOUTH);
                    case SOUTH -> player.getRobot().setDirection(Direction.EAST);
                    case EAST -> player.getRobot().setDirection(Direction.NORTH);
                    default -> throw new Exception("Invalid direction");
                }
            }
            case "Right Turn" -> {
                switch (player.getRobot().getDirection()){
                    case NORTH -> player.getRobot().setDirection(Direction.EAST);
                    case EAST -> player.getRobot().setDirection(Direction.SOUTH);
                    case SOUTH -> player.getRobot().setDirection(Direction.WEST);
                    case WEST -> player.getRobot().setDirection(Direction.NORTH);
                    default -> throw new Exception("Invalid direction");
                }
            }
            case "U-Turn" -> {
                switch (player.getRobot().getDirection()) {
                    case NORTH -> player.getRobot().setDirection(Direction.SOUTH);
                    case EAST -> player.getRobot().setDirection(Direction.WEST);
                    case SOUTH -> player.getRobot().setDirection(Direction.NORTH);
                    case WEST -> player.getRobot().setDirection(Direction.EAST);
                    default -> throw new Exception("Invalid direction");
                }
            }
        }
    }
}
