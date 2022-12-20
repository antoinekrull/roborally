package game.card;

public class VirusDeck extends Deck{
    @Override
    public void createDeck() {
        for(int i = 0; i < 18; i++){
            addCard(new VirusCard());
        }
    }
}
