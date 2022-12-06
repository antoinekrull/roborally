package game.card;

import game.robot.Robot;

public class PowerUpCard extends Card{

    public PowerUpCard(){
        setCardName("Power Up");
    }

    @Override
    public void applyEffect(Robot robot) throws Exception {
        robot.increaseEnergyCubes();
    }

}
