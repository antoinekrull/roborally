package game.card;

import game.player.Player;

public class RearLaserCard extends Card{
    public RearLaserCard(){
        cardType = CardType.UPGRADE_CARD;
        setCard("RearLaser");
        setCost(2);
        setPermanent(true);
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        player.setRearLaserOn(true);
    }
}
