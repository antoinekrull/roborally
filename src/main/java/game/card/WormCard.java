package game.card;

import game.player.Player;

public class WormCard extends Card {
    //immediately reboot your robot
    public WormCard() {
        cardType = CardType.DAMAGE_CARD;
        setCardName("Worm");
    }
    @Override
    public void applyEffect(Player player) throws Exception {
        //player.getRobot().reboot(0);
    }
}
