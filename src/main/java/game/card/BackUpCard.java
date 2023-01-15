package game.card;

import game.CollisionCalculator;
import game.board.Direction;
import game.player.Player;
import org.javatuples.Pair;

public class BackUpCard extends Card {
    int velocity = -1;

    public BackUpCard(){
        cardType = CardType.PROGRAMMING_CARD;
        setCardName("Move Back");
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        Direction originalDirection = player.getRobot().getDirection(); //the original direction is saved
        //the direction is flipped to allow for collision checking, will be later restored to its original value
        switch (player.getRobot().getDirection()){
            case NORTH -> player.getRobot().setDirection(Direction.SOUTH);
            case SOUTH -> player.getRobot().setDirection(Direction.NORTH);
            case EAST -> player.getRobot().setDirection(Direction.WEST);
            case WEST -> player.getRobot().setDirection(Direction.EAST);
        }
        //code from MoveCard1
        Pair<Integer, Integer> newPosition = new Pair<>(player.getRobot().getCurrentPosition().getValue0(),
                player.getRobot().getCurrentPosition().getValue1());
        for(int i = 0; i < 1; i++){
            if(CollisionCalculator.checkRobotCollision(player)){
                switch(player.getRobot().getDirection()){
                    case NORTH -> newPosition.setAt0(newPosition.getValue0() + 1);
                    case SOUTH -> newPosition.setAt0(newPosition.getValue0() - 1);
                    case EAST -> newPosition.setAt1(newPosition.getValue1() + 1);
                    case WEST -> newPosition.setAt1(newPosition.getValue1() - 1);
                }
            }
        }
        player.getRobot().setCurrentPosition(newPosition);
        player.getRobot().setDirection(originalDirection);
    }
}
