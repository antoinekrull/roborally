package game.card;

import game.player.Player;
import game.robot.Robot;

import static game.Game.currentRegister;

public class SpamCard extends Card {
    //play the top card of your programming deck this register
    public SpamCard(){setCardName("Spam");}

    @Override
    public void applyEffect(Player player) throws Exception {
        Card topProgrammingCard = player.getRobot().getDeck().popCardFromDeck();
        player.setCardRegister(this , currentRegister);
    }
}
