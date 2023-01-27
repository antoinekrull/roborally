package game.card;

import game.player.Player;

public class PowerUpCard extends Card{

    public PowerUpCard(){
        cardType = CardType.PROGRAMMING_CARD;
        setCard("PowerUp");
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        player.getRobot().increaseEnergyCubes("PowerUpCard");
    }

}
