package game.card;

import game.player.Player;

public class MemorySwapCard extends Card{
    private int cost = 1;
    private boolean isPermanent = false;

    public int getCost() {
        return cost;
    }
    public boolean isPermanent() {
        return isPermanent;
    }

    public MemorySwapCard(){
        cardType = CardType.UPGRADE_CARD;
        setCardName("Memory Swap");
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        super.applyEffect(player);
    }
}
