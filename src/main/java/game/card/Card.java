package game.card;

/**
 * @author Moritz
 * @version 1.0
 */

import game.player.Player;

/**
 * Combines the features for a playable card.
 */
public abstract class Card {

    private String cardName;
    private boolean isActivated = false;
    protected boolean isDamageCard = false;
    private String cardEffect;
    protected CardType cardType;

    public CardType getCardType() {
        return cardType;
    }
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
    public boolean isActivated() {
        return isActivated;
    }
    public void setActivated(boolean activated) {
        isActivated = activated;
    }
    public boolean isDamageCard() {
        return isDamageCard;
    }
    public void setDamageCard(boolean damageCard) {
        isDamageCard = damageCard;
    }

    public void applyEffect(Player player) throws Exception {}

}
