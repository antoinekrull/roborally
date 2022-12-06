package game.card;

public class MoveCard3 extends Card {
    int velocity = 3;

    public MoveCard3(){
        setCardName("Move 3");
    }

    public int getVelocity() {
        return velocity;
    }
    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }
}
