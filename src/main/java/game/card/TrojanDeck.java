package game.card;

public class TrojanDeck extends Deck {

    public TrojanDeck() {
        for(int i = 0; i < 12; i++){
            addCard(new WormCard());
        }
    }
    @Override
    public void createDeck() {
        for(int i = 0; i < 12; i++){
            addCard(new TrojanCard());
        }
    }
}
