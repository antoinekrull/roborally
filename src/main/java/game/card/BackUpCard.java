package game.card;

import game.CollisionCalculator;
import game.player.Player;
import org.javatuples.Pair;

public class BackUpCard extends Card {
    int velocity = -1;

    public BackUpCard(){
        cardType = CardType.PROGRAMMING_CARD;
        setCardName("BackUp");
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        Pair<Integer, Integer> newPosition = new Pair<>(player.getRobot().getCurrentPosition().getValue0(),
                player.getRobot().getCurrentPosition().getValue1());
        for(int i = 0; i < Math.abs(velocity); i++){
            if(!CollisionCalculator.checkReverseRobotCollision(player)){
                Pair<Integer, Integer> tempPosition;
                switch(player.getRobot().getDirection()){
                    case NORTH -> tempPosition = newPosition.setAt0(newPosition.getValue0() - 1);
                    case SOUTH -> tempPosition = newPosition.setAt0(newPosition.getValue0() + 1);
                    case EAST -> tempPosition = newPosition.setAt1(newPosition.getValue1() - 1);
                    case WEST -> tempPosition = newPosition.setAt1(newPosition.getValue1() + 1);
                    default -> tempPosition = newPosition;
                }
                newPosition = tempPosition;
            }
        }
        player.getRobot().setCurrentPosition(newPosition);
    }

    public int getVelocity() {
        return velocity;
    }
    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

}
