package game.card;

import game.player.Player;

public class PowerUpCard extends Card{

    public PowerUpCard(){
        cardType = CardType.PROGRAMMING_CARD;
        setCardName("Power Up");
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        player.getRobot().increaseEnergyCubes();
    }

}
