package game.card;

import game.robot.Robot;

public class TrojanCard extends Card{
    //immediately take 2 spam, play the top card of your programming deck this register
    public TrojanCard(){setCardName("Trojan Horse");}
    @Override
    public void applyEffect(Robot robot) throws Exception {
        super.applyEffect(robot);
    }
}
