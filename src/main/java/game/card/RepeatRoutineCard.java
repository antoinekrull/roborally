package game.card;

import game.player.Player;

public class RepeatRoutineCard extends Card{
    public RepeatRoutineCard(){
        cardType = CardType.SPECIAL_PROGRAMMING_CARD;
        setCardName("Repeat Routine");
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        if (player.getCurrentRegister(this) == 0) {
        } else {
            int previousRegister = player.getCurrentRegister(this) - 1;
            Card previousCard =player.getCardFromRegister(previousRegister);
            if(previousCard.isDamageCard){
                //if the previous card is a damage card, a new card is drawn from the deck and played in its stead
                player.setCardRegister(player.getRobot().getDeck().popCardFromDeck(), previousRegister);
                player.getCardFromRegister(previousRegister).applyEffect(player);
            } //else if(previousCard.getCardType().equals(CardType.UPGRADE_CARD)){} //needs more details
            else{
                player.getCardFromRegister(previousRegister).applyEffect(player);
            }
        }
    }
}
