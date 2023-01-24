package game.card;

import game.CollisionCalculator;
import game.player.Player;
import org.javatuples.Pair;

public class MoveCard2 extends Card {
    int velocity = 2;

    public MoveCard2(){
        cardType = CardType.PROGRAMMING_CARD;
        setCard("MoveII");
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
            if(!CollisionCalculator.checkRobotCollision(player)){
                Pair<Integer, Integer> tempPosition;
                switch(player.getRobot().getDirection()){
                    case NORTH -> tempPosition = newPosition.setAt0(newPosition.getValue0() + 1);
                    case SOUTH -> tempPosition = newPosition.setAt0(newPosition.getValue0() - 1);
                    case EAST -> tempPosition = newPosition.setAt1(newPosition.getValue1() + 1);
                    case WEST -> tempPosition = newPosition.setAt1(newPosition.getValue1() - 1);
                    default -> tempPosition = newPosition;
                }
                newPosition = tempPosition;
            }
        }
        player.getRobot().setCurrentPosition(newPosition);
        //TODO: Add pit check
    }

}
