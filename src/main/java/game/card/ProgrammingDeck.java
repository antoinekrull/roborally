package game.card;

import java.util.Collections;
import java.util.LinkedList;

public class ProgrammingDeck extends Deck{

    @Override
    public void createDeck() {
        for(int i = 0; i < 5; i++) {
            addCard(new MoveCard1());
        }
        for(int i = 0; i < 3; i++) {
            addCard(new MoveCard2());
            addCard(new TurnRightCard());
            addCard(new TurnLeftCard());
        }
        for(int i = 0; i < 2; i ++){
            addCard(new AgainCard());
        }
        addCard(new MoveCard3());
        addCard(new BackUpCard());
        addCard(new PowerUpCard());
        addCard(new UTurnCard());
        Collections.shuffle(super.deck);
    }
}
