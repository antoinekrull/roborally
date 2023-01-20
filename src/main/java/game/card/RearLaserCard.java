package game.card;

import game.player.Player;

public class RearLaserCard extends Card{
    public RearLaserCard(){
        cardType = CardType.UPGRADE_CARD;
        setCardName("Rear Laser");
        setCost(2);
        setPermanent(true);
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        super.applyEffect(player);
    }
}
