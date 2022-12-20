package game.card;

public class WormDeck extends Deck {
    @Override
    public void createDeck() {
        for(int i = 0; i < 6; i++){
            addCard(new WormCard());
        }
    }
}
