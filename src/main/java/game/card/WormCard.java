package game.card;

import game.robot.Robot;

public class WormCard extends Card {
    //immediately reboot your robot
    public WormCard() {setCardName("Worm");}
    @Override
    public void applyEffect(Robot robot) throws Exception {
        robot.reboot(0);
    }
}
