package game.card;

import game.CollisionCalculator;
import game.player.Player;
import org.javatuples.Pair;

public class SpeedRoutineCard extends Card{
    public SpeedRoutineCard(){
        cardType = CardType.SPECIAL_PROGRAMMING_CARD;
        setCardName("Speed Routine");
    }
    @Override
    public void applyEffect(Player player) throws Exception {
        Pair<Integer, Integer> newPosition = new Pair<>(player.getRobot().getCurrentPosition().getValue0(),
                player.getRobot().getCurrentPosition().getValue1());
        for(int i = 0; i < 3; i++){
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
    }
}
