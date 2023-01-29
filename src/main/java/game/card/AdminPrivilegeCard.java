package game.card;

import game.player.Player;

public class AdminPrivilegeCard extends Card{
    public AdminPrivilegeCard(){
        cardType = CardType.UPGRADE_CARD;
        setCard("AdminPrivilege");
        setCost(3);
        setPermanent(true);
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        player.setHasAdminPrivilege(true);
    }
}
