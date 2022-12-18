package game.card;

import game.Game;
import game.robot.Robot;

public class TrojanCard extends Card{
    //immediately take 2 spam, play the top card of your programming deck this register
    public TrojanCard(){setCardName("Trojan Horse");}
    @Override
    public void applyEffect(Robot robot) throws Exception {
        robot.getOwner().addCard(Game.spamDeck.popCardFromDeck());
        robot.getOwner().addCard(Game.spamDeck.popCardFromDeck());
        Card topProgrammingCard = robot.getDeck().popCardFromDeck();
        robot.setRegister(robot.getActiveRegister(), topProgrammingCard);
    }
}
