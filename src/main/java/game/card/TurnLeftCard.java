package game.card;

import game.board.Direction;
import game.board.RotationType;
import game.robot.Robot;

public class TurnLeftCard extends Card {
    private final RotationType rotationType = RotationType.LEFT;

    public TurnLeftCard(){
        setCardName("Left Turn");
    }

    public RotationType getRotationType(){
        return rotationType;
    }

    @Override
    public void applyEffect(Robot robot) throws Exception {
        switch (robot.getDirection()){
            case NORTH -> robot.setDirection(Direction.WEST);
            case WEST -> robot.setDirection(Direction.SOUTH);
            case SOUTH -> robot.setDirection(Direction.EAST);
            case EAST -> robot.setDirection(Direction.NORTH);
            default -> throw new Exception("Invalid direction");
        }
    }
}
