package game.card;

import game.player.Player;

public class AdminPrivilegeCard extends Card{
    public AdminPrivilegeCard(){
        cardType = CardType.UPGRADE_CARD;
        setCardName("Admin Privilege");
        setCost(3);
        setPermanent(true);
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        super.applyEffect(player);
    }
}
