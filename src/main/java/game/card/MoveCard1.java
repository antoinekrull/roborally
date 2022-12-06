package game.card;

public class MoveCard1 extends Card {
    int velocity = 1;

    public MoveCard1(){
        setCardName("Move 1");
    }

    public int getVelocity() {
        return velocity;
    }
    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }
}
