package game.card;

import java.util.Collections;
import java.util.LinkedList;

/**
 * @author Dominik, Moritz
 * @version 1.0
 */

public class Deck {

    private LinkedList<Card> deck;

    public Deck() {
        deck = new LinkedList<Card>();
    }

    /**
     * Creates the shuffled deck of cards according to the rules of the game.
     */
    public void createDeck() {
        //TODO: Implement creation of Deck

        Collections.shuffle(deck);
    }
    public Card popCardFromDeck() {
        Card drawnCard = deck.pop();
        return drawnCard;
    }
    public int getSize() {
        return deck.size();
    }
    public void addCard(Card card) {
        deck.add(card);
    }
}

