package game.card;

import game.player.Player;

public class AgainCard extends Card{

    private CardType cardType = CardType.PROGRAMMING_CARD;

    public AgainCard(){
        setCardName("Again");
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        int previousRegister = player.getRobot().getOwner().getCurrentRegister(this) - 1;
        player.getRobot().getOwner().getCardFromRegister(previousRegister).applyEffect(player);
    }
}
