package game.card;

import game.player.Player;

public class AgainCard extends Card{


    public AgainCard(){
        cardType = CardType.PROGRAMMING_CARD;
        setCardName("Again");
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        int previousRegister = player.getCurrentRegister(this) - 1;
        player.getCardFromRegister(previousRegister).applyEffect(player);
    }
}
