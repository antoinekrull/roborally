package game.card;

import game.Game;
import game.player.Player;

public class TrojanCard extends Card{
    //immediately take 2 spam, play the top card of your programming deck this register
    public TrojanCard(){
        cardType = CardType.DAMAGE_CARD;
        setCardName("Trojan Horse");
    }
    @Override
    public void applyEffect(Player player) throws Exception {
        player.getHand().add(Game.spamDeck.popCardFromDeck());
        player.getHand().add(Game.spamDeck.popCardFromDeck());
        Card topProgrammingCard = player.getRobot().getDeck().popCardFromDeck();
        player.setCardRegister(topProgrammingCard, player.getRobot().getActiveRegister());
    }
}
