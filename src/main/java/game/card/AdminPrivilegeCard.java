package game.card;

import game.player.Player;

public class AdminPrivilegeCard extends Card{
    private int cost = 3;
    private boolean isPermanent = true;

    public boolean isPermanent() {
        return isPermanent;
    }

    public int getCost() {
        return cost;
    }

    public AdminPrivilegeCard(){
        cardType = CardType.UPGRADE_CARD;
        setCardName("Admin Privilege");
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        super.applyEffect(player);
    }
}
