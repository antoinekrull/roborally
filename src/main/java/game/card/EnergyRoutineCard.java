package game.card;

import game.player.Player;

public class EnergyRoutineCard extends Card{
    public EnergyRoutineCard(){
        cardType = CardType.SPECIAL_PROGRAMMING_CARD;
        setCardName("Energy Routine");
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        player.getRobot().setEnergyCubes(player.getRobot().getEnergyCubes() + 1);
    }
}
