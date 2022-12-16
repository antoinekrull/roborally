package game.card;

import game.robot.Robot;

public class SpamCard extends Card {
    //play the top card of your programming deck this register
    public SpamCard(){setCardName("Spam");}

    @Override
    public void applyEffect(Robot robot) throws Exception {
        super.applyEffect(robot);
    }
}
