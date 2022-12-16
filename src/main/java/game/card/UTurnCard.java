package game.card;

import game.board.Direction;
import game.robot.Robot;

public class UTurnCard extends Card {

    public UTurnCard(){
        setCardName("U-Turn");
    }

    @Override
    public void applyEffect(Robot robot) throws Exception {
        switch (robot.getDirection()) {
            case NORTH -> robot.setDirection(Direction.SOUTH);
            case EAST -> robot.setDirection(Direction.WEST);
            case SOUTH -> robot.setDirection(Direction.NORTH);
            case WEST -> robot.setDirection(Direction.EAST);
            default -> throw new Exception("Invalid direction");
        }
    }
}


