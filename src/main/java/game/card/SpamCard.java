package game.card;

import game.player.Player;

import static game.Game.currentRegister;

public class SpamCard extends Card {
    //play the top card of your programming deck this register
    public SpamCard(){
        cardType = CardType.DAMAGE_CARD;
        setCardName("Spam");
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        Card topProgrammingCard = player.getRobot().getDeck().popCardFromDeck();
        player.setCardRegister(this , currentRegister);
    }
}
