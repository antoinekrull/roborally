package game.card;

import game.player.Player;

public class MemorySwapCard extends Card{
    public MemorySwapCard(){
        cardType = CardType.UPGRADE_CARD;
        setCard("Memory Swap");
        setCost(1);
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        super.applyEffect(player);
    }
}
