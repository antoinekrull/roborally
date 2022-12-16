package game.card;

import game.robot.Robot;

public class VirusCard extends Card {
    //robots within a 6 space radius of your robot immediately take 1 spam, play the top card of your programming deck this register
    public VirusCard(){setCardName("Virus");}

    @Override
    public void applyEffect(Robot robot) throws Exception {
        super.applyEffect(robot);
    }
}
