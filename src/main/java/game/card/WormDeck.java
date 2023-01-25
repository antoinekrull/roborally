package game.card;

public class WormDeck extends Deck {

    public WormDeck() {
        for(int i = 0; i < 6; i++){
            addCard(new WormCard());
        }
    }
    @Override
    public void createDeck() {
        for(int i = 0; i < 6; i++){
            addCard(new WormCard());
        }
    }
}
