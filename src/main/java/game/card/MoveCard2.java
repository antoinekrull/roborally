package game.card;

import game.player.Player;
import org.javatuples.Pair;

public class MoveCard2 extends Card {
    int velocity = 2;

    public MoveCard2(){
        setCardName("Move 2");
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
        switch(player.getRobot().getDirection()){
            case NORTH -> newPosition.setAt0(newPosition.getValue0() + velocity);
            case SOUTH -> newPosition.setAt0(newPosition.getValue0() - velocity);
            case EAST -> newPosition.setAt1(newPosition.getValue1() + velocity);
            case WEST -> newPosition.setAt1(newPosition.getValue1() - velocity);
        }
        player.getRobot().setCurrentPosition(newPosition);
    }

}
