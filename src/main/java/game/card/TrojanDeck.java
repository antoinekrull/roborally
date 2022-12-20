package game.card;

public class TrojanDeck extends Deck {
    @Override
    public void createDeck() {
        for(int i = 0; i < 12; i++){
            addCard(new TrojanCard());
        }
    }
}
