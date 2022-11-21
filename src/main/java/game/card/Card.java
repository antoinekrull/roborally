package game.card;

/**
 * @author Moritz
 * @version 1.0
 */

/**
 * Combines the features for a playable card.
 */
public abstract class Card {

    private String cardName;

    private String cardEffect;

    private CardType NONE;

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String newCardName) {
        this.cardName = newCardName;
    }

    public String getCardEffect() {
        return cardEffect;
    }

    public void setCardEffect(String newCardEffect) {
        this.cardEffect = newCardEffect;
    }
}

