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
    //this is only used by the memory swap card, since  swapped cards are placed on top of the deck
    public void addCard(Card card, int index){deck.add(index, card);}
    public void shuffleDeck() {
        Collections.shuffle(deck);
    }
}

