package game.card;

import game.player.Player;
import game.robot.Robot;

public class PowerUpCard extends Card{

    public PowerUpCard(){
        setCardName("Power Up");
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        player.getRobot().increaseEnergyCubes();
    }

}
