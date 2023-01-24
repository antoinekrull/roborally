package game.card;

import game.player.Player;

public class AgainCard extends Card{


    public AgainCard(){
        cardType = CardType.PROGRAMMING_CARD;
        setCard("Again");
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        if (player.getCurrentRegister(this) == 0) {
        } else {
            int previousRegister = player.getCurrentRegister(this) - 1;
            player.getCardFromRegister(previousRegister).applyEffect(player);
        }
    }
}
