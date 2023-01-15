package game.card;

import game.player.Player;

public class WeaselRoutineCard extends Card{
    public WeaselRoutineCard(){
        cardType = CardType.SPECIAL_PROGRAMMING_CARD;
        setCardName("Weasel Routine");
    }

    @Override
    public void applyEffect(Player player) throws Exception {

    }
}
