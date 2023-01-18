package game.card;

import game.player.Player;

public class RearLaserCard extends Card{
    private int cost = 2;
    private boolean isPermanent = true;

    public int getCost() {
        return cost;
    }

    public boolean isPermanent() {
        return isPermanent;
    }

    public RearLaserCard(){
        cardType = CardType.UPGRADE_CARD;
        setCardName("Rear Laser");
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        super.applyEffect(player);
    }
}
