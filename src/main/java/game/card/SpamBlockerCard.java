package game.card;

import game.player.Player;

public class SpamBlockerCard extends Card{
    private int cost = 3;
    private boolean isPermanent = false;

    public int getCost() {
        return cost;
    }
    public boolean isPermanent() {
        return isPermanent;
    }

    public SpamBlockerCard(){
        cardType = CardType.UPGRADE_CARD;
        setCardName("Spam Blocker");
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        super.applyEffect(player);
    }
}
