package game.card;

public class SpamDeck extends Deck {

    public SpamDeck() {
        for(int i = 0; i < 38; i++){
            addCard(new SpamCard());
        }
    }

    @Override
    public void createDeck() {
        for(int i = 0; i < 38; i++){
            addCard(new SpamCard());
        }
    }
}
