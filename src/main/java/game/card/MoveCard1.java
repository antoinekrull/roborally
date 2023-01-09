package game.card;

import game.player.Player;
import org.javatuples.Pair;
import game.CollisionCalculator;

public class MoveCard1 extends Card {
    int velocity = 1;

    public MoveCard1(){
        cardType = CardType.PROGRAMMING_CARD;
        setCardName("Move 1");
    }

    public int getVelocity() {
        return velocity;
    }
    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        Pair<Integer, Integer> newPosition = new Pair<>(player.getRobot().getCurrentPosition().getValue0(),
                player.getRobot().getCurrentPosition().getValue1());
        for(int i = 0; i < velocity; i++){
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
