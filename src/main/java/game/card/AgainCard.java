package game.card;

import game.player.Player;

public class AgainCard extends Card{

    private CardType cardType = CardType.PROGRAMMING_CARD;

    public AgainCard(){
        setCardName("Again");
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        int previousRegister = player.getCurrentRegister(this) - 1;
        player.getCardFromRegister(previousRegister).applyEffect(player);
    }
}
