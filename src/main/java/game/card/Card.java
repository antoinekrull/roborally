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


    private int clientID;
    private String card;
    private boolean isActivated = false;
    protected boolean isDamageCard = false;
    private String cardEffect;
    protected CardType cardType;

    public int getClientID() {
        return clientID;
    }
    public void setClientID(int clientID) {
        this.clientID = clientID;
    }
    public CardType getCardType() {
        return cardType;
    }
    public String getCard() {
        return card;
    }
    public void setCard(String newCardName) {
        this.card = newCardName;
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
