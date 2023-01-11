package game.card;

public class BackUpCard extends Card {
    int velocity = -1;

    public BackUpCard(){
        cardType = CardType.PROGRAMMING_CARD;
        setCardName("Move Back");
    }

    public int getVelocity() {
        return velocity;
    }
    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

}
