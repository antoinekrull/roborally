package game.card;

public class SpamDeck extends Deck {
    @Override
    public void createDeck() {
        for(int i = 0; i < 38; i++){
            addCard(new SpamCard());
        }
    }
}
