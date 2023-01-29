package game.card;

import game.player.Player;

public class MemorySwapCard extends Card{
    public MemorySwapCard(){
        cardType = CardType.UPGRADE_CARD;
        setCard("MemorySwap");
        setCost(1);
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        for(int i = 0; i < 3; i++){
            player.getHand().add(player.getRobot().getDeck().popCardFromDeck());
        }
        //player chooses cards to swap and cardsToSwap is filled
        //TODO: Check with gui team for what they need to implement this
        for(int i = 0; i < 3; i++){
            player.getRobot().getDeck().addCard(player.getCardsToSwap().peek(), 0);
            player.getCardsToSwap().remove(player.getCardsToSwap().peek());
        }
    }
}
