package game.card;

import java.util.Collections;
import java.util.LinkedList;

/**
 * @author Dominik, Moritz
 * @version 1.0
 */

public abstract class Deck {

    protected LinkedList<Card> deck = new LinkedList<>();

    public Deck() {
        deck = new LinkedList<Card>();
    }

    /**
     * Creates the shuffled deck of cards according to the rules of the game.
     */
    public void createDeck() {}
    public Card popCardFromDeck() {
        return deck.pop();
    }
    public int getSize() {
        return deck.size();
    }
    public void addCard(Card card) {
        deck.add(card);
    }
    public void shuffleDeck() {
        Collections.shuffle(deck);
    }
}

