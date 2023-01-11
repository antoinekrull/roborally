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
        player.addCard(Game.spamDeck.popCardFromDeck());
        player.addCard(Game.spamDeck.popCardFromDeck());
        Card topProgrammingCard = player.getRobot().getDeck().popCardFromDeck();
        //TODO: Fix this
        //player.getRobot().setRegister(player.getRobot().getActiveRegister(), topProgrammingCard);
    }
}
