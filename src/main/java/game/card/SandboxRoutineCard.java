package game.card;

import game.player.Player;

public class SandboxRoutineCard extends Card{
    public SandboxRoutineCard(){
        cardType = CardType.SPECIAL_PROGRAMMING_CARD;
        setCardName("Sandbox Routine");
    }

    @Override
    public void applyEffect(Player player) throws Exception {

    }
}
