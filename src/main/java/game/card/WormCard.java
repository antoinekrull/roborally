package game.card;

import game.player.Player;

public class WormCard extends Card {
    //immediately reboot your robot
    public WormCard() {
        setCardName("Worm");
        super.isDamageCard = true;
    }
    @Override
    public void applyEffect(Player player) throws Exception {
        player.getRobot().reboot(0);
    }
}
