package game.card;

import game.player.Player;

public class SpamBlockerCard extends Card{
    public SpamBlockerCard(){
        cardType = CardType.UPGRADE_CARD;
        setCardName("Spam Blocker");
        setCost(3);
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        super.applyEffect(player);
    }
}
