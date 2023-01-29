package game.card;

import game.player.Player;

import java.util.ArrayList;

import static game.Game.spamDeck;

public class SpamBlockerCard extends Card{
    public SpamBlockerCard(){
        cardType = CardType.UPGRADE_CARD;
        setCard("SpamBlocker");
        setCost(3);
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        boolean effectNeeded = false;
        ArrayList<Integer> spamLocations = new ArrayList<>();
        for(int i = 0; i < player.getHand().size(); i++){
            if(player.getHand().get(i) instanceof SpamCard){
                effectNeeded = true;
                spamLocations.add(i);
            }
        }
        if(effectNeeded){
            for(int i = 0; i < spamLocations.size(); i++){
                spamDeck.addCard(player.getHand().get(spamLocations.get(i)));
                //the card is removed from the hand instead of being discarded because it was added to the spamDeck
                player.getHand().remove(player.getHand().get(spamLocations.get(i)));
            }
            for (int i = 0; i < spamLocations.size(); i++){
                player.drawCard();
            }
        }
    }
}
